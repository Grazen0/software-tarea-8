package xyz.grazen.rewards.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateClientRewardEmailDto(
        @NotNull @Email String email) {

}
