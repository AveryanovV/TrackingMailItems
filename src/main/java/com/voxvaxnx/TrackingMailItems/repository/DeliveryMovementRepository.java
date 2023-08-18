package com.voxvaxnx.TrackingMailItems.repository;

import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryMovementRepository extends JpaRepository<DeliveryMovement, Long> {
}
