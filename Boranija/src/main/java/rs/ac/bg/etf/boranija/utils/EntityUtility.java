/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.boranija.utils;

import java.util.Set;
import javax.persistence.EntityManager;
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
}
