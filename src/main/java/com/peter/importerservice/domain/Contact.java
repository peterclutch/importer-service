package com.peter.importerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_sequenceGenerator")
    @SequenceGenerator(
            name = "contact_sequenceGenerator",
            sequenceName = "contact_sequence",
            allocationSize = 1)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "position")
    private String position;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    public Contact id(Long id) {
        this.id = id;
        return this;
    }

    public Contact firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Contact lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Contact localName(String localName) {
        this.localName = localName;
        return this;
    }

    public Contact phone(String phone) {
        this.phone = phone;
        return this;
    }

    public Contact mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public Contact position(String position) {
        this.position = position;
        return this;
    }

    @JsonIgnore
    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }
}
