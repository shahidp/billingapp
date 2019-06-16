package com.wyob.billingapp.service.mapper;

import com.wyob.billingapp.domain.*;
import com.wyob.billingapp.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "transaction.id", target = "transactionId")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "transactionId", target = "transaction")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
