/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Moieen
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Customer extends CommonEntity implements Serializable{
    
    private String fullName;
    private String fatherName;
    private long cnic;
    private String currentAddress;
    private String permanentAddress;
    private long phoneNo;
    private String email;
    private long accountNo;
    private byte[] cnicImage;
    
    
    @OneToOne(mappedBy = "customer")
    private Account account;
    
    @OneToMany(mappedBy = "customer")
    private List<Sale> sales;

    
}
