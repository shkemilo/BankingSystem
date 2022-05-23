/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.etf.boranija.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author matej
 */
@Entity(name = "Account")
@Table(name = "account")
public class Account {
    
    public enum Status {
        ACTIVE,
        BLOCKED
    };
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private long allowedMinus;
    
    private long balance;
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    
    private LocalDateTime created = LocalDateTime.now();
    
    private long transcationCount = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Client owner;
    
    @OneToMany(mappedBy = "account",
               orphanRemoval = true,
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Account(Client owner, long allowedMinus) {
        this.owner = owner;
        this.allowedMinus = allowedMinus;
    }
    
    public Account() { }

    public Long getId() {
        return id;
    }

    public long getAllowedMinus() {
        return allowedMinus;
    }

    public void setAllowedMinus(long allowedMinus) {
        this.allowedMinus = allowedMinus;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public long getTranscationCount() {
        return transcationCount;
    }

    public void setTranscationCount(long transcationCount) {
        this.transcationCount = transcationCount;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
