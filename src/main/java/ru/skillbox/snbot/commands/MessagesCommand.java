package ru.skillbox.snbot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.api.CommonRs;
import ru.skillbox.snbot.handlers.Client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
public class MessagesCommand implements ICommand {
    private final Client client;
    private final int offset;
    private final int perPage;

    @Override
    public SendMessage getMessage(Update update) {
        try {
            CommonRs<?> response = client.getMessages(update.getCallbackQuery().getFrom().getId(), offset, perPage);

            if (response == null) {
                return Messages.siteUnavailable(update.getCallbackQuery().getMessage());
            }

            return Messages.messagesMessage(update.getCallbackQuery().getMessage(), response);
        } catch (ExecutionException | IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        return Messages.somethingWrong(update.getCallbackQuery().getMessage());
    }
}
