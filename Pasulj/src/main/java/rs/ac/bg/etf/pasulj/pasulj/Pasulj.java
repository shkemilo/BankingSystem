/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.pasulj.pasulj;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rs.ac.bg.etf.pasulj.entities.Location;

/**
 *
 * @author matej
 */
public class Pasulj {
    
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pasuljPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        entityManager.getTransaction().begin();
        
        Location location = new Location("Kragujevac", 34000);
        entityManager.persist(location);
        
        entityManager.getTransaction().commit();
        
        entityManager.close();
        entityManagerFactory.close();
   }
}
