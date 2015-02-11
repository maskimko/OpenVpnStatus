/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.implementation;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import ua.pp.msk.openvpnstatus.api.Client;
import ua.pp.msk.openvpnstatus.api.Route;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;

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
public class OpenVpnStatus implements Status, Serializable {
    private Calendar updatedAt;
    private List<Client> clientList;
    private Set<Route> routeSet;

    @Override
    public Calendar getUpdateTime() {
       return updatedAt;
    }

    @Override
    public List<Client> getClientList() {
        return clientList;
    }

    @Override
    public Set<Route> getRoutes() {
        return routeSet;
    }
    
    public void setCommandOutput(String output) throws OpenVpnParseException{
        String[] lines = output.split("\n");
        Pattern clientsHeader = Pattern.compile("^OpenVPN CLIENT LIST");
        Pattern updated = Pattern.compile("^Updated,.*");
        Pattern clientColumns = Pattern.compile("Common Name,Real Address,Bytes Received,Bytes Sent,Connected Since");
        Pattern routesHeader = Pattern.compile("^ROUTING TABLE");
        Pattern routesColumns = Pattern.compile("Virtual Address,Common Name,Real Address,Last Ref");
        Pattern globalStats = Pattern.compile("GLOBAL STATS");
        for (int i = 0; i < lines.length; i++) {
            if (clientsHeader.matcher(lines[i]).matches()){
                if (updated.matcher(lines[++i]).matches()) {
                    try {
                        updatedAt = parseUpdatedTime(lines[i++]);
                    } catch (OpenVpnParseException ex) {
                        //use slf4j
                        Logger.getLogger(OpenVpnStatus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                     throw new OpenVpnParseException("Cannot parse OpenVPN status. Wrong lines sequence.");
                }
                if (clientColumns.matcher(lines[i++]).matches()){
                    while(!routesHeader.matcher(lines[i]).matches()){
                        addClient(lines[i++]);
                    }
                } else {
                    throw new OpenVpnParseException("Cannot parse OpenVPN status. Wrong lines sequence.");
                }
            }
            if (routesHeader.matcher(lines[i]).matches()){
                i++;
                 if (routesColumns.matcher(lines[i]).matches()){
                     i++;
                    while(!globalStats.matcher(lines[i]).matches()){
                        addRoute(lines[i++]);
                    }
                    break;
                } else  {
                     throw new OpenVpnParseException("Cannot parse OpenVPN status. Wrong lines sequence.");
                 }
            }
        }
    }
    
    private Calendar parseUpdatedTime(String updatedString) throws OpenVpnParseException{
       Calendar ut = null;
        try {
            String[] components = updatedString.split(",");
            if (components.length != 2) {
                throw new OpenVpnParseException("Cannot parse update time string. There should be 2 components separated by comma");
            }
            SimpleDateFormat sdf = new SimpleDateFormat(Status.DATE_FORMAT);
           Date parsedDate = sdf.parse(components[1]);
           Calendar c = Calendar.getInstance();
           c.setTime(parsedDate);
           ut = c;
        } catch (ParseException ex) {
            //Use slf4j
            Logger.getLogger(OpenVpnStatus.class.getName()).log(Level.SEVERE, null, ex);
            throw new OpenVpnParseException("Cannot parse update time string");
        }
        return ut;
    }
    
    private void addClient(String clientString){
        if (clientList == null) {
            clientList = new ArrayList<>();
        }
        try {
            OpenVpnClient ovc = new OpenVpnClient();
            ovc.setRawString(clientString);
            clientList.add(ovc);
        } catch (OpenVpnParseException ex) {
            //try to use slf4j or logback
            Logger.getLogger(OpenVpnStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addRoute(String routeString){
         if (routeSet == null) {
            routeSet = new HashSet<>();
        }
        try {
           OpenVpnRoute ovr = new OpenVpnRoute();
            ovr.setRawString(routeString);
            routeSet.add(ovr);
        } catch (OpenVpnParseException ex) {
            //try to use slf4j or logback
            Logger.getLogger(OpenVpnStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     @Override
    public String toString() {
        DateFormat df = DateFormat.getDateTimeInstance();
        StringBuilder sb = new StringBuilder("Updated:\t").append(df.format(getUpdateTime().getTime()))
                .append("\n").append("Client List:\n");
        for (Client c: getClientList()){
            sb.append("\t").append(c.toString()).append("\n");
        }
        sb.append("Routes list:\n");
        for (Route r : getRoutes()) {
            sb.append("\t").append(r.toString()).append("\n");
        }
        return sb.toString();
    }
}
