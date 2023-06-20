package ru.skillbox.snbot.commands;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.enums.EMode;
import ru.skillbox.snbot.session.SessionStorage;

@RequiredArgsConstructor
public class ProcessCreatePostTitleCommand implements ICommand {
    private final String title;

    @Override
    public SendMessage getMessage(Update update) {
        SessionStorage.setParam(update.getMessage().getFrom().getId(), "postTitle", title.trim());
        SessionStorage.setMode(update.getMessage().getFrom().getId(), EMode.CREATE_POST_TEXT);

        return Messages.createPostText(update.getMessage());
    }
}
