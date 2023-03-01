package com.project.nhatrotot.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.VnPayment;
import com.project.nhatrotot.rest.dto.VnpPaymentDetailsDto;

@Mapper
public interface VnpPaymentMapper {
    @Mapping(target = "vnpTransNo", source = "id")
    VnpPaymentDetailsDto convertFromVnPayment(VnPayment payment);

    default OffsetDateTime localDateToOffset(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(date, zoneId).toOffsetDateTime();
    }

}
