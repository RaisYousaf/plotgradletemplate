/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * @author Moieen
 */
@Entity
@Data
public class Transaction extends CommonEntity implements Serializable {
    
    private String title;
    
    @ManyToOne
    private Sale sale;
    
    @OneToMany(mappedBy = "transaction")
    private List<TransactionDetail> taransactionDetails;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

}
