/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dexdev.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;


/**
 *
 * @author Moieen
 */
@Entity
@Data

public class Plot extends CommonEntity implements Serializable{
    
    private String name;
    private String dimenstion;
    private String location;
    private String address;
    private String type;
    private String marlas;
    private long purchasepricepermarla;
    private long totalprice;
    private long saleprice;
    
    @OneToMany(mappedBy = "plot")
    private List<Sale> sales;
    
    
}
