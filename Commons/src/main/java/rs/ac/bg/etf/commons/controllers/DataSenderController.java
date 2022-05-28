/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.commons.controllers;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import rs.ac.bg.etf.commons.utils.EntityUtility;

/**
 *
 * @author matejs
 */
public class DataSenderController {
    
    private EntityManager entityManager;
    
    public DataSenderController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public <T> List<T> getAll(Class<T> entityClass) {       
        if(!EntityUtility.isEntity(entityManager, entityClass)) {
            entityManager.close();
            return null;
        }
        
        TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName(), entityClass);
        List<T> entities = query.getResultList();
        
        entityManager.close();
        
        return entities;
    }
}
