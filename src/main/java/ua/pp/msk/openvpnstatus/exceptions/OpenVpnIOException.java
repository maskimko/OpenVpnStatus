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
public class OpenVpnIOException extends Exception{

    public OpenVpnIOException() {
    }

    public OpenVpnIOException(String message) {
        super(message);
    }

    public OpenVpnIOException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
