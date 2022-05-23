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
@Entity(name = "WithdrawalTransaction")
@Table(name = "withdrawal_transaction")
public class WithdrawalTransaction extends Transaction {
    
    private long branchId;

    public WithdrawalTransaction(Account account, long sum, long branchId) {
        super(account, sum);
        this.branchId = branchId;
    }
    
    public WithdrawalTransaction() { }
    
    public long getBranchId() {
        return branchId;
    }
    
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }
}
