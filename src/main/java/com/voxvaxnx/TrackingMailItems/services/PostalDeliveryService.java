package com.voxvaxnx.TrackingMailItems.services;

import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import com.voxvaxnx.TrackingMailItems.entities.PostOffice;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;

import java.util.List;

public interface PostalDeliveryService {
    PostalDelivery registerDelivery(PostalDelivery delivery);

    String arriveAtPostOffice(Long deliveryId, PostOffice office);

    String departureFromThePostOffice(Long deliveryId);

    String deliveredToTheRecipient(Long deliveryId);

    String getDeliveryStatus(Long deliveryId);

    List<DeliveryMovement> getDeliveryMovementHistory(Long deliveryId);
}