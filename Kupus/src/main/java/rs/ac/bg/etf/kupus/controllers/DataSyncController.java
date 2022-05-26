/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.kupus.controllers;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rs.ac.bg.etf.kupus.entities.EntityUtility;

/**
 *
 * @author matej
 */
public class DataSyncController {
    
    EntityManagerFactory emf;
    
    public DataSyncController() {
        emf = Persistence.createEntityManagerFactory("kupusPU");
    }
    
    public <T> void sync(List<T> entities, Class<T> entityClass) {
        if(entities.isEmpty()) {
            return;
        }
        
        EntityManager entityManager = emf.createEntityManager();
        
        if(!EntityUtility.isEntity(entityManager, entityClass)) {
            entityManager.close();
            return;
        }
        
        EntityUtility.clearTable(entityManager, entityClass);
        EntityUtility.addEntities(entityManager, entities);
        
        entityManager.close();
    }
    
    @Override
    protected void finalize() {
        emf.close();
    }
}
