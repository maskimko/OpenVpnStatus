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


/*
INFO:OpenVPN Management Interface Version 1 -- type 'help' for more info
status
OpenVPN CLIENT LIST
Updated,Wed Feb 11 00:07:24 2015
Common Name,Real Address,Bytes Received,Bytes Sent,Connected Since
home-workstation,172.19.2.19:40749,19281,20010,Tue Feb 10 23:30:45 2015
julia-laptop,172.19.2.2:46010,19475,20454,Tue Feb 10 23:30:45 2015
cloud.msk.pp.ua,194.14.179.127:59113,19192,19924,Tue Feb 10 23:30:46 2015
ROUTING TABLE
Virtual Address,Common Name,Real Address,Last Ref
172.19.17.18,home-workstation,172.19.2.19:40749,Tue Feb 10 23:30:46 2015
172.19.17.26,cloud.msk.pp.ua,194.14.179.127:59113,Tue Feb 10 23:30:52 2015
172.19.17.6,julia-laptop,172.19.2.2:46010,Tue Feb 10 23:30:47 2015
GLOBAL STATS
Max bcast/mcast queue length,0
END

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
