/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.pasulj.controllers;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import rs.ac.bg.etf.pasulj.entities.EntityUtility;

/**
 *
 * @author matej
 */
public class DataSenderController {
    EntityManagerFactory emf;
    
    public DataSenderController() {
        emf = Persistence.createEntityManagerFactory("kupusPU");
    }
    
    public <T> List<T> getAll(Class<T> entityClass) {   
        EntityManager entityManager = emf.createEntityManager();
        
        if(!EntityUtility.isEntity(entityManager, entityClass)) {
            entityManager.close();
            return null;
        }
        
        TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName(), entityClass);
        List<T> entities = query.getResultList();
        
        entityManager.close();
        
        return entities;
    }
    
    @Override
    protected void finalize() {
        emf.close();
    }
}
