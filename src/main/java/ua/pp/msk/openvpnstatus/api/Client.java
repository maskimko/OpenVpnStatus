/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.api;

import java.net.InetSocketAddress;
import java.util.Calendar;

/**
 *
 * @author maskimko
 */
public interface Client {

    public String getCommonName();

    public InetSocketAddress getIpAddress();

    public long getReceivedBytes();

    public long getSentBytes();

    public Calendar getConnectedSince();
}
