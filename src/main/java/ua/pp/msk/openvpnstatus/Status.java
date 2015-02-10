/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 *
 * @author maskimko
 */
public interface Status {
    public Calendar getUpdateTime();
    public List<Client> getClientList();
    public Set<Route> getRoutes();
}
