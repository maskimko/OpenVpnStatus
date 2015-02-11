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
import org.slf4j.LoggerFactory;
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
    private static ManagementConnection mc = null;

    public static ManagementConnection getConnection(InetAddress addr, int port, boolean keepAlive) {
        if (mc == null) {
            synchronized (ManagementConnection.class) {
                if (mc == null) {
                    mc = new ManagementConnection(addr, port, keepAlive);
                }
            }
        }
        return mc;
    }

    public static ManagementConnection getConnection(InetAddress addr, int port) {
        return getConnection(addr, port, false);
    }

    private ManagementConnection(InetAddress addr, int port) {
        this(addr, port, false);
    }

    private ManagementConnection(InetAddress addr, int port, boolean keepAlive) {
        this.addr = addr;
        this.port = port;
        this.keepAlive = keepAlive;
    }

    @Override
    public InetAddress getServerAddress() {
        return addr;
    }

    @Override
    public int getServerPort() {
        return port;
    }

    @Override
    public synchronized String executeCommand(String command) throws OpenVpnIOException, IOException {
        if (socket == null) {
            throw new OpenVpnIOException("Illegal state! Socket has been not initialized yet. Socket is null value");
        }
        if (!socket.isConnected()) {
            throw new OpenVpnIOException("Illegal state! Socket is not connected.");
        }
        PrintWriter out = new PrintWriter(os);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        out.print(command);
        out.print("\n");
        out.flush();
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
            LoggerFactory.getLogger(this.getClass()).debug("Read from socket line: " + line);
            if (!br.ready() && line.equals("END")) {
                LoggerFactory.getLogger(this.getClass()).debug("Buffered reader is not ready for reading. I will not wait for next readLine method. Exiting.");
                break;
            }
        }

        return sb.toString();
    }

    @Override
    public synchronized Status getStatus() throws OpenVpnParseException, OpenVpnIOException, IOException {
        OpenVpnStatus ovs = new OpenVpnStatus();
        ovs.setCommandOutput(executeCommand(Connection.STATUSCOMMAND));
        return ovs;
    }

    @Override
    public synchronized void connect() throws IOException {
        if (addr != null && port > 0) {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(addr, port);
                is = socket.getInputStream();
                os = socket.getOutputStream();
                socket.setKeepAlive(keepAlive);
            }
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
    public synchronized  void close() throws Exception {
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
