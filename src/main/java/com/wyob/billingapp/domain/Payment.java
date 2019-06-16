package com.wyob.billingapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment_tbl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bill_number", nullable = false, unique = true)
    private String billNumber;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "mode_of_description")
    private String modeOfDescription;

    @Column(name = "payid")
    private String payid;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    @ManyToOne(optional = false)
    @NotNull
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public Payment billNumber(String billNumber) {
        this.billNumber = billNumber;
        return this;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public Payment billDate(LocalDate billDate) {
        this.billDate = billDate;
        return this;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public Payment paidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
        return this;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public Payment modeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
        return this;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getModeOfDescription() {
        return modeOfDescription;
    }

    public Payment modeOfDescription(String modeOfDescription) {
        this.modeOfDescription = modeOfDescription;
        return this;
    }

    public void setModeOfDescription(String modeOfDescription) {
        this.modeOfDescription = modeOfDescription;
    }

    public String getPayid() {
        return payid;
    }

    public Payment payid(String payid) {
        this.payid = payid;
        return this;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Payment createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Payment createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Payment lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Payment lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Payment transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", billNumber='" + getBillNumber() + "'" +
            ", billDate='" + getBillDate() + "'" +
            ", paidAmount=" + getPaidAmount() +
            ", modeOfPayment='" + getModeOfPayment() + "'" +
            ", modeOfDescription='" + getModeOfDescription() + "'" +
            ", payid='" + getPayid() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
