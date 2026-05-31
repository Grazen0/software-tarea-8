package xyz.grazen.rewards.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ClientRewardNotFoundException extends ResponseStatusException {

    public ClientRewardNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Client reward not found.");
    }

}
