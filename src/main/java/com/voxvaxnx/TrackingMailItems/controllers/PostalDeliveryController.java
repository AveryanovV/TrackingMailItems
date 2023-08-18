package com.voxvaxnx.TrackingMailItems.controllers;

import com.voxvaxnx.TrackingMailItems.Utils.ConvertPostalDeliveryDTO;
import com.voxvaxnx.TrackingMailItems.dto.PostalDeliveryDTO;
import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import com.voxvaxnx.TrackingMailItems.entities.PostOffice;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;
import com.voxvaxnx.TrackingMailItems.services.PostalDeliveryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class PostalDeliveryController {

    private final PostalDeliveryService postalDeliveryService;

    @Autowired
    public PostalDeliveryController(PostalDeliveryService postalDeliveryService) {
        this.postalDeliveryService = postalDeliveryService;
    }

    @Tag(name = "Register REST", description = "API для регистрации отправления")
    @PostMapping("/register")
    public ResponseEntity<PostalDeliveryDTO> registerDelivery(@RequestBody PostalDeliveryDTO deliveryDTO) {
        PostalDelivery newDelivery = ConvertPostalDeliveryDTO.convert(deliveryDTO);
        PostalDelivery registeredDelivery = postalDeliveryService.registerDelivery(newDelivery);
        PostalDeliveryDTO responseDTO = new PostalDeliveryDTO(registeredDelivery);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Tag(name = "Arrive REST", description = "API для регистрации прибытия в почтовое отделение")
    @PostMapping("/{deliveryId}/arrive")
    public ResponseEntity<String> arriveAtPostOffice(@PathVariable Long deliveryId, @RequestBody PostOffice postOffice) {
        String result = postalDeliveryService.arriveAtPostOffice(deliveryId, postOffice);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Depart REST", description = "API для регистрации убытия из почтового отделения")
    @PostMapping("/{deliveryId}/depart")
    public ResponseEntity<String> departureFromThePostOffice(@PathVariable Long deliveryId) {
        String result = postalDeliveryService.departureFromThePostOffice(deliveryId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Deliver REST", description = "API для регистрации доставки отправления до адресата")
    @PostMapping("/{deliveryId}/deliver")
    public ResponseEntity<String> deliveredToTheRecipient(@PathVariable Long deliveryId) {
        String result = postalDeliveryService.deliveredToTheRecipient(deliveryId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Status REST", description = "API для получения статуса почтового отправления")
    @GetMapping("/{deliveryId}/status")
    public ResponseEntity<String> getDeliveryStatus(@PathVariable Long deliveryId) {
        String status = postalDeliveryService.getDeliveryStatus(deliveryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @Tag(name = "Movement-history REST", description = "API для получения полной истории движения почтового отправления")
    @GetMapping("/{deliveryId}/movement-history")
    public ResponseEntity<List<DeliveryMovement>> getDeliveryMovementHistory(@PathVariable Long deliveryId) {
        List<DeliveryMovement> movementHistory = postalDeliveryService.getDeliveryMovementHistory(deliveryId);
        return new ResponseEntity<>(movementHistory, HttpStatus.OK);
    }
}
