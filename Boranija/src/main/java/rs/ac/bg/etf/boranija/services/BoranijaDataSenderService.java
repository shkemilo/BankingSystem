/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.boranija.services;

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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import rs.ac.bg.etf.boranija.entities.Account;
import rs.ac.bg.etf.boranija.entities.Client;
import rs.ac.bg.etf.boranija.entities.DepositTransaction;
import rs.ac.bg.etf.boranija.entities.InternalTransaction;
import rs.ac.bg.etf.boranija.entities.Transaction;
import rs.ac.bg.etf.boranija.entities.WithdrawalTransaction;
import rs.ac.bg.etf.commons.controllers.DataSenderController;
import rs.ac.bg.etf.commons.operations.SyncOperation;
import rs.ac.bg.etf.commons.utils.ServiceUtils;

/**
 *
 * @author matej
 */
@WebListener
public class BoranijaDataSenderService implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(BoranijaDataSenderService.class.getName());
    
    @Resource(lookup = "ConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @PersistenceContext(unitName = "boranijaPU")
    private EntityManager entityManager;
        
    @Resource(lookup = "BoranijaSyncRequestQueue")
    private Queue requestQueue;
    
    @Resource(lookup = "KupusDataSyncQueue")
    private Queue dataQueue;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Thread serviceThread = new Thread(() -> startService());
        serviceThread.start();
    }

    private void startService() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(requestQueue);
        
        DataSenderController dataSenderController = new DataSenderController(entityManager);
        
        while(true) {
            try {
                logger.info("Boranija DataSender accepting requests");
                TextMessage request = (TextMessage)consumer.receive();
                
                String requestName = request.getText();
                logger.info("Boranija DataSender got request: " + requestName);
                if(SyncOperation.ACCOUNT.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, Account.class);
                } else if(SyncOperation.CLIENT.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, Client.class);
                } else if(SyncOperation.TRANSACTION.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, Transaction.class);
                } else if(SyncOperation.DEPOSIT_TRANSACTION.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, DepositTransaction.class);
                } else if(SyncOperation.WITHDRAWAL_TRANSACTION.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, WithdrawalTransaction.class);
                } else if(SyncOperation.INTERNAL_TRANSACTION.getName().equals(requestName)) {
                    ServiceUtils.sendAllToQueue(dataSenderController, producer, dataQueue, InternalTransaction.class);
                } else {
                    logger.log(Level.WARNING, "Unsupported operation {0}", requestName);
                }
                logger.info("Boranija DataSender request finished");
            } catch (JMSException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
