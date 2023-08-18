package com.voxvaxnx.TrackingMailItems.services;

import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import com.voxvaxnx.TrackingMailItems.entities.PostOffice;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;
import com.voxvaxnx.TrackingMailItems.enums.StatusDelivery;
import com.voxvaxnx.TrackingMailItems.repository.DeliveryMovementRepository;
import com.voxvaxnx.TrackingMailItems.repository.PostOfficeRepository;
import com.voxvaxnx.TrackingMailItems.repository.PostalDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.String.*;

@Service
public class PostalDeliveryServiceImpl implements PostalDeliveryService {
    private final PostalDeliveryRepository postalDeliveryRepository;
    private final DeliveryMovementRepository deliveryMovementRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Autowired
    public PostalDeliveryServiceImpl(
            PostalDeliveryRepository postalDeliveryRepository,
            DeliveryMovementRepository deliveryMovementRepository, PostOfficeRepository postOfficeRepository) {
        this.postalDeliveryRepository = postalDeliveryRepository;
        this.deliveryMovementRepository = deliveryMovementRepository;
        this.postOfficeRepository = postOfficeRepository;
    }

    @Override
    public PostalDelivery registerDelivery(PostalDelivery delivery) {
        delivery.setRecipientStatus(valueOf(StatusDelivery.Accepted));
        delivery.setDeliveryMovements(new ArrayList<>());
        PostalDelivery savedDelivery = postalDeliveryRepository.save(delivery);
        DeliveryMovement deliveryMovement = new DeliveryMovement(delivery, new Date(), valueOf(StatusDelivery.Accepted), "В пункте приема");
        delivery.getDeliveryMovements().add(deliveryMovement);
        deliveryMovementRepository.save(deliveryMovement);
        return savedDelivery;
    }

    @Override
    public String arriveAtPostOffice(Long deliveryId, PostOffice office) {

        Optional<PostalDelivery> deliveryOptional = postalDeliveryRepository.findById(deliveryId);
        if (deliveryOptional.isPresent()) {
            PostalDelivery delivery = deliveryOptional.get();
            delivery.setCurrentPostOffice(postOfficeRepository.save(office));
            delivery.setRecipientStatus(valueOf(StatusDelivery.ArrivedAtThePostOffice));
            postalDeliveryRepository.save(delivery);
            DeliveryMovement deliveryMovement = new DeliveryMovement(delivery, new Date(), valueOf(StatusDelivery.ArrivedAtThePostOffice), office.toString());
            deliveryMovementRepository.save(deliveryMovement);
            return "Отправление успешно зарегистрировано в отделении";
        } else {
            return "Отправление не найдено";
        }
    }

    @Override
    public String departureFromThePostOffice(Long deliveryId) {
        Optional<PostalDelivery> deliveryOptional = postalDeliveryRepository.findById(deliveryId);
        if (deliveryOptional.isPresent()) {
            PostalDelivery delivery = deliveryOptional.get();
            delivery.setCurrentPostOffice(null);
            delivery.setRecipientStatus(valueOf(StatusDelivery.DepartPostOffice));
            postalDeliveryRepository.save(delivery);
            DeliveryMovement deliveryMovement = new DeliveryMovement(delivery, new Date(), valueOf(StatusDelivery.DepartPostOffice), "в пути...");
            deliveryMovementRepository.save(deliveryMovement);

            return "Отправление покинуло отделение";
        } else {
            return "Отправление не найдено";
        }
    }

    @Override
    public String deliveredToTheRecipient(Long deliveryId) {
        Optional<PostalDelivery> deliveryOptional = postalDeliveryRepository.findById(deliveryId);
        if (deliveryOptional.isPresent()) {
            PostalDelivery delivery = deliveryOptional.get();
            delivery.setCurrentPostOffice(null);
            delivery.setRecipientStatus(valueOf(StatusDelivery.Delivered));
            postalDeliveryRepository.save(delivery);
            DeliveryMovement deliveryMovement = new DeliveryMovement(delivery, new Date(), valueOf(StatusDelivery.Delivered), delivery.getRecipientAddress());
            deliveryMovementRepository.save(deliveryMovement);

            return "Отправление доставлено";
        } else {
            return "Отправление не найдено";
        }
    }

    @Override
    public String getDeliveryStatus(Long deliveryId) {
        Optional<PostalDelivery> deliveryOptional = postalDeliveryRepository.findById(deliveryId);
        if (deliveryOptional.isPresent()) {
            PostalDelivery delivery = deliveryOptional.get();
            return delivery.getRecipientStatus();
        } else {
            return "Отправление не найдено";
        }
    }

    @Override
    public List<DeliveryMovement> getDeliveryMovementHistory(Long deliveryId) {
        return postalDeliveryRepository.findMovementHistoryByDeliveryId(deliveryId);
    }


}
