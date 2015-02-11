/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.api;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import ua.pp.msk.openvpnstatus.exceptions.OpenVpnParseException;
import ua.pp.msk.openvpnstatus.implementation.OpenVpnStatus;

/**
 *
 * @author maskimko
 */
public class StatusTest {

    private static Status instance;

    public StatusTest() {
    }

    @BeforeClass
    public static  void init() throws OpenVpnParseException {
        String input = "INFO:OpenVPN Management Interface Version 1 -- type 'help' for more info\n"
                + "status\n"
                + "OpenVPN CLIENT LIST\n"
                + "Updated,Wed Feb 11 00:07:24 2015\n"
                + "Common Name,Real Address,Bytes Received,Bytes Sent,Connected Since\n"
                + "home-workstation,172.19.2.19:40749,19281,20010,Tue Feb 10 23:30:45 2015\n"
                + "julia-laptop,172.19.2.2:46010,19475,20454,Tue Feb 10 23:30:45 2015\n"
                + "cloud.msk.pp.ua,194.14.179.127:59113,19192,19924,Tue Feb 10 23:30:46 2015\n"
                + "ROUTING TABLE\n"
                + "Virtual Address,Common Name,Real Address,Last Ref\n"
                + "172.19.17.18,home-workstation,172.19.2.19:40749,Tue Feb 10 23:30:46 2015\n"
                + "172.19.17.26,cloud.msk.pp.ua,194.14.179.127:59113,Tue Feb 10 23:30:52 2015\n"
                + "172.19.17.6,julia-laptop,172.19.2.2:46010,Tue Feb 10 23:30:47 2015\n"
                + "GLOBAL STATS\n"
                + "Max bcast/mcast queue length,0\n"
                + "END";
        instance = new OpenVpnStatus();
        ((OpenVpnStatus) instance).setCommandOutput(input);

    }
    
    @AfterClass
    public static void conclude(){
        System.out.println(instance.toString());
    }

    /**
     * Test of getUpdateTime method, of class Status.
     */
    @Test
    public void testGetUpdateTime() {
        System.out.println("getUpdateTime");
        Calendar expResult = null;
        Calendar result = instance.getUpdateTime();
        assertNotNull(result);
        assertNotNull(result);

    }

    /**
     * Test of getClientList method, of class Status.
     */
    @Test
    public void testGetClientList() {
        System.out.println("getClientList");
        List<Client> expResult = null;
        List<Client> result = instance.getClientList();
        assertNotNull(result);
        assertTrue(result.size() == 3);
    }

    /**
     * Test of getRoutes method, of class Status.
     */
    @Test
    public void testGetRoutes() {
        System.out.println("getRoutes");
        Set<Route> expResult = null;
        Set<Route> result = instance.getRoutes();
        assertNotNull(result);
        assertTrue(result.size() == 3);
    }

}
