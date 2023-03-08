package com.project.nhatrotot.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "houses")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "house_id")
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    @ToString.Exclude
    UserEntity owner;
    private Double latitude;
    private Double longitude;
    private String address;
    private BigDecimal price;
    private String thumbnail;
    private String title;
    @ManyToOne
    @JoinColumn(name = "house_type", referencedColumnName = "id")
    private HouseType houseType;
    private String video;
    private Boolean visible;
    private Double square;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    private String description;
    @Column(name = "ac")
    private Boolean ac;
    private Boolean parking;
    private Boolean pet;
    private Boolean elevator;
    private Boolean furnished;
    private Integer rooms;
    @Column(name = "bath_rooms")
    private Integer bathRooms;
    @Column(name = "bedrooms")
    private Integer bedRooms;
    @Column(name = "maintenance_fee")
    private BigDecimal maintenanceFee;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "house_has_images", joinColumns = @JoinColumn(name = "house_id", referencedColumnName = "house_id"), inverseJoinColumns = @JoinColumn(name = "house_images_id", referencedColumnName = "id"))
    private Set<HouseImages> houseImages;
    @ManyToOne
    @JoinColumn(name = "trade_category", referencedColumnName = "id")
    private HouseCategory houseCategory;
    @ManyToOne
    @JoinColumn(name = "wards_code", referencedColumnName = "code")
    private Wards ward;
    @ManyToOne
    @JoinColumn(name = "district_code", referencedColumnName = "code")
    private Districts district;
    @ManyToOne
    @JoinColumn(name = "province_code", referencedColumnName = "code")
    private Provinces province;

    public void merger(House newObject) {

        for (Field field : this.getClass().getDeclaredFields()) {

            for (Field newField : newObject.getClass().getDeclaredFields()) {

                if (field.getName().equals(newField.getName())) {

                    try {

                        field.set(this, field.get(this) == null ? newField.get(newObject) : field.get(this));

                    } catch (IllegalAccessException ignore) {
                        // Field update exception on final modifier and other cases.
                    }
                }
            }
        }
    }
}
