package com.project.nhatrotot.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.ManualPayment;
import com.project.nhatrotot.rest.dto.ManualPaymentDetailDto;

@Mapper
public interface ManualPaymentDtoMapper {
    @Mapping(target = "creatorEmail", source = "creator.email")
    @Mapping(target = "verifierEmail", source = "verifier.email")
    ManualPaymentDetailDto convertFromManualPayment(ManualPayment payment);

    default OffsetDateTime localDateToOffset(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(date, zoneId).toOffsetDateTime();
    }
}
