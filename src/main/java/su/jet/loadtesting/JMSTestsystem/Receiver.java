/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.jet.loadtesting.JMSTestsystem;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.LoggerFactory;


/**
 *
 * @author sy.lysev
 */
public class Receiver implements MessageListener, Closeable{
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(Receiver.class); 
private Session session = null;
private Connection connection = null;
private Destination source = null;
private QueueReceiver queueReceiver;

    public Receiver(Connection conn) {
    try {
        this.connection = conn;
        session = connection.createSession(javax.jms.Session.AUTO_ACKNOWLEDGE);
        source = session.createQueue("input");
        connection.start();
        queueReceiver =  (QueueReceiver) session.createConsumer(source);
        queueReceiver.setMessageListener(this);
    } catch (JMSException ex) {
        Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    

    @Override
    public void onMessage(Message msg) {
    try {
        TextMessage tmsg = (TextMessage) msg;
        System.out.println("message: "+tmsg.getText());
        Thread.sleep(TestSystem.getDelay());
        sendMessage(tmsg.getText(),tmsg.getJMSCorrelationID());
        log.debug("JMSCorrelationId = "+tmsg.getJMSCorrelationID());
        TestSystem.incrementCounter();
        log.debug("CurrentCount "+TestSystem.getCount());
    } catch (JMSException ex) {
        Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
    }   catch (InterruptedException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() throws IOException {
       try{
            queueReceiver.close();
           log.debug("MQQueueReceiver closed successefully");
            session.close();
            log.debug("Session closed successefully");
        } catch (JMSException e){
            log.error("Unable to close reseiver",e);
        }
    }
    private void sendMessage(String msg,String corelId){
    try {
        TextMessage tmsg = session.createTextMessage(msg);
        tmsg.setJMSCorrelationID(corelId);
        tmsg.setStringProperty("MesageType","rs");
        Destination destination = session.createQueue("output");
        MessageProducer msgProducer = session.createProducer(null);
        msgProducer.send(destination, tmsg);
    } catch (JMSException ex) {
        Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

}
