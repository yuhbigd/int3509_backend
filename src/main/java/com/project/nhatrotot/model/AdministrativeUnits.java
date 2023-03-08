package com.project.nhatrotot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "administrative_units")
public class AdministrativeUnits {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "full_name_en")
    private String fullNameEn;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "short_name_en")
    private String shortNameEm;
    @Column(name = "code_name")
    private String codeName;
    @Column(name = "code_name_en")
    private String codeNameEn;
}
