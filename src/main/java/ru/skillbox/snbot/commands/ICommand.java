package ru.skillbox.snbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ICommand {
    public SendMessage getMessage(Update update);
}
