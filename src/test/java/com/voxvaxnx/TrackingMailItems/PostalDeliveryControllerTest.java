package com.voxvaxnx.TrackingMailItems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxvaxnx.TrackingMailItems.controllers.PostalDeliveryController;
import com.voxvaxnx.TrackingMailItems.dto.PostalDeliveryDTO;
import com.voxvaxnx.TrackingMailItems.entities.DeliveryMovement;
import com.voxvaxnx.TrackingMailItems.entities.PostOffice;
import com.voxvaxnx.TrackingMailItems.entities.PostalDelivery;
import com.voxvaxnx.TrackingMailItems.services.PostalDeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(PostalDeliveryController.class)
public class PostalDeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostalDeliveryService postalDeliveryService;

    @InjectMocks
    private PostalDeliveryController postalDeliveryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterDelivery() throws Exception {
        PostalDeliveryDTO deliveryDTO = new PostalDeliveryDTO();

        PostalDelivery registeredDelivery = new PostalDelivery();

        when(postalDeliveryService.registerDelivery(any(PostalDelivery.class))).thenReturn(registeredDelivery);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveries/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(registeredDelivery.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(registeredDelivery.getType()));

    }

    @Test
    public void testArriveAtPostOffice() throws Exception {
        Long deliveryId = 1L;
        PostOffice postOffice = new PostOffice();
        String expectedResult = "Успешно доставлено на почту.";

        when(postalDeliveryService.arriveAtPostOffice(deliveryId, postOffice)).thenReturn(expectedResult);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveries/{deliveryId}/arrive", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postOffice)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testDepartureFromThePostOffice() throws Exception {
        Long deliveryId = 1L;
        String expectedResult = "Благополучно отбыл из почтового отделения.";

        when(postalDeliveryService.departureFromThePostOffice(deliveryId)).thenReturn(expectedResult);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveries/{deliveryId}/depart", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testDeliveredToTheRecipient() throws Exception {
        Long deliveryId = 1L;
        String expectedResult = "Успешно доставлено получателю.";

        when(postalDeliveryService.deliveredToTheRecipient(deliveryId)).thenReturn(expectedResult);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveries/{deliveryId}/deliver", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testGetDeliveryStatus() throws Exception {
        Long deliveryId = 1L;
        String expectedStatus = "Delivered";

        when(postalDeliveryService.getDeliveryStatus(deliveryId)).thenReturn(expectedStatus);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries/{deliveryId}/status", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedStatus));
    }

    @Test
    public void testGetDeliveryMovementHistory() throws Exception {
        Long deliveryId = 1L;

        List<DeliveryMovement> expectedMovementHistory = new ArrayList<>();
        expectedMovementHistory.add(new DeliveryMovement(new PostalDelivery(), new Date(), "Arrived", "Промежуточная почта 1"));
        expectedMovementHistory.add(new DeliveryMovement(new PostalDelivery(), new Date(), "Departed", "Промежуточная почта 2"));
        expectedMovementHistory.add(new DeliveryMovement(new PostalDelivery(), new Date(), "Delivered", "Доставлено"));

        when(postalDeliveryService.getDeliveryMovementHistory(anyLong())).thenReturn(expectedMovementHistory);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries/{deliveryId}/movement-history", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expectedMovementHistory.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(expectedMovementHistory.get(0).getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].location").value(expectedMovementHistory.get(0).getLocation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value(expectedMovementHistory.get(1).getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].location").value(expectedMovementHistory.get(1).getLocation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].status").value(expectedMovementHistory.get(2).getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].location").value(expectedMovementHistory.get(2).getLocation()));
    }


}
