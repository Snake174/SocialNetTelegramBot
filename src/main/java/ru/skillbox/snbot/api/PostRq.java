package ru.skillbox.snbot.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class PostRq {
    @JsonProperty("post_text")
    private String postText;
    private List<String> tags;
    private String title;
}
