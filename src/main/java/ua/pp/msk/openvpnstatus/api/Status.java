/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.api;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 *
 * @author maskimko
 */
public interface Status {
    public static final String DATE_FORMAT= "EEE MMMM dd HH:mm:ss yyyy";
    public Calendar getUpdateTime();
    public List<Client> getClientList();
    public Set<Route> getRoutes();
}
