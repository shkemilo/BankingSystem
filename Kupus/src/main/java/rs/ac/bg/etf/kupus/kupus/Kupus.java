/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.kupus.kupus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rs.ac.bg.etf.kupus.entities.Client;

/**
 *
 * @author matej
 */
public class Kupus {
    public static void main(String[] args) {
        System.out.println("Kupus starting!");
        
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kupusPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        entityManager.getTransaction().begin();
        
        Client client = new Client("Marko Markovic", "LOL 123", null);
        entityManager.persist(client);
        
        entityManager.getTransaction().commit();
        
        entityManager.close();
        entityManagerFactory.close();
        
        System.out.println("Kupus ending!");
    }
}
