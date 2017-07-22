package com.basakpie.models;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ali
 */

@Transactional(readOnly = true)
public interface SaleRepository extends JpaRepository<Sale, Long>{
//    List<Sale> findByNameIgnoreCaseContaining(String name);
//    List<Customer> findByFullName();
}
