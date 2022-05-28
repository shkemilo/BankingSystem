/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.pasulj.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rs.ac.bg.etf.commons.controllers.DataSenderController;
import rs.ac.bg.etf.commons.operations.SyncOperation;
import rs.ac.bg.etf.commons.utils.ServiceUtils;
import rs.ac.bg.etf.pasulj.entities.Branch;
import rs.ac.bg.etf.pasulj.entities.Client;
import rs.ac.bg.etf.pasulj.entities.Location;

/**
 *
 * @author matej
 */
public class PasuljDataSenderService implements Runnable {
    
    private static final Logger logger = Logger.getLogger(PasuljDataSenderService.class.getName());
    
    @Resource(lookup = "ConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @PersistenceContext(name = "pasuljPU")
    private EntityManager entityManager;
        
    @Resource(lookup = "PasuljSyncRequestQueue")
    private Queue requestQueue;
    
    @Resource(lookup = "KupusDataSyncQueue")
    private Queue dataQueue;

    @Override
    public void run() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(requestQueue);
        
        DataSenderController dataSenderController = new DataSenderController(entityManager);
        
        while(true) {
            try {
                TextMessage request = (TextMessage)consumer.receive();
                
                String requestName = request.getText();
                if(SyncOperation.BRANCH.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, Branch.class);
                } else if (SyncOperation.CLIENT.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, Client.class);
                } else if (SyncOperation.LOCATION.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, Location.class);
                } else {
                    logger.log(Level.WARNING, "Unsupported operation {0}", requestName);
                }
            } catch (JMSException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
