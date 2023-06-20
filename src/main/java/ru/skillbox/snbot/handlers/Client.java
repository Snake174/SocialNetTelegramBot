package ru.skillbox.snbot.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.snbot.api.*;
import ru.skillbox.snbot.configuration.BotConfiguration;
import ru.skillbox.snbot.enums.ECommands;
import ru.skillbox.snbot.session.SessionStorage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class Client {
    private final BotConfiguration botConfiguration;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "authorization";

    public TgApiResponse send(TgApiRequest request)
        throws ExecutionException, InterruptedException, IOException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String content = ow.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(botConfiguration.getApi() + "tg"))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(content))
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), TgApiResponse.class);
    }

    public RegisterRs sendResetPassword(long telegramId, PasswordSetRq request)
        throws ExecutionException, InterruptedException, IOException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String content = ow.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(botConfiguration.getApi() + "account/password/reset"))
            .header(AUTHORIZATION, SessionStorage.getToken(telegramId))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .PUT(HttpRequest.BodyPublishers.ofString(content))
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), RegisterRs.class);
    }

    public CommonRs<?> getFeeds(long telegramId, int offset, int perPage)
        throws ExecutionException, InterruptedException, IOException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(
                URI.create(botConfiguration.getApi() + "feeds?offset=" + offset + "&perPage=" + perPage)
            )
            .header(AUTHORIZATION, SessionStorage.getToken(telegramId))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), CommonRs.class);
    }

    public CommonRs<?> getFriendsInReq(long telegramId, int offset, int perPage)
        throws ExecutionException, InterruptedException, IOException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(
                URI.create(botConfiguration.getApi() + "friends/request?offset=" + offset + "&perPage=" + perPage)
            )
            .header(AUTHORIZATION, SessionStorage.getToken(telegramId))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), CommonRs.class);
    }

    public CommonRs<?> createPost(long telegramId, PostRq request)
        throws IOException, ExecutionException, InterruptedException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String content = ow.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder(
                URI.create(botConfiguration.getApi() + "users/" + SessionStorage.getUserId(telegramId) + "/wall")
            )
            .header(AUTHORIZATION, SessionStorage.getToken(telegramId))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(content))
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), CommonRs.class);
    }

    public CommonRs<?> acceptFriendRequest(long telegramId, long userId)
        throws IOException, ExecutionException, InterruptedException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(botConfiguration.getApi() + "friends/request/" + userId))
            .header(AUTHORIZATION, SessionStorage.getToken(telegramId))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), CommonRs.class);
    }

    public CommonRs<?> declineFriendRequest(long telegramId, long userId)
        throws IOException, ExecutionException, InterruptedException
    {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(botConfiguration.getApi() + "friends/request/" + userId))
            .header(AUTHORIZATION, SessionStorage.getToken(telegramId))
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .DELETE()
            .build();

        HttpResponse<?> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get();

        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(httpResponse.body().toString(), CommonRs.class);
    }

    public CommonRs<?> getMessages(Long telegramId, int offset, int perPage)
        throws IOException, ExecutionException, InterruptedException
    {
        long userId = SessionStorage.getUserId(telegramId);

        TgApiRequest request = TgApiRequest.builder()
            .id(userId)
            .command(ECommands.MESSAGES.getCommand())
            .data(offset + ";" + perPage)
            .build();

        TgApiResponse response = send(request);

        if (response.getStatus().equals("fail")) {
            return null;
        }

        return new ObjectMapper().readValue(response.getData(), CommonRs.class);
    }
}
