package ru.skillbox.snbot.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRs {
    @JsonIgnore
    private String about;

    @JsonIgnore
    private String birthDate;

    @JsonIgnore
    private String city;

    @JsonIgnore
    private String country;

    @JsonIgnore
    private String currency;

    @JsonIgnore
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonIgnore
    private String friendStatus;

    private String id;

    @JsonIgnore
    private String isBlocked;

    @JsonIgnore
    private String isBlockedByCurrentUser;

    @JsonProperty("last_name")
    private String lastName;

    @JsonIgnore
    private String lastOnlineTime;

    @JsonIgnore
    private String messagesPermission;

    @JsonIgnore
    private String online;

    @JsonIgnore
    private String phone;

    @JsonIgnore
    private String photo;

    @JsonIgnore
    private String regDate;

    @JsonIgnore
    private String token;

    @JsonIgnore
    private String userDeleted;

    @JsonIgnore
    private String weather;
}
