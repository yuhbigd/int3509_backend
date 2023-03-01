package com.project.nhatrotot.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import java.util.List;
import com.project.nhatrotot.model.Payment;
import com.project.nhatrotot.rest.dto.MyPaymentsDto;

@Mapper
public interface MyPaymentDtoMapper {
    @Mapping(target = "type", source = "type.type")
    @Mapping(target = "state", source = "paymentState")
    @ValueMappings({
            @ValueMapping(source = "done", target = "DONE"),
            @ValueMapping(source = "pending", target = "PENDING"),
            @ValueMapping(source = "cancelled", target = "CANCELLED"),
    })
    MyPaymentsDto convertFromPayment(Payment payment);
    List<MyPaymentsDto> convertFromPayments(List<Payment> payments);

    default OffsetDateTime localDateToOffset(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(date, zoneId).toOffsetDateTime();
    }

    default LocalDate localDateTimeToLocalDate(LocalDateTime date) {
        return date.toLocalDate();
    }
}
