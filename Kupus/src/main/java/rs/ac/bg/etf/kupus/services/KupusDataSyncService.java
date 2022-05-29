/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.kupus.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
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
import rs.ac.bg.etf.commons.controllers.DataSyncController;
import rs.ac.bg.etf.commons.operations.SyncOperation;
import rs.ac.bg.etf.kupus.entities.Account;
import rs.ac.bg.etf.kupus.entities.Branch;
import rs.ac.bg.etf.kupus.entities.DepositTransaction;
import rs.ac.bg.etf.kupus.entities.InternalTransaction;
import rs.ac.bg.etf.kupus.entities.Location;
import rs.ac.bg.etf.kupus.entities.Transaction;
import rs.ac.bg.etf.kupus.entities.WithdrawalTransaction;

/**
 *
 * @author matej
 */
public class KupusDataSyncService implements Runnable {
    
    private static final Logger logger = Logger.getLogger(KupusDataSyncService.class.getName());
    
    private static final Pair<SyncOperation, Class<?>>[] pasuljOperationsDefinition = {
        new Pair<>(SyncOperation.BRANCH, Branch.class),
        new Pair<>(SyncOperation.LOCATION, Location.class)
    };
    
    private static final Pair<SyncOperation, Class<?>>[] boranijaOperationsDefinition = {
        new Pair<>(SyncOperation.ACCOUNT, Account.class),
        new Pair<>(SyncOperation.TRANSACTION, Transaction.class),
        new Pair<>(SyncOperation.INTERNAL_TRANSACTION, InternalTransaction.class),
        new Pair<>(SyncOperation.DEPOSIT_TRANSACTION, DepositTransaction.class),
        new Pair<>(SyncOperation.WITHDRAWAL_TRANSACTION, WithdrawalTransaction.class)
    };
            
    @Resource(lookup = "ConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @PersistenceContext(name = "kupusPU")
    private EntityManager entityManager;
        
    @Resource(lookup = "PasuljSyncRequestQueue")
    private Queue pasuljSyncRequestQueue;
    
    @Resource(lookup = "BoranijaSyncRequestQueue")
    private Queue boranijaSyncRequestQueue;
    
    @Resource(lookup = "KupusDataSyncQueue")
    private Queue dataQueue;

    @Override
    public void run() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(dataQueue);
        
        executeOperations(consumer, producer, pasuljSyncRequestQueue, pasuljOperationsDefinition);
        executeOperations(consumer, producer, boranijaSyncRequestQueue, boranijaOperationsDefinition);
        
        // Client not synced yet here
    }
    
    private void executeOperations(JMSConsumer consumer, JMSProducer producer, Queue destination, Pair<SyncOperation, Class<?>>[] operationsDefinition) {
        DataSyncController dataSyncController = new DataSyncController(entityManager);
        Gson gson = new Gson();
        
        try {
            for(Pair<SyncOperation, Class<?>> operationDefinition : operationsDefinition) {
                producer.send(destination, operationDefinition.getKey().getName());
                String json = ((TextMessage) consumer.receive()).getText();
                
                processData(dataSyncController, gson, json, operationDefinition.getValue());
            }
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null ,ex);
        }
    }
    
    private <T> void processData(DataSyncController dataSyncController, Gson gson, String json, Class<T> type) {
        List<T> entityList = gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
        dataSyncController.sync(entityList, type);
    } 
}
