package com.voxvaxnx.TrackingMailItems.repository;

import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostalDeliveryRepository extends JpaRepository<PostalDelivery, Long> {
    @Query("SELECT dm FROM DeliveryMovement dm WHERE dm.delivery.id = :deliveryId ORDER BY dm.movementDate")
    List<DeliveryMovement> findMovementHistoryByDeliveryId(@Param("deliveryId")Long deliveryId);
}
