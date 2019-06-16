package com.wyob.billingapp.repository;

import com.wyob.billingapp.domain.TransactionItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TransactionItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionItemsRepository extends JpaRepository<TransactionItems, Long>, JpaSpecificationExecutor<TransactionItems> {

}
