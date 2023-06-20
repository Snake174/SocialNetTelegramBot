package ru.skillbox.snbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.session.SessionStorage;

public class DisableNotificationsCommand implements ICommand {
    @Override
    public SendMessage getMessage(Update update) {
        SessionStorage.setParam(update.getCallbackQuery().getFrom().getId(), "notifications", "0");

        return Messages.disableNotifications(update.getCallbackQuery().getMessage());
    }
}
