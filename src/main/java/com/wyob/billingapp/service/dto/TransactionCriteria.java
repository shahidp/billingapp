package com.wyob.billingapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.wyob.billingapp.domain.Transaction} entity. This class is used
 * in {@link com.wyob.billingapp.web.rest.TransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transactionNumber;

    private LocalDateFilter trnDate;

    private DoubleFilter totalAmount;

    private StringFilter description;

    private StringFilter items;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter customerId;

    public TransactionCriteria(){
    }

    public TransactionCriteria(TransactionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.transactionNumber = other.transactionNumber == null ? null : other.transactionNumber.copy();
        this.trnDate = other.trnDate == null ? null : other.trnDate.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.items = other.items == null ? null : other.items.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public TransactionCriteria copy() {
        return new TransactionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(StringFilter transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDateFilter getTrnDate() {
        return trnDate;
    }

    public void setTrnDate(LocalDateFilter trnDate) {
        this.trnDate = trnDate;
    }

    public DoubleFilter getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(DoubleFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getItems() {
        return items;
    }

    public void setItems(StringFilter items) {
        this.items = items;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionCriteria that = (TransactionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(transactionNumber, that.transactionNumber) &&
            Objects.equals(trnDate, that.trnDate) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(items, that.items) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        transactionNumber,
        trnDate,
        totalAmount,
        description,
        items,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate,
        customerId
        );
    }

    @Override
    public String toString() {
        return "TransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (transactionNumber != null ? "transactionNumber=" + transactionNumber + ", " : "") +
                (trnDate != null ? "trnDate=" + trnDate + ", " : "") +
                (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (items != null ? "items=" + items + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
