/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.kupus.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author matej
 */
@WebListener
public class KupusInitializeServlet implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(KupusInitializeServlet.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Kupus is starting");
        Runnable syncTask = () -> {
            logger.info("Sync statring");
        };
        
        ScheduledExecutorService periodicSyncExecutor = Executors.newScheduledThreadPool(1);
        periodicSyncExecutor.scheduleAtFixedRate(syncTask, 0, 2, TimeUnit.MINUTES);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Kupus is shutting down!");
    }
}
