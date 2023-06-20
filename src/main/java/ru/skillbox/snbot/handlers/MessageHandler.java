package ru.skillbox.snbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.commands.*;
import ru.skillbox.snbot.enums.ECommands;
import ru.skillbox.snbot.enums.EMode;
import ru.skillbox.snbot.session.SessionStorage;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final Client client;

    public synchronized SendMessage processMessage(Update update) {
        SendMessage sendMessage = getCommandResponse(update);
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.enableHtml(true);
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    private SendMessage getCommandResponse(Update update) {
        String inputText = update.getMessage().getText();
        long userId = update.getMessage().getFrom().getId();

        if (inputText == null) {
            throw new IllegalArgumentException();
        }

        if (inputText.equals(ECommands.START.getCommand())) {
            return new StartCommand().getMessage(update);
        }

        if (SessionStorage.getMode(userId) == EMode.LOGIN) {
            return new ProcessEmailCommand(inputText, client).getMessage(update);
        }

        if (SessionStorage.getMode(userId) == EMode.RESET_PASSWORD) {
            return new ProcessResetPasswordCommand(inputText, client).getMessage(update);
        }

        if (SessionStorage.getMode(userId) == EMode.CREATE_POST_TITLE) {
            return new ProcessCreatePostTitleCommand(inputText).getMessage(update);
        }

        if (SessionStorage.getMode(userId) == EMode.CREATE_POST_TEXT) {
            return new ProcessCreatePostTextCommand(inputText).getMessage(update);
        }

        if (SessionStorage.getMode(userId) == EMode.CREATE_POST_TAGS) {
            return new ProcessCreatePostTagsCommand(inputText, client).getMessage(update);
        }

        return new UnknownCommand().getMessage(update);
    }
}
