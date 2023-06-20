package ru.skillbox.snbot.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRs {
    private PersonRs author;

    @JsonIgnore
    private List<String> comments;

    @JsonIgnore
    private Long id;

    @JsonIgnore
    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonIgnore
    private Integer likes;

    @JsonIgnore
    @JsonProperty("my_like")
    private Boolean myLike;

    @JsonProperty("post_text")
    private String postText;

    private List<String> tags;

    private String time;

    private String title;

    @JsonIgnore
    private String type;
}
