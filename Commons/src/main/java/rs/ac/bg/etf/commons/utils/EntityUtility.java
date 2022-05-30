/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.commons.utils;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.transaction.UserTransaction;

/**
 *
 * @author matej
 */
public class EntityUtility {
    
    public static boolean isEntity(EntityManager entityManager, Class<?> clazz) {
        boolean foundEntity = false;
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        for(EntityType<?> entityType : entities) {
            Class<?> entityClass = entityType.getJavaType();
            if(entityClass.equals(clazz)) {
                foundEntity = true;
            }
        }
        
        return foundEntity;
    }
    
    public static <T> void clearTable(UserTransaction userTransaction, EntityManager entityManager, Class<T> entityClass) {
        try {
            userTransaction.begin();

            System.out.println("Delete starting");
            Query query = entityManager.createQuery("DELETE FROM " + entityClass.getSimpleName());
            query.executeUpdate();
            System.out.println("Delete finished");

            userTransaction.commit();
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    
    public static <T> void addEntities(UserTransaction userTransaction, EntityManager entityManager, List<T> entities) {
        try {
            userTransaction.begin();

            System.out.println("Add starting");
            for(T entity : entities) {
                entityManager.persist(entity);
            }
            System.out.println("Add finished");

            userTransaction.commit();
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}