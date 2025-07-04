package ddd.darayo.festival.presentation.user.exchanges;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLoginRes {
    private final String token;

    @Builder
    public UserLoginRes(String token) {
        this.token = token;
    }
}
