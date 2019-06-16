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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.wyob.billingapp.domain.Payment} entity. This class is used
 * in {@link com.wyob.billingapp.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter billNumber;

    private LocalDateFilter billDate;

    private DoubleFilter paidAmount;

    private StringFilter modeOfPayment;

    private StringFilter modeOfDescription;

    private StringFilter payid;

    private StringFilter createdBy;

    private LocalDateFilter createdDate;

    private StringFilter lastModifiedBy;

    private LocalDateFilter lastModifiedDate;

    private LongFilter transactionId;

    public PaymentCriteria(){
    }

    public PaymentCriteria(PaymentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.billNumber = other.billNumber == null ? null : other.billNumber.copy();
        this.billDate = other.billDate == null ? null : other.billDate.copy();
        this.paidAmount = other.paidAmount == null ? null : other.paidAmount.copy();
        this.modeOfPayment = other.modeOfPayment == null ? null : other.modeOfPayment.copy();
        this.modeOfDescription = other.modeOfDescription == null ? null : other.modeOfDescription.copy();
        this.payid = other.payid == null ? null : other.payid.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(StringFilter billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDateFilter getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateFilter billDate) {
        this.billDate = billDate;
    }

    public DoubleFilter getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(DoubleFilter paidAmount) {
        this.paidAmount = paidAmount;
    }

    public StringFilter getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(StringFilter modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public StringFilter getModeOfDescription() {
        return modeOfDescription;
    }

    public void setModeOfDescription(StringFilter modeOfDescription) {
        this.modeOfDescription = modeOfDescription;
    }

    public StringFilter getPayid() {
        return payid;
    }

    public void setPayid(StringFilter payid) {
        this.payid = payid;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCriteria that = (PaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(billNumber, that.billNumber) &&
            Objects.equals(billDate, that.billDate) &&
            Objects.equals(paidAmount, that.paidAmount) &&
            Objects.equals(modeOfPayment, that.modeOfPayment) &&
            Objects.equals(modeOfDescription, that.modeOfDescription) &&
            Objects.equals(payid, that.payid) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        billNumber,
        billDate,
        paidAmount,
        modeOfPayment,
        modeOfDescription,
        payid,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate,
        transactionId
        );
    }

    @Override
    public String toString() {
        return "PaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (billNumber != null ? "billNumber=" + billNumber + ", " : "") +
                (billDate != null ? "billDate=" + billDate + ", " : "") +
                (paidAmount != null ? "paidAmount=" + paidAmount + ", " : "") +
                (modeOfPayment != null ? "modeOfPayment=" + modeOfPayment + ", " : "") +
                (modeOfDescription != null ? "modeOfDescription=" + modeOfDescription + ", " : "") +
                (payid != null ? "payid=" + payid + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            "}";
    }

}
