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

import java.util.regex.Pattern;
import org.slf4j.LoggerFactory;
import ua.pp.msk.openvpnstatus.api.Client;
import ua.pp.msk.openvpnstatus.api.Route;
import ua.pp.msk.openvpnstatus.api.Status;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;

/**
 *
 * @author maskimko
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
        LoggerFactory.getLogger(this.getClass()).debug("Parsing: \n" + output);
        String[] lines = output.split("\n");
        setCommandOutput(lines);
    }
    
    public void setCommandOutput(List<String> output) throws OpenVpnParseException {
        setCommandOutput(output.toArray(new String[0]));
    }
    public void setCommandOutput(String[] lines) throws OpenVpnParseException{
        
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
                        LoggerFactory.getLogger(OpenVpnStatus.class.getName()).error("Cannot parse update date", ex);
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
        LoggerFactory.getLogger(this.getClass()).debug("Successfully parsed \n" + this.toString());
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
            LoggerFactory.getLogger(OpenVpnStatus.class.getName()).error("Cannot parse update time string", ex);
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
            LoggerFactory.getLogger(OpenVpnStatus.class.getName()).error("Cannot add the client", ex);
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
            LoggerFactory.getLogger(OpenVpnStatus.class.getName()).error("Cannot add route", ex);
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
