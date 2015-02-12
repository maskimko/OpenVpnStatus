/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.net;

import java.io.IOException;
import java.net.InetAddress;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnIOException;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;

/**
 *
 * @author maskimko
 */
public interface Connection extends AutoCloseable {

    public final static String STATUSCOMMAND = "status";

    public InetAddress getServerAddress();

    public int getServerPort();

    public String executeCommand(String command) throws OpenVpnIOException, IOException;

    public Status getStatus() throws OpenVpnParseException, OpenVpnIOException, IOException;

    public void connect() throws IOException;

    public void setServerAddress(InetAddress addr);

    public void setServerPort(int port);

    public boolean isKeepAlive();

    public void setKeepAlive(boolean keepAlive);

    public boolean isConnected();
}
