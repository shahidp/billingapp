package com.wyob.billingapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A TransactionItems.
 */
@Entity
@Table(name = "transaction_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "amount")
    private Double amount;

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

    public String getItemName() {
        return itemName;
    }

    public TransactionItems itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getQty() {
        return qty;
    }

    public TransactionItems qty(Double qty) {
        this.qty = qty;
        return this;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getAmount() {
        return amount;
    }

    public TransactionItems amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionItems transaction(Transaction transaction) {
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
        if (!(o instanceof TransactionItems)) {
            return false;
        }
        return id != null && id.equals(((TransactionItems) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TransactionItems{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", qty=" + getQty() +
            ", amount=" + getAmount() +
            "}";
    }
}
