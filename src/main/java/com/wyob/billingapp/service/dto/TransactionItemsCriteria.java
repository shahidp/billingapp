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

/**
 * Criteria class for the {@link com.wyob.billingapp.domain.TransactionItems} entity. This class is used
 * in {@link com.wyob.billingapp.web.rest.TransactionItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemName;

    private DoubleFilter qty;

    private DoubleFilter amount;

    private LongFilter transactionId;

    public TransactionItemsCriteria(){
    }

    public TransactionItemsCriteria(TransactionItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
    }

    @Override
    public TransactionItemsCriteria copy() {
        return new TransactionItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public DoubleFilter getQty() {
        return qty;
    }

    public void setQty(DoubleFilter qty) {
        this.qty = qty;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
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
        final TransactionItemsCriteria that = (TransactionItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemName,
        qty,
        amount,
        transactionId
        );
    }

    @Override
    public String toString() {
        return "TransactionItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (qty != null ? "qty=" + qty + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            "}";
    }

}
