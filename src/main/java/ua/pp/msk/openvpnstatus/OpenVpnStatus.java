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
class OpenVpnStatus implements Status{
    private Calendar updatedAt;
    private List<Client> clientList;
    private Set<Route> routeSet;

    @Override
    public Calendar getUpdateTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Client> getClientList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Route> getRoutes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
