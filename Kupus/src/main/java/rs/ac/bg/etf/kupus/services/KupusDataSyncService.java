/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.kupus.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import javax.transaction.UserTransaction;
import rs.ac.bg.etf.commons.controllers.DataSyncController;
import rs.ac.bg.etf.commons.operations.SyncOperation;
import rs.ac.bg.etf.kupus.entities.Account;
import rs.ac.bg.etf.kupus.entities.Branch;
import rs.ac.bg.etf.kupus.entities.Client;
import rs.ac.bg.etf.kupus.entities.DepositTransaction;
import rs.ac.bg.etf.kupus.entities.InternalTransaction;
import rs.ac.bg.etf.kupus.entities.Location;
import rs.ac.bg.etf.kupus.entities.Transaction;
import rs.ac.bg.etf.kupus.entities.WithdrawalTransaction;

/**
 *
 * @author matej
 */
@WebListener
public class KupusDataSyncService implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(KupusDataSyncService.class.getName());
    
    private static final OperationDefinition[] pasuljOperationsDefinition = {
        new OperationDefinition(SyncOperation.BRANCH, Branch.class),
        new OperationDefinition(SyncOperation.LOCATION, Location.class)
    };
    
    private static final OperationDefinition[] boranijaOperationsDefinition = {
        new OperationDefinition(SyncOperation.CLIENT, Client.class),
        new OperationDefinition(SyncOperation.ACCOUNT, Account.class),
        new OperationDefinition(SyncOperation.TRANSACTION, Transaction.class),
        new OperationDefinition(SyncOperation.INTERNAL_TRANSACTION, InternalTransaction.class),
        new OperationDefinition(SyncOperation.DEPOSIT_TRANSACTION, DepositTransaction.class),
        new OperationDefinition(SyncOperation.WITHDRAWAL_TRANSACTION, WithdrawalTransaction.class)
    };
    
    static private class OperationDefinition {
        
        public OperationDefinition(SyncOperation operation, Class type) {
            this.operation = operation;
            this.type = type;
        }
        
        public final SyncOperation operation;
        public final Class type;
    }
            
    @Resource(lookup = "ConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @PersistenceContext(unitName = "kupusPU")
    private EntityManager entityManager;
    
    @Resource
    private UserTransaction userTransaction;
        
    @Resource(lookup = "PasuljSyncRequestQueue")
    private Queue pasuljSyncRequestQueue;
    
    @Resource(lookup = "BoranijaSyncRequestQueue")
    private Queue boranijaSyncRequestQueue;
    
    @Resource(lookup = "KupusDataSyncQueue")
    private Queue dataQueue;
    
    private DataSyncController dataSyncController;
    
    private Gson gson;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ScheduledExecutorService periodicSyncExecutor = Executors.newScheduledThreadPool(1);
        periodicSyncExecutor.scheduleAtFixedRate(() -> startService(), 0, 2, TimeUnit.MINUTES);
    }

    private void startService() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(dataQueue);
        
        dataSyncController = new DataSyncController(entityManager);
        gson = new Gson();
        
        logger.info("Kupus sync starting");
        
        logger.info("Start sending data sync requests to boranija: ");
        executeOperations(consumer, producer, boranijaSyncRequestQueue, boranijaOperationsDefinition);
        
        logger.info("Start sending data sync requests to pasulj: ");
        executeOperations(consumer, producer, pasuljSyncRequestQueue, pasuljOperationsDefinition);
        
        logger.info("Kupus sync finished");
    }
    
    private <T> void executeOperations(JMSConsumer consumer, JMSProducer producer, Queue destination, OperationDefinition[] operationDefinitions) {      
        try {
            for(OperationDefinition operationDefinition : operationDefinitions) {
                logger.info("Sending request: " + operationDefinition.operation.getName());
                producer.send(destination, operationDefinition.operation.getName());
                String json = ((TextMessage) consumer.receive()).getText();
                logger.info("Data recieved:\n" + json);
                
                processData(json, operationDefinition.type);
            }
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null ,ex);
        }
    }
    
    private <T> void processData(String json, Class<T> type) {
        Type typeToken = TypeToken.getParameterized(List.class, type).getType();
        List<T> entityList = gson.fromJson(json, typeToken);
        logger.info("Data processed successfully. Entities to update: " + entityList.size());
        dataSyncController.sync(userTransaction, entityList, type);
    } 
}
