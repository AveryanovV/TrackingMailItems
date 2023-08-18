package com.voxvaxnx.TrackingMailItems.Utils;

import com.voxvaxnx.TrackingMailItems.dto.PostalDeliveryDTO;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;

public class ConvertPostalDeliveryDTO {

    PostalDeliveryDTO postalDeliveryDTO;

    public ConvertPostalDeliveryDTO(PostalDeliveryDTO postalDeliveryDTO) {
        this.postalDeliveryDTO = postalDeliveryDTO;
    }

    public static PostalDelivery convert(PostalDeliveryDTO postalDeliveryDTO) {

        PostalDelivery postalDelivery = new PostalDelivery();
        postalDelivery.setType(postalDeliveryDTO.getType());
        postalDelivery.setRecipientIndex(postalDeliveryDTO.getRecipientIndex());
        postalDelivery.setRecipientAddress(postalDeliveryDTO.getRecipientAddress());
        postalDelivery.setRecipientName(postalDeliveryDTO.getRecipientName());
        return postalDelivery;
    }
}
