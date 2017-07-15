/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dexdev.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author Moieen
 */
@Entity
@Data
public class Sale extends CommonEntity implements Serializable {
    @ManyToOne
    private Customer customer;
    
    @ManyToOne
    private Plot plot;
    private long salePricePerMarla;
    private int numberOfInstallment;
    private long advance;
    private long tokenMoney;
    private long amountPerInstallment;
    
    @OneToMany(mappedBy = "sale")
    private List<Transaction> transactions;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

}
