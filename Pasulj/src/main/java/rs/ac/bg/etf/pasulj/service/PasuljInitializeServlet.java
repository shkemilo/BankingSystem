/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.pasulj.service;

import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author matej
 */
@WebListener
public class PasuljInitializeServlet implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(PasuljInitializeServlet.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Pasulj is starting");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Pasulj is shutting down!");
    }
}
