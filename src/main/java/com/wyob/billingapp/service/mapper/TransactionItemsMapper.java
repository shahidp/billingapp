package com.wyob.billingapp.service.mapper;

import com.wyob.billingapp.domain.*;
import com.wyob.billingapp.service.dto.TransactionItemsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionItems} and its DTO {@link TransactionItemsDTO}.
 */
@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface TransactionItemsMapper extends EntityMapper<TransactionItemsDTO, TransactionItems> {

    @Mapping(source = "transaction.id", target = "transactionId")
    TransactionItemsDTO toDto(TransactionItems transactionItems);

    @Mapping(source = "transactionId", target = "transaction")
    TransactionItems toEntity(TransactionItemsDTO transactionItemsDTO);

    default TransactionItems fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionItems transactionItems = new TransactionItems();
        transactionItems.setId(id);
        return transactionItems;
    }
}
