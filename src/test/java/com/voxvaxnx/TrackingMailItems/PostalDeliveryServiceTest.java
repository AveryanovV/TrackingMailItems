package com.voxvaxnx.TrackingMailItems;

import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import com.voxvaxnx.TrackingMailItems.entities.PostOffice;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;
import com.voxvaxnx.TrackingMailItems.enums.StatusDelivery;
import com.voxvaxnx.TrackingMailItems.repository.DeliveryMovementRepository;
import com.voxvaxnx.TrackingMailItems.repository.PostOfficeRepository;
import com.voxvaxnx.TrackingMailItems.repository.PostalDeliveryRepository;
import com.voxvaxnx.TrackingMailItems.services.PostalDeliveryService;
import com.voxvaxnx.TrackingMailItems.services.PostalDeliveryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostalDeliveryServiceTest {

    @Mock
    private PostalDeliveryRepository postalDeliveryRepository;
    @Mock
    private DeliveryMovementRepository deliveryMovementRepository;
    @Mock
    private PostOfficeRepository postOfficeRepository;

    @Autowired
    private PostalDeliveryService postalDeliveryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        postalDeliveryService = new PostalDeliveryServiceImpl(postalDeliveryRepository, deliveryMovementRepository, postOfficeRepository);
    }

    @Test
    public void testRegisterDelivery() {
        PostalDelivery delivery = new PostalDelivery();
        delivery.setDeliveryMovements(new ArrayList<>());

        when(postalDeliveryRepository.save(delivery)).thenReturn(delivery);

        PostalDelivery savedDelivery = postalDeliveryService.registerDelivery(delivery);

        verify(postalDeliveryRepository).save(delivery);

        assertEquals(StatusDelivery.Accepted.toString(), savedDelivery.getRecipientStatus());
        assertEquals(1, savedDelivery.getDeliveryMovements().size());
    }

    @Test
    public void testArriveAtPostOffice() {
        Long deliveryId = 1L;
        PostOffice office = new PostOffice();
        PostalDelivery postalDelivery = new PostalDelivery();
        postalDelivery.setId(deliveryId);

        when(postalDeliveryRepository.findById(deliveryId)).thenReturn(Optional.of(postalDelivery));
        when(postOfficeRepository.save(office)).thenReturn(office);

        String result = postalDeliveryService.arriveAtPostOffice(deliveryId, office);

        verify(postalDeliveryRepository).findById(deliveryId);
        verify(postalDeliveryRepository).save(any(PostalDelivery.class));
        verify(postOfficeRepository).save(office);

        assertEquals("Отправление успешно зарегистрировано в отделении", result);
    }

    @Test
    public void testDepartureFromThePostOffice() {
        Long deliveryId = 1L;
        PostalDelivery delivery = new PostalDelivery();
        DeliveryMovement deliveryMovement = new DeliveryMovement();

        when(postalDeliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));
        when(deliveryMovementRepository.save(any(DeliveryMovement.class))).thenReturn(deliveryMovement);

        String result = postalDeliveryService.departureFromThePostOffice(deliveryId);

        delivery.setDeliveryMovements(new ArrayList<>());
        delivery.getDeliveryMovements().add(deliveryMovement);

        verify(postalDeliveryRepository).findById(deliveryId);
        verify(postalDeliveryRepository).save(delivery);
        verify(deliveryMovementRepository).save(any(DeliveryMovement.class));

        assertEquals("Отправление покинуло отделение", result);
        assertNull(delivery.getCurrentPostOffice());
        assertEquals(StatusDelivery.DepartPostOffice.toString(), delivery.getRecipientStatus());
        assertEquals(deliveryMovement, delivery.getDeliveryMovements().get(0));
    }

    @Test
    public void testDepartureFromThePostOfficeNotFound() {
        Long deliveryId = 1L;

        when(postalDeliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        String result = postalDeliveryService.departureFromThePostOffice(deliveryId);

        verify(postalDeliveryRepository).findById(deliveryId);
        verifyNoMoreInteractions(postalDeliveryRepository, deliveryMovementRepository);

        assertEquals("Отправление не найдено", result);
    }

    @Test
    public void testDeliveredToTheRecipientNotFound() {
        Long deliveryId = 1L;

        when(postalDeliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        String result = postalDeliveryService.deliveredToTheRecipient(deliveryId);

        verify(postalDeliveryRepository).findById(deliveryId);
        verifyNoMoreInteractions(postalDeliveryRepository, deliveryMovementRepository);

        assertEquals("Отправление не найдено", result);
    }

    @Test
    public void testGetDeliveryStatus() {
        Long deliveryId = 1L;
        PostalDelivery delivery = new PostalDelivery();
        delivery.setRecipientStatus(StatusDelivery.ArrivedAtThePostOffice.toString());

        when(postalDeliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));

        String result = postalDeliveryService.getDeliveryStatus(deliveryId);

        verify(postalDeliveryRepository).findById(deliveryId);
        verifyNoMoreInteractions(postalDeliveryRepository);

        assertEquals(StatusDelivery.ArrivedAtThePostOffice.toString(), result);
    }

    @Test
    public void testGetDeliveryStatusNotFound() {
        Long deliveryId = 1L;

        when(postalDeliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        String result = postalDeliveryService.getDeliveryStatus(deliveryId);

        verify(postalDeliveryRepository).findById(deliveryId);
        verifyNoMoreInteractions(postalDeliveryRepository);

        assertEquals("Отправление не найдено", result);
    }

    @Test
    public void testGetDeliveryMovementHistory() {
        Long deliveryId = 1L;

        List<DeliveryMovement> movementHistory = new ArrayList<>();
        movementHistory.add(new DeliveryMovement());
        movementHistory.add(new DeliveryMovement());

        PostalDelivery postalDelivery = new PostalDelivery();
        postalDelivery.setDeliveryMovements(movementHistory);

        when(postalDeliveryRepository.findMovementHistoryByDeliveryId(deliveryId)).thenReturn(movementHistory);

        List<DeliveryMovement> result = postalDeliveryService.getDeliveryMovementHistory(deliveryId);

        verify(postalDeliveryRepository).findMovementHistoryByDeliveryId(deliveryId);
        verifyNoMoreInteractions(postalDeliveryRepository);

        assertEquals(movementHistory, result);
    }

    @Test
    public void testGetDeliveryMovementHistoryNotFound() {
        Long deliveryId = 1L;

        when(postalDeliveryRepository.findMovementHistoryByDeliveryId(deliveryId)).thenReturn(new ArrayList<>());

        List<DeliveryMovement> result = postalDeliveryService.getDeliveryMovementHistory(deliveryId);

        verify(postalDeliveryRepository).findMovementHistoryByDeliveryId(deliveryId);
        verifyNoMoreInteractions(postalDeliveryRepository);

        assertTrue(result.isEmpty());
    }
}
