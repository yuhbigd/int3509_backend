package com.project.nhatrotot.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "administrative_regions")
public class AdministrativeRegions {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "code_name")
    private String codeName;
    @Column(name = "code_name_en")
    private String codeNameEn;
    @OneToMany(mappedBy = "administrativeRegion", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Provinces> provinces;
}
