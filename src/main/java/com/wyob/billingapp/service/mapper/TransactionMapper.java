package com.wyob.billingapp.service.mapper;

import com.wyob.billingapp.domain.*;
import com.wyob.billingapp.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.fname", target = "customerFname")
    TransactionDTO toDto(Transaction transaction);

    @Mapping(source = "customerId", target = "customer")
    Transaction toEntity(TransactionDTO transactionDTO);

    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
