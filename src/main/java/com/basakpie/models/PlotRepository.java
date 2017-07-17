/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basakpie.models;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Moieen
 */
@Transactional(readOnly = true)
public interface PlotRepository extends JpaRepository<Plot, Long> {
    
    List<Plot> findByNameIgnoreCaseContaining(String name);
}
