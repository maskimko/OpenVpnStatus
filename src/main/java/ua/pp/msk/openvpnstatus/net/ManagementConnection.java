/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnIOException;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;
import ua.pp.msk.openvpnstatus.implementation.OpenVpnStatus;

/**
 *
 * @author maskimko
 */
public class ManagementConnection implements Connection {

    private InetAddress addr;
    private int port;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private boolean keepAlive;

    @Override
    public InetAddress getServerAddress() {
        return addr;
    }

    @Override
    public int getServerPort() {
        return port;
    }

    @Override
    public String executeCommand(String command) throws OpenVpnIOException, IOException {
        if (socket == null) {
            throw new OpenVpnIOException("Illegal state! Socket has been not initialized yet. Socket is null value");
        }
        if (!socket.isConnected()) {
            throw new OpenVpnIOException("Illegal state! Socket is not connected.");
        }
        PrintWriter out = new PrintWriter(os, true);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        out.print(command);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Status getStatus() throws OpenVpnParseException,  OpenVpnIOException, IOException {
        OpenVpnStatus ovs = new OpenVpnStatus();
        ovs.setCommandOutput(executeCommand(Connection.STATUSCOMMAND));
        return ovs;
    }

    @Override
    public void connect() throws IOException {
        if (addr != null && port > 0) {
            socket = new Socket(addr, port);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            socket.setKeepAlive(keepAlive);
        }
    }

    @Override
    public void setServerAddress(InetAddress addr) {
        this.addr = addr;
    }

    @Override
    public void setServerPort(int port) {
        this.port = port;
    }

    @Override
    public void close() throws Exception {
        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.flush();
            os.close();
        }
        if (socket != null) {

            socket.close();
        }
    }

    @Override
    public boolean isKeepAlive() {
        return keepAlive;
    }

    @Override
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    @Override
    public boolean isConnected() {
        if (socket == null) {
            return false;
        } else {
            return socket.isConnected();
        }
    }

}
