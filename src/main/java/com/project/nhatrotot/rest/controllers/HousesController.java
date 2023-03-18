package com.project.nhatrotot.rest.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.nhatrotot.service.HouseService;
import com.project.nhatrotot.rest.api.HousesApi;
import com.project.nhatrotot.rest.dto.GetHouseHandle200ResponseDto;
import com.project.nhatrotot.rest.dto.HouseCreatingDetailsDto;
import com.project.nhatrotot.rest.dto.HouseDto;
import com.project.nhatrotot.rest.dto.HouseUpdatableFieldsDto;

@RestController
@RequestMapping("/api")
public class HousesController implements HousesApi {
    @Autowired
    private HouseService houseService;

    @Override
    public ResponseEntity<Void> postHouseHandle(@Valid HouseCreatingDetailsDto houseCreatingDetailsDto) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        houseService.createHousePost(houseCreatingDetailsDto, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HouseDto> getHouseById(Integer houseId) {
        // TODO Auto-generated method stub
        return new ResponseEntity<>(houseService.getHouseById(houseId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateHouseById(Integer houseId,
            @Valid HouseUpdatableFieldsDto houseUpdatableFieldsDto) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        boolean isAdmin = authenticationToken.getAuthorities().stream().anyMatch((authority) -> {
            return authority.getAuthority().equals("ROLE_admin") || authority.getAuthority().equals("ROLE_sub_admin");
        });
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        houseService.updateHouseById(userId, isAdmin, houseId, houseUpdatableFieldsDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteHouseById(Integer houseId) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        boolean isAdmin = authenticationToken.getAuthorities().stream().anyMatch((authority) -> {
            return authority.getAuthority().equals("ROLE_admin") || authority.getAuthority().equals("ROLE_sub_admin");
        });
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        houseService.deleteHouseById(userId, isAdmin, houseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetHouseHandle200ResponseDto> getHouseHandle(@NotNull @Valid String queryFor,
            @NotNull @Valid String queryType, @DecimalMin("0") @DecimalMax("100") @Valid BigDecimal distance,
            @Size(min = 1) @Valid List<Integer> houseIds, @Size(min = 8) @Valid List<BigDecimal> polygonPoints,
            @Size(min = 2, max = 2) @Valid List<BigDecimal> mapPoint, @Valid Integer pageSize,
            @Valid Integer pageNumber, @Min(1) @Max(5) @Valid Integer houseType, @Valid String ownerId,
            @Min(1) @Max(2) @Valid Integer houseCategory, @Valid Boolean hasAc, @Valid Boolean hasParking,
            @Valid Boolean hasElevator, @Valid Boolean hasFurnished, @Valid Boolean allowPet, @Valid String province,
            @Valid String district, @Valid String ward, @DecimalMin("0") @Valid BigDecimal squareLte,
            @DecimalMin("0") @Valid BigDecimal squareGte, @Min(0) @Valid Integer roomLte,
            @Min(0) @Valid Integer roomGte, @Min(0) @Valid Integer bedRoomLte, @Min(0) @Valid Integer bedRoomGte,
            @Min(0) @Valid Integer bathRoomLte, @Min(0) @Valid Integer bathRoomGte, @Valid LocalDate fromDate,
            @Valid LocalDate toDate, @DecimalMin("0") @Valid BigDecimal priceFrom,
            @DecimalMin("0") @Valid BigDecimal priceTo, @Valid String sortBy, @Valid String sortOrder) {
        var result = houseService.getHouses(queryFor, queryType, distance, houseIds, polygonPoints, mapPoint, pageSize,
                pageNumber, houseType, ownerId, houseCategory, hasAc, hasParking, hasElevator, hasFurnished, allowPet,
                province,
                district, ward, squareLte, squareGte, roomLte, roomGte, bedRoomLte, bedRoomGte, bathRoomLte,
                bathRoomGte,
                fromDate, toDate, priceFrom, priceTo, sortBy, sortOrder);
        return new ResponseEntity<GetHouseHandle200ResponseDto>(result, HttpStatus.OK);
    }
}
