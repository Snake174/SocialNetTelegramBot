package ru.skillbox.snbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;

public class CancelLogoutCommand implements ICommand {
    @Override
    public SendMessage getMessage(Update update) {
        return Messages.cancelKillyourself(update.getCallbackQuery().getMessage());
    }
}
