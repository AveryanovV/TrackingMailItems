package com.voxvaxnx.TrackingMailItems.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMovement {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "delivery_movement_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    @Fetch(FetchMode.JOIN)
    private PostalDelivery delivery;

    @Column(name = "movement_date")
    private Date movementDate;
    @Column(name = "movement_status")
    private String status;
    @Column(name = "movement_location")
    private String location;

    public DeliveryMovement(PostalDelivery delivery, Date movementDate, String status, String location) {
        this.delivery = delivery;
        this.movementDate = movementDate;
        this.status = status;
        this.location = location;
    }
}
