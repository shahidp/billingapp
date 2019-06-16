package com.wyob.billingapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.wyob.billingapp.domain.TransactionItems;
import com.wyob.billingapp.domain.*; // for static metamodels
import com.wyob.billingapp.repository.TransactionItemsRepository;
import com.wyob.billingapp.service.dto.TransactionItemsCriteria;
import com.wyob.billingapp.service.dto.TransactionItemsDTO;
import com.wyob.billingapp.service.mapper.TransactionItemsMapper;

/**
 * Service for executing complex queries for {@link TransactionItems} entities in the database.
 * The main input is a {@link TransactionItemsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionItemsDTO} or a {@link Page} of {@link TransactionItemsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionItemsQueryService extends QueryService<TransactionItems> {

    private final Logger log = LoggerFactory.getLogger(TransactionItemsQueryService.class);

    private final TransactionItemsRepository transactionItemsRepository;

    private final TransactionItemsMapper transactionItemsMapper;

    public TransactionItemsQueryService(TransactionItemsRepository transactionItemsRepository, TransactionItemsMapper transactionItemsMapper) {
        this.transactionItemsRepository = transactionItemsRepository;
        this.transactionItemsMapper = transactionItemsMapper;
    }

    /**
     * Return a {@link List} of {@link TransactionItemsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionItemsDTO> findByCriteria(TransactionItemsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionItems> specification = createSpecification(criteria);
        return transactionItemsMapper.toDto(transactionItemsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionItemsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionItemsDTO> findByCriteria(TransactionItemsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionItems> specification = createSpecification(criteria);
        return transactionItemsRepository.findAll(specification, page)
            .map(transactionItemsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionItemsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionItems> specification = createSpecification(criteria);
        return transactionItemsRepository.count(specification);
    }

    /**
     * Function to convert TransactionItemsCriteria to a {@link Specification}.
     */
    private Specification<TransactionItems> createSpecification(TransactionItemsCriteria criteria) {
        Specification<TransactionItems> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TransactionItems_.id));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), TransactionItems_.itemName));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), TransactionItems_.qty));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), TransactionItems_.amount));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionId(),
                    root -> root.join(TransactionItems_.transaction, JoinType.LEFT).get(Transaction_.id)));
            }
        }
        return specification;
    }
}
