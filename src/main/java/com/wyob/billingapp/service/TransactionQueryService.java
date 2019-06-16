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

import com.wyob.billingapp.domain.Transaction;
import com.wyob.billingapp.domain.*; // for static metamodels
import com.wyob.billingapp.repository.TransactionRepository;
import com.wyob.billingapp.service.dto.TransactionCriteria;
import com.wyob.billingapp.service.dto.TransactionDTO;
import com.wyob.billingapp.service.mapper.TransactionMapper;

/**
 * Service for executing complex queries for {@link Transaction} entities in the database.
 * The main input is a {@link TransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionDTO} or a {@link Page} of {@link TransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionQueryService extends QueryService<Transaction> {

    private final Logger log = LoggerFactory.getLogger(TransactionQueryService.class);

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public TransactionQueryService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Return a {@link List} of {@link TransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> findByCriteria(TransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionMapper.toDto(transactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findByCriteria(TransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification, page)
            .map(transactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.count(specification);
    }

    /**
     * Function to convert TransactionCriteria to a {@link Specification}.
     */
    private Specification<Transaction> createSpecification(TransactionCriteria criteria) {
        Specification<Transaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Transaction_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionNumber(), Transaction_.transactionNumber));
            }
            if (criteria.getTrnDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrnDate(), Transaction_.trnDate));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), Transaction_.totalAmount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Transaction_.description));
            }
            if (criteria.getItems() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItems(), Transaction_.items));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Transaction_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Transaction_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Transaction_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Transaction_.lastModifiedDate));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Transaction_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}
