/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.jet.loadtesting.JMSTestsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.LoggerFactory;


/**
 *
 * @author sergey
 */
public class TestSystem implements ServletContextListener{
private final static org.slf4j.Logger log = LoggerFactory.getLogger(TestSystem.class); 
private static String serverUrl = "tcp://localhost:7222";
private static String userName = "admin";
private static String password = "";
private static List<Receiver> recievers = new ArrayList<>();
private static Connection connection = null;
private Receiver receiver;
private static int delay = 1000;
private static int count = 0;
   @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("Create connection");
    try {
        ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(
                serverUrl);
        connection = factory.createConnection(userName, password);
        receiver = new Receiver(connection);
        recievers.add(receiver);
    } catch (JMSException ex) {
        log.error("JMS Exception ");
        ex.printStackTrace();
    }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("context destroy");
    
    try {
        receiver.close();
    } catch (IOException ex) {
        log.error("Close JMS connection exception "+ex.getLocalizedMessage());
    }
    
}
    public static int getDelay(){
        return delay;
    }

    public static void setDelay(int delay) {
        TestSystem.delay = delay;
    }
    public static int getThreadCount(){
        return recievers.size();
    }
    public static void incrementThreads(){
        Receiver receiver = new Receiver(connection);
        recievers.add(receiver);
    }
    public static void decrementThreads(){
    try {
        recievers.get(0).close();
        recievers.remove(0);
    } catch (IOException ex) {
        Logger.getLogger(TestSystem.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    public static synchronized void incrementCounter(){
        count++;
    }
    public static int getCount(){
        return count;
    }
    public static void resetCount(){
        count = 0;
    }
}
