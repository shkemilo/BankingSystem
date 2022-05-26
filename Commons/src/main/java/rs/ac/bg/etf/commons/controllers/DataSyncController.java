/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.commons.controllers;

import java.util.List;
import javax.persistence.EntityManager;
import rs.ac.bg.etf.commons.utils.EntityUtility;

/**
 *
 * @author matej
 */
public class DataSyncController {
    
    private EntityManager entityManager;
    
    public DataSyncController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public <T> void sync(List<T> entities, Class<T> entityClass) {
        if(entities.isEmpty()) {
            return;
        }
        
        if(!EntityUtility.isEntity(entityManager, entityClass)) {
            return;
        }
        
        EntityUtility.clearTable(entityManager, entityClass);
        EntityUtility.addEntities(entityManager, entities);
    }
}
