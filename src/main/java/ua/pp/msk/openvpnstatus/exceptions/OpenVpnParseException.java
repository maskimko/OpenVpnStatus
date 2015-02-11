/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.exceptions;

/**
 *
 * @author maskimko
 */
public class OpenVpnParseException extends Exception{

    public OpenVpnParseException() {
    }

    public OpenVpnParseException(String string) {
        super(string);
    }

    public OpenVpnParseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
