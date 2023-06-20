package ru.skillbox.snbot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.api.CommonRs;
import ru.skillbox.snbot.api.PostRq;
import ru.skillbox.snbot.handlers.Client;
import ru.skillbox.snbot.session.SessionStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
public class ProcessCreatePostTagsCommand implements ICommand {
    private final String tags;
    private final Client client;

    @Override
    public SendMessage getMessage(Update update) {
        long telegramId = update.getMessage().getFrom().getId();
        List<String> tagsList;

        if (tags.trim().equals("-")) {
            tagsList = new ArrayList<>();
        } else {
            tagsList = Arrays.asList(tags.trim().split(","));
            tagsList.replaceAll(t -> t.startsWith("#") ? t.trim() : "#" + t.trim());
        }

        PostRq request = PostRq.builder()
            .title(SessionStorage.getParam(telegramId, "postTitle"))
            .postText(SessionStorage.getParam(telegramId, "postText"))
            .tags(tagsList)
            .build();

        try {
            CommonRs<?> response = client.createPost(telegramId, request);

            if (response == null) {
                clear(telegramId);
                return Messages.siteUnavailable(update.getMessage());
            }

            clear(telegramId);

            return Messages.createPostSuccess(update.getMessage());
        } catch (ExecutionException | IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        clear(telegramId);

        return Messages.createPostFail(update.getMessage());
    }

    private void clear(long telegramId) {
        SessionStorage.removeParam(telegramId, "postTitle");
        SessionStorage.removeParam(telegramId, "postText");
    }
}
