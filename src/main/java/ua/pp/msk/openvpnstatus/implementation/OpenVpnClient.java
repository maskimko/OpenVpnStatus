/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.implementation;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import ua.pp.msk.openvpnstatus.api.Client;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;

/**
 *
 * @author maskimko
 */
public class OpenVpnClient implements Client, Serializable {

    private String commonName;
    private InetSocketAddress realIp;
    private long receivedBytes;
    private long sentBytes;
    private Calendar connectedSince;

    @Override
    public String getCommonName() {
        return commonName;
    }

    @Override
    public InetSocketAddress getIpAddress() {
        return realIp;
    }

    @Override
    public long getReceivedBytes() {
        return receivedBytes;
    }

    @Override
    public long getSentBytes() {
        return sentBytes;
    }

    @Override
    public Calendar getConnectedSince() {
        return connectedSince;
    }

    void setRawString(String s) throws OpenVpnParseException {
        String[] splitted = s.split(",");

        if (splitted.length != 5) {
            throw new OpenVpnParseException("Malformed route string! " + s + " Need to have 5 sections separated by commas");
        }
        try {
            setCommonName(splitted[0]);
            String[] realConnection = splitted[1].split(":");
            if (realConnection.length != 2) {
                throw new OpenVpnParseException("Malformed real connection string! " + splitted[2] + " Need to have 2 sections separated by colon");
            }
            InetAddress realIp = Inet4Address.getByName(realConnection[0]);
            int port = Integer.parseInt(realConnection[1]);
            InetSocketAddress realIpSocket = new InetSocketAddress(realIp, port);
            SimpleDateFormat sdf = new SimpleDateFormat(Status.DATE_FORMAT);
            Date parsedDate = sdf.parse(splitted[4]);
            Calendar connectedSince = Calendar.getInstance();
            connectedSince.setTime(parsedDate);
            long rb = Long.parseLong(splitted[2]);
            long sb = Long.parseLong(splitted[3]);
            setRealIp(realIpSocket);
            setReceivedBytes(rb);
            setSentBytes(sb);
            setConnectedSince(connectedSince);
        } catch (NumberFormatException ex) {
            throw new OpenVpnParseException("Cannot parse port number. ", ex);
        } catch (ParseException ex) {
            throw new OpenVpnParseException("Cannot Parse date." + ex);
        } catch (UnknownHostException ex) {
            throw new OpenVpnParseException("Cannot parse hostname.", ex);
        }
    }

    void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    void setRealIp(InetSocketAddress realIp) {
        this.realIp = realIp;
    }

    void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    void setConnectedSince(Calendar connectedSince) {
        this.connectedSince = connectedSince;
    }

}
