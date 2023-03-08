package com.project.nhatrotot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "provinces")
public class Provinces {
    @Id
    @Column(name = "code")
    @EqualsAndHashCode.Include
    private String code;
    String name;
    @Column(name = "name_en")
    String nameEn;
    @Column(name = "full_name")
    String fullName;
    @Column(name = "full_name_en")
    String fullNameEn;
    @Column(name = "code_name")
    String codeName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id", referencedColumnName = "id")
    @ToString.Exclude
    AdministrativeUnits administrativeUnits;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_region_id", referencedColumnName = "id")
    @ToString.Exclude
    AdministrativeRegions administrativeRegion;
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Districts> districts;
    
}
