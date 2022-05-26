/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.boranija;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rs.ac.bg.etf.boranija.entities.Client;

/**
 *
 * @author matej
 */
public class Boranija {
    public static void main(String[] args) {
        System.out.println("Boranija starting!");
        
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("boranijaPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        entityManager.getTransaction().begin();
        
        Client client = new Client("Marko Markovic", "LOL 123", 0);
        entityManager.persist(client);
        
        entityManager.getTransaction().commit();
        
        entityManager.close();
        entityManagerFactory.close();
        
        System.out.println("Boranija ending!");
    }
}
