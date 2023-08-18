package com.voxvaxnx.TrackingMailItems.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostalDeliveryDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String type;
    private Integer recipientIndex;
    private String recipientAddress;
    private String recipientName;

    public PostalDeliveryDTO(PostalDelivery postalDelivery) {
        this.id = postalDelivery.getId();
        this.type = postalDelivery.getType();
        this.recipientIndex = postalDelivery.getRecipientIndex();
        this.recipientAddress = postalDelivery.getRecipientAddress();
        this.recipientName = postalDelivery.getRecipientName();
    }
}