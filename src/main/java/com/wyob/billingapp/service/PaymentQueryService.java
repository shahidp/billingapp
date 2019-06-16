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

import com.wyob.billingapp.domain.Payment;
import com.wyob.billingapp.domain.*; // for static metamodels
import com.wyob.billingapp.repository.PaymentRepository;
import com.wyob.billingapp.service.dto.PaymentCriteria;
import com.wyob.billingapp.service.dto.PaymentDTO;
import com.wyob.billingapp.service.mapper.PaymentMapper;

/**
 * Service for executing complex queries for {@link Payment} entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentDTO} or a {@link Page} of {@link PaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private final Logger log = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentQueryService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Return a {@link List} of {@link PaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByCriteria(PaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentMapper.toDto(paymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByCriteria(PaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.findAll(specification, page)
            .map(paymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert PaymentCriteria to a {@link Specification}.
     */
    private Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Payment_.id));
            }
            if (criteria.getBillNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillNumber(), Payment_.billNumber));
            }
            if (criteria.getBillDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBillDate(), Payment_.billDate));
            }
            if (criteria.getPaidAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaidAmount(), Payment_.paidAmount));
            }
            if (criteria.getModeOfPayment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModeOfPayment(), Payment_.modeOfPayment));
            }
            if (criteria.getModeOfDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModeOfDescription(), Payment_.modeOfDescription));
            }
            if (criteria.getPayid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayid(), Payment_.payid));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Payment_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Payment_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Payment_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Payment_.lastModifiedDate));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionId(),
                    root -> root.join(Payment_.transaction, JoinType.LEFT).get(Transaction_.id)));
            }
        }
        return specification;
    }
}
