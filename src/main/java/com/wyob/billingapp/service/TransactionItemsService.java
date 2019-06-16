package com.wyob.billingapp.service;

import com.wyob.billingapp.service.dto.TransactionItemsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.wyob.billingapp.domain.TransactionItems}.
 */
public interface TransactionItemsService {

    /**
     * Save a transactionItems.
     *
     * @param transactionItemsDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionItemsDTO save(TransactionItemsDTO transactionItemsDTO);

    /**
     * Get all the transactionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionItemsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" transactionItems.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionItemsDTO> findOne(Long id);

    /**
     * Delete the "id" transactionItems.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
