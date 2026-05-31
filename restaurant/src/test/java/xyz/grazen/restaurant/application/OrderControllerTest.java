package xyz.grazen.restaurant.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import xyz.grazen.restaurant.TestcontainersConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRejectEmptyBody() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectMissingClientId() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "restaurantCode": "CODE-123",
                            "total": 50.00,
                            "cardId": "card-123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectMissingRestaurantCode() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "total": 50.00,
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectMissingTotal() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectMissingCardId() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": 50.00
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectNegativeTotal() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": -10.00,
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectZeroTotal() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": 0,
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectEmptyRestaurantCode() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "",
                            "total": 50.00,
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectEmptyCardId() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": 50.00,
                            "cardId": ""
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectNullClientId() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": null,
                            "restaurantCode": "CODE-123",
                            "total": 50.00,
                            "cardId": "card-123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectNullRestaurantCode() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": null,
                            "total": 50.00,
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectNullTotal() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": null,
                            "cardId": "card-123"
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectNullCardId() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": 50.00,
                            "cardId": null
                        }
                        """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateOrderAndReturnCorrectFields() throws Exception {
        var clientId = UUID.randomUUID();

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "CODE-123",
                            "total": 50.00,
                            "cardId": "card-123"
                        }
                        """.formatted(clientId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId.toString()))
                .andExpect(jsonPath("$.restaurantCode").value("CODE-123"))
                .andExpect(jsonPath("$.total").value(50.00))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    public void shouldCreateMultipleOrders() throws Exception {
        var clientId1 = UUID.randomUUID();
        var clientId2 = UUID.randomUUID();

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "RESTO-1",
                            "total": 25.50,
                            "cardId": "card-aaa"
                        }
                        """.formatted(clientId1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantCode").value("RESTO-1"));

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "clientId": "%s",
                            "restaurantCode": "RESTO-2",
                            "total": 100.00,
                            "cardId": "card-bbb"
                        }
                        """.formatted(clientId2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantCode").value("RESTO-2"));
    }

    @Test
    public void shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
