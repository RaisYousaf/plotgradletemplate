/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 *
 * @author Moieen
 */
@Entity
@Data
public class TransactionDetail extends CommonEntity implements Serializable {
 
    private long accountID;
    private long amount;
    @ManyToOne
    private Transaction transaction;
    @ManyToOne
    private Account account;

    
}
