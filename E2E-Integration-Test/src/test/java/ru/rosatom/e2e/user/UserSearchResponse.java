package ru.rosatom.e2e.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class UserSearchResponse {

    @JsonProperty("content")
    private List<User> users;
}
