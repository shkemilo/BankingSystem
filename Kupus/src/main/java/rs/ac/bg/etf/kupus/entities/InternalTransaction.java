/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.kupus.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author matej
 */
@Entity(name = "InternalTransaction")
@Table(name = "internal_transaction")
public class InternalTransaction extends Transaction {
    
}
