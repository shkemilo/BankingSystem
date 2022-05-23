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
@Entity(name = "WithdrawalTransaction")
@Table(name = "withdrawal_transaction")
public class WithdrawalTransaction extends Transaction {
    
    private Branch branch;

    public WithdrawalTransaction(Account account, long sum, Branch branch) {
        super(account, sum);
        this.branch = branch;
    }
    
    public WithdrawalTransaction() { }
    
    public Branch getBranchId() {
        return branch;
    }
    
    public void setBranchId(Branch branch) {
        this.branch = branch;
    }
}
