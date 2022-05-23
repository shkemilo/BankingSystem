/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.boranija.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author matej
 */
@Entity(name = "DepositTransaction")
@Table(name = "deposit_transcation")
public class DepositTransaction extends Transaction {
    
    private long branchId;

    public DepositTransaction(Account account, long sum, long branchId) {
        super(account, sum);
        this.branchId = branchId;
    }
    
    public DepositTransaction() { }
    
    public long getBranchId() {
        return branchId;
    }
    
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }
}
