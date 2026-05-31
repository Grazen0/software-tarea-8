package xyz.grazen.rewards.application;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import xyz.grazen.rewards.application.dto.ClientRewardResponseDto;
import xyz.grazen.rewards.application.dto.UpdateClientRewardEmailDto;
import xyz.grazen.rewards.application.exception.ClientRewardNotFoundException;
import xyz.grazen.rewards.domain.ClientRewardService;

@RestController
@RequestMapping("rewards")
@RequiredArgsConstructor
public class ClientRewardsController {

    private final ClientRewardService clientRewardService;

    @GetMapping
    public List<ClientRewardResponseDto> getAllRewards() {
        var rewards = clientRewardService.getAllRewards();
        return rewards.stream().map(ClientRewardResponseDto::new).toList();
    }

    @GetMapping("/{clientId}")
    public ClientRewardResponseDto getRewardById(@PathVariable UUID clientId) {
        var reward = clientRewardService.getRewardByClientId(clientId).orElseThrow(ClientRewardNotFoundException::new);
        return new ClientRewardResponseDto(reward);
    }

    @PatchMapping("/{clientId}/email")
    public ClientRewardResponseDto updateClientRewardEmail(
            @PathVariable UUID clientId,
            @Valid @RequestBody UpdateClientRewardEmailDto dto) {
        var reward = clientRewardService.updateRewardEmailById(clientId, dto.email())
                .orElseThrow(ClientRewardNotFoundException::new);
        return new ClientRewardResponseDto(reward);
    }

}
