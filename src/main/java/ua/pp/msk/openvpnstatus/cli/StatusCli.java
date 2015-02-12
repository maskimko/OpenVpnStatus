/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.cli;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnIOException;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;
import ua.pp.msk.openvpnstatus.net.Connection;
import ua.pp.msk.openvpnstatus.net.ManagementConnection;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class StatusCli {

    private static void printInfo(Options o){
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("java -jar StatusCli [options]", o);
    }
    
    public static void main(String[] args) {
        try {
            Options opts = new Options();
            opts.addOption("H", "host", true, "OpenVPN server management interface address");
            opts.addOption("P", "port", true, "Management interface port");
            opts.addOption("h", "help", false, "Print usage information");
            CommandLineParser parser = new GnuParser();
            CommandLine cmd = parser.parse(opts, args);
            
            if (cmd.hasOption("help")){
                printInfo(opts);
                System.exit(0);
            }
            
            if (! cmd.hasOption("host") || !cmd.hasOption("port")) {
                printInfo(opts);
                LoggerFactory.getLogger(StatusCli.class).warn("Missing required options");
                System.exit(1);
            }
            
            String host = cmd.getOptionValue("host");
            int port = Integer.parseInt(cmd.getOptionValue("port"));
            InetAddress serverAddr = Inet4Address.getByName(host);
            Connection conn = ManagementConnection.getConnection(serverAddr, port);
            conn.connect();
            Status status = conn.getStatus();
            System.out.println("OpenVPN status: " + status.toString());
            
        } catch (ParseException ex) {
            LoggerFactory.getLogger(StatusCli.class).error("Cannot parse arguments", ex);
        } catch (UnknownHostException ex) {
            LoggerFactory.getLogger(StatusCli.class.getName()).error("Cannot resolve hostname ", ex);
        } catch (OpenVpnParseException | OpenVpnIOException | IOException ex) {
            LoggerFactory.getLogger(StatusCli.class.getName()).error("Cannot get OpenVPN status.", ex);
        }
    }
}
