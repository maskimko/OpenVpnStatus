/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Calendar;

/**
 *
 * @author maskimko
 */
public interface Route {

    //Virtual Address,Common Name,Real Address,Last Ref

    public InetAddress getVirtualIpAddress();

    public String getCommonName();

    public InetSocketAddress getRealIpAddress();

    /**
     * As I understand it should return connection date
     *
     * @return date since client acquired the connection
     */
    public Calendar getLastRef();
}
