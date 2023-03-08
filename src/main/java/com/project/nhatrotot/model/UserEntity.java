package com.project.nhatrotot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @EqualsAndHashCode.Include
    private String userId;
    @NotBlank
    @Length(max = 50)
    @Column(name = "first_name")
    private String firstName;
    @NotBlank
    @Length(max = 50)
    @Column(name = "last_name")
    private String lastName;
    @NotBlank
    @Length(max = 500)
    @Column(name = "email")
    @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    private String email;
    @Column(name = "register_at")
    private LocalDateTime registerAt;
    @NotBlank
    @Length(max = 12)
    @Pattern(regexp = "^\\d{1,12}$")
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "banned")
    private boolean banned;
    @Column(name = "birth_date")
    private LocalDateTime birthDate;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(columnDefinition = "TEXT")
    private String intro;
    @NotBlank
    private String image;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "avg_rating")
    private float avgRating;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "title", referencedColumnName = "id")
    private UserTitle title;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role", referencedColumnName = "id")
    private UserRole role;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Payment> payments;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<House> houses;

    public boolean getBanned() {
        return this.banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
