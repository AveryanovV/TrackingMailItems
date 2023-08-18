package com.voxvaxnx.TrackingMailItems.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostalDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "delivery_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Column(name = "delivery_type")
    private String type;
    @Column(name = "recipient_index")
    private Integer recipientIndex;
    @Column(name = "recipient_address")
    private String recipientAddress;
    @Column(name = "recipient_name")
    private String recipientName;
    @Column(name = "recipient_status")
    private String recipientStatus;

    @OneToMany
    @JoinColumn(name = "delivery_movements")
    @Fetch(FetchMode.JOIN)
    private List<DeliveryMovement> deliveryMovements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "post_office_id")
    @Fetch(FetchMode.JOIN)
    private PostOffice currentPostOffice;

    public PostalDelivery(String type, Integer recipientIndex, String recipientAddress, String recipientName) {
        this.type = type;
        this.recipientIndex = recipientIndex;
        this.recipientAddress = recipientAddress;
        this.recipientName = recipientName;

    }
}
