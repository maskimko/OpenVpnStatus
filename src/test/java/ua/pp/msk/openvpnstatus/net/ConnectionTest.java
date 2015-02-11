/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.openvpnstatus.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ua.pp.msk.openvpnstatus.api.Client;
import ua.pp.msk.openvpnstatus.api.Route;
import ua.pp.msk.openvpnstatus.api.Status;

/**
 *
 * @author maskimko
 */
public class ConnectionTest {
    
    public static Connection connection;
    
    public ConnectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws UnknownHostException {
        ResourceBundle bundle = ResourceBundle.getBundle("test");
        String server = bundle.getString("openvpn.server");
        int port = Integer.parseInt(bundle.getString("management.port"));
        InetAddress addr = Inet4Address.getByName(server);
        connection = ManagementConnection.getConnection(addr, port);
        
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        connection.close();
    }

    /**
     * Test of connect method, of class Connection.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        connection.connect();
        assertTrue(connection.isConnected());
    }
    
    
    /**
     * Test of executeCommand method, of class Connection.
     */
    @Test
    public void testExecuteCommand() throws Exception {
        System.out.println("executeCommand");
        String command = "help";
        if (!connection.isConnected()) {
            connection.connect();
        }
        String result = connection.executeCommand(command);
        assertNotNull(result);
        String[] lines  = result.split("\n");
        assertTrue(lines.length > 1);
        assertEquals(lines[lines.length -1 ], "END");
    }

    /**
     * Test of getStatus method, of class Connection.
     */
    @Test
    public void testGetStatus() throws Exception {
      System.out.println("getStatus");
        if (!connection.isConnected()) {
            connection.connect();
        }
        Status result = connection.getStatus();
        assertNotNull(result);
        assertNotNull(result.getUpdateTime());
        List<Client> clientList = result.getClientList();
        assertNotNull(clientList);
        Set<Route> routes = result.getRoutes();
        assertNotNull(routes);
        System.out.println(result);
    }

  


   
    
}
