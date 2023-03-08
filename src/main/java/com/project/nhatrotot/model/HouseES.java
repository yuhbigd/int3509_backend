package com.project.nhatrotot.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(indexName = "houses")
public class HouseES {
    @JsonProperty("house_id")
    @Field(value = "house_id", type = FieldType.Integer)
    private Integer houseId;
    @JsonProperty("owner_id")
    @Field(value = "owner_id", type = FieldType.Keyword)
    private String ownerId;
    private Double latitude;
    private Double longitude;
    private String title;
    private String address;
    private String thumbnail;
    private String video;
    private Integer visible;
    @Field(value = "house_type", type = FieldType.Integer)
    @JsonProperty("house_type")
    private Integer houseType;
    @Field(value = "trade_category", type = FieldType.Integer)
    @JsonProperty("trade_category")
    private Integer tradeCategory;
    @JsonProperty("wards_code")
    @Field(value = "wards_code", type = FieldType.Keyword)
    private String wardCode;
    @JsonProperty("province_code")
    @Field(value = "province_code", type = FieldType.Keyword)
    private String provinceCode;
    @JsonProperty("district_code")
    @Field(value = "district_code", type = FieldType.Keyword)
    private String districtCode;
    @JsonProperty("created_date")
    @Field(value = "created_date", type = FieldType.Date)
    private Date createdDate;
    private BigDecimal price;
    @JsonProperty("maintenance_fee")
    @Field(value = "maintenance_fee", type = FieldType.Double)
    private BigDecimal maintenanceFee;
    private Double square;
    private Boolean ac;
    private Boolean parking;
    private Boolean pet;
    private Boolean elevator;
    private Boolean furnished;
    private Integer rooms;
    private Integer bedrooms;
    @JsonProperty("bath_rooms")
    @Field(value = "bath_rooms", type = FieldType.Integer)
    private Integer bathRooms;

}
