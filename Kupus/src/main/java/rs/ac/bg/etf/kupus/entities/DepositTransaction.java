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
@Entity(name = "DepositTransaction")
@Table(name = "deposit_transcation")
public class DepositTransaction extends Transaction {
    
    private Branch branch;

    public DepositTransaction(Account account, long sum, Branch branch) {
        super(account, sum);
        this.branch = branch;
    }
    
    public DepositTransaction() { }
    
    public Branch getBranchId() {
        return branch;
    }
    
    public void setBranchId(Branch branch) {
        this.branch = branch;
    }
}
