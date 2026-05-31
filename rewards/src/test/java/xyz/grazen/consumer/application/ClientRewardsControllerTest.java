package xyz.grazen.rewards.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import xyz.grazen.rewards.TestcontainersConfiguration;
import xyz.grazen.rewards.infrastructure.jooq.public_.tables.ClientRewards;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestcontainersConfiguration.class)
public class ClientRewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DSLContext dsl;

    @BeforeEach
    void setUp() {
        dsl.truncate(ClientRewards.CLIENT_REWARDS).execute();
    }

    @Test
    public void shouldReturnEmptyListWhenNoRewards() throws Exception {
        mockMvc.perform(get("/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void shouldGetRewardByClientId() throws Exception {
        var clientId = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, clientId)
                .set(ClientRewards.CLIENT_REWARDS.EMAIL, "test@test.com")
                .execute();

        mockMvc.perform(get("/rewards/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId.toString()))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void shouldReturn404WhenRewardNotFound() throws Exception {
        mockMvc.perform(get("/rewards/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnAllRewards() throws Exception {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, id1)
                .set(ClientRewards.CLIENT_REWARDS.EMAIL, "a@test.com")
                .execute();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, id2)
                .set(ClientRewards.CLIENT_REWARDS.EMAIL, "b@test.com")
                .execute();

        mockMvc.perform(get("/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void shouldUpdateEmail() throws Exception {
        var clientId = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, clientId)
                .set(ClientRewards.CLIENT_REWARDS.EMAIL, "old@test.com")
                .execute();

        mockMvc.perform(patch("/rewards/" + clientId + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email": "new@test.com"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId.toString()))
                .andExpect(jsonPath("$.email").value("new@test.com"));
    }

    @Test
    public void shouldReturn404WhenUpdatingEmailForNonExistentReward() throws Exception {
        mockMvc.perform(patch("/rewards/" + UUID.randomUUID() + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email": "new@test.com"}
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRejectInvalidEmail() throws Exception {
        var clientId = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, clientId)
                .set(ClientRewards.CLIENT_REWARDS.EMAIL, "old@test.com")
                .execute();

        mockMvc.perform(patch("/rewards/" + clientId + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email": "not-an-email"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectEmptyBody() throws Exception {
        var clientId = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, clientId)
                .execute();

        mockMvc.perform(patch("/rewards/" + clientId + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectNullEmail() throws Exception {
        var clientId = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, clientId)
                .execute();

        mockMvc.perform(patch("/rewards/" + clientId + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email": null}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectMalformedJson() throws Exception {
        var clientId = UUID.randomUUID();
        dsl.insertInto(ClientRewards.CLIENT_REWARDS)
                .set(ClientRewards.CLIENT_REWARDS.CLIENT_ID, clientId)
                .execute();

        mockMvc.perform(patch("/rewards/" + clientId + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }
}
