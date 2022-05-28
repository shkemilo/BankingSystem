/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.commons.utils;

import com.google.gson.Gson;
import java.util.List;
import java.util.Set;
import javax.jms.Queue;
import javax.jms.JMSProducer;
import javax.persistence.metamodel.EntityType;
import rs.ac.bg.etf.commons.controllers.DataSenderController;

/**
 *
 * @author matej
 */
public class ServiceUtils {
    
    static public void sendAllEntitiesToQueue(DataSenderController dataSenderController, JMSProducer producer, Queue destination) {
        Set<EntityType<?>> entities = dataSenderController.getEntityManager().getMetamodel().getEntities();
        for(EntityType<?> entityType : entities) {
            Class<?> entityClass = entityType.getJavaType();
            sendAllToQueue(dataSenderController, producer, destination, entityClass);
        }
    }
    
    static public <T> void sendAllToQueue(DataSenderController dataSenderController, JMSProducer producer, Queue destination, Class<T> type) {
        List<T> entities = dataSenderController.getAll(type);
        Gson gson = new Gson();
        producer.send(destination, gson.toJson(entities));
    }
}
