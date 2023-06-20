package ru.skillbox.snbot.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TgApiRequest {
    private long id;
    private String command;
    private String data;
}
