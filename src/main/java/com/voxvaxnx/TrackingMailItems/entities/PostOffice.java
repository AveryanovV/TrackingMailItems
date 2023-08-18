package com.voxvaxnx.TrackingMailItems.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "post_office_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Column(name = "post_office_index")
    private Integer index;
    @Column(name = "post_office_name")
    private String name;
    @Column(name = "post_office_address")
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "currentPostOffice", cascade = CascadeType.ALL, orphanRemoval = true)
    // (просто для примера каскадов)
    private List<PostalDelivery> postalDeliveries;
}
