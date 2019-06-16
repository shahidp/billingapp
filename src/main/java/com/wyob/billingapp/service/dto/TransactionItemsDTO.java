package com.wyob.billingapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.wyob.billingapp.domain.TransactionItems} entity.
 */
public class TransactionItemsDTO implements Serializable {

    private Long id;

    private String itemName;

    private Double qty;

    private Double amount;


    private Long transactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
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

        TransactionItemsDTO transactionItemsDTO = (TransactionItemsDTO) o;
        if (transactionItemsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionItemsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionItemsDTO{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", qty=" + getQty() +
            ", amount=" + getAmount() +
            ", transaction=" + getTransactionId() +
            "}";
    }
}
