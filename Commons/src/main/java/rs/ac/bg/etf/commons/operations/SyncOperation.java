/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.commons.operations;

/**
 *
 * @author matej
 */
public enum SyncOperation {
    ACCOUNT("syncAccount"),
    CLIENT("syncClient"),
    BRANCH("syncBranch"),
    LOCATION("syncLocation"),
    TRANSACTION("syncTransaction"),
    DEPOSIT_TRANSACTION("syncDepositTransaction"),
    WITHDRAWAL_TRANSACTION("syncWithdrawalTransaction"),
    INTERNAL_TRANSACTION("syncInternalTransaction");
    
    private final String name;

    private SyncOperation(String name) {
        this.name = name;
    }
    
    public final String getName() {
        return name;
    }
}
