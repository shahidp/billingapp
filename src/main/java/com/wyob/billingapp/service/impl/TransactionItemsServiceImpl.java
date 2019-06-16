package com.wyob.billingapp.service.impl;

import com.wyob.billingapp.service.TransactionItemsService;
import com.wyob.billingapp.domain.TransactionItems;
import com.wyob.billingapp.repository.TransactionItemsRepository;
import com.wyob.billingapp.service.dto.TransactionItemsDTO;
import com.wyob.billingapp.service.mapper.TransactionItemsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TransactionItems}.
 */
@Service
@Transactional
public class TransactionItemsServiceImpl implements TransactionItemsService {

    private final Logger log = LoggerFactory.getLogger(TransactionItemsServiceImpl.class);

    private final TransactionItemsRepository transactionItemsRepository;

    private final TransactionItemsMapper transactionItemsMapper;

    public TransactionItemsServiceImpl(TransactionItemsRepository transactionItemsRepository, TransactionItemsMapper transactionItemsMapper) {
        this.transactionItemsRepository = transactionItemsRepository;
        this.transactionItemsMapper = transactionItemsMapper;
    }

    /**
     * Save a transactionItems.
     *
     * @param transactionItemsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TransactionItemsDTO save(TransactionItemsDTO transactionItemsDTO) {
        log.debug("Request to save TransactionItems : {}", transactionItemsDTO);
        TransactionItems transactionItems = transactionItemsMapper.toEntity(transactionItemsDTO);
        transactionItems = transactionItemsRepository.save(transactionItems);
        return transactionItemsMapper.toDto(transactionItems);
    }

    /**
     * Get all the transactionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionItemsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionItems");
        return transactionItemsRepository.findAll(pageable)
            .map(transactionItemsMapper::toDto);
    }


    /**
     * Get one transactionItems by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionItemsDTO> findOne(Long id) {
        log.debug("Request to get TransactionItems : {}", id);
        return transactionItemsRepository.findById(id)
            .map(transactionItemsMapper::toDto);
    }

    /**
     * Delete the transactionItems by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionItems : {}", id);
        transactionItemsRepository.deleteById(id);
    }
}
