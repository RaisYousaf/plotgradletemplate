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
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * @author Moieen
 */
@Entity
@Data
public class Plot extends CommonEntity implements Serializable{
    
    private String name;
    private String dimension;
    private String location;
    private String address;
    private String type;
    private String marlas;
    private double purchasePricePerMarla;
    private double totalprice;
    private double saleprice;
   
    
}
