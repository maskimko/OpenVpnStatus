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
import java.util.Objects;
import ua.pp.msk.openvpnstatus.api.Route;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;

/**
 *
 * @author maskimko
 */
public class OpenVpnRoute implements Route, Serializable {

    private InetAddress virtIp;
    private String commonName;
    private InetSocketAddress realIp;
    private Calendar lastRef;

    @Override
    public InetAddress getVirtualIpAddress() {
        return virtIp;
    }

    @Override
    public String getCommonName() {
        return commonName;
    }

    @Override
    public InetSocketAddress getRealIpAddress() {
        return realIp;
    }

    @Override
    public Calendar getLastRef() {
        return lastRef;
    }

    void setRawString(String s) throws OpenVpnParseException {
        String[] splitted = s.split(",");

        if (splitted.length != 4) {
            throw new OpenVpnParseException("Malformed route string! " + s + " Need to have 4 sections separated by commas");
        }
        try {
            InetAddress virtIp = Inet4Address.getByName(splitted[0]);
            String[] realConnection = splitted[2].split(":");
            if (realConnection.length != 2) {
                throw new OpenVpnParseException("Malformed real connection string! " + splitted[2] + " Need to have 2 sections separated by colon");
            }
            InetAddress realIp = Inet4Address.getByName(realConnection[0]);
            int port = Integer.parseInt(realConnection[1]);
            InetSocketAddress realIpSocket = new InetSocketAddress(realIp, port);
            SimpleDateFormat sdf = new SimpleDateFormat(Status.DATE_FORMAT);
            Date parsedDate = sdf.parse(splitted[3]);
            Calendar lastRef = Calendar.getInstance();
            lastRef.setTime(parsedDate);
            setCommonName(splitted[1]);
            setVirtualIpAddress(virtIp);
            setRealIpAddress(realIpSocket);
            setLastRef(lastRef);
        } catch (NumberFormatException ex) {
            throw new OpenVpnParseException("Cannot parse port number. ", ex);
        } catch (ParseException ex) {
            throw new OpenVpnParseException("Cannot Parse date." + ex);
        } catch (UnknownHostException ex) {
            throw new OpenVpnParseException("Cannot parse hostname.", ex);
        }

    }

    void setVirtualIpAddress(InetAddress virtIp) {
        this.virtIp = virtIp;
    }

    void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    void setRealIpAddress(InetSocketAddress realIp) {
        this.realIp = realIp;
    }

    void setLastRef(Calendar lastRef) {
        this.lastRef = lastRef;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.virtIp);
        hash = 79 * hash + Objects.hashCode(this.commonName);
        hash = 79 * hash + Objects.hashCode(this.realIp);
        hash = 79 * hash + Objects.hashCode(this.lastRef);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpenVpnRoute other = (OpenVpnRoute) obj;
        if (!Objects.equals(this.virtIp, other.virtIp)) {
            return false;
        }
        if (!Objects.equals(this.commonName, other.commonName)) {
            return false;
        }
        if (!Objects.equals(this.realIp, other.realIp)) {
            return false;
        }
        return true;
    }
    
    

}
