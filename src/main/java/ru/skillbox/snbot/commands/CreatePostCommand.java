package ru.skillbox.snbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.enums.EMode;
import ru.skillbox.snbot.session.SessionStorage;

public class CreatePostCommand implements ICommand {
    @Override
    public SendMessage getMessage(Update update) {
        SessionStorage.setMode(update.getCallbackQuery().getFrom().getId(), EMode.CREATE_POST_TITLE);
        return Messages.createPostTitle(update.getCallbackQuery().getMessage());
    }
}
