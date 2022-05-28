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
    
    public static <T> void clearTable(EntityManager entityManager, Class<T> entityClass) {
        entityManager.getTransaction().begin();
        
        Query query = entityManager.createQuery("DELETE FROM " + entityClass.getSimpleName());
        query.executeUpdate();
        
        entityManager.getTransaction().commit();
    }
    
    public static <T> void addEntities(EntityManager entityManager, List<T> entities) {
        entityManager.getTransaction().begin();
        
        for(T entity : entities) {
            entityManager.persist(entity);
        }
        
        entityManager.getTransaction().commit();
    }
}