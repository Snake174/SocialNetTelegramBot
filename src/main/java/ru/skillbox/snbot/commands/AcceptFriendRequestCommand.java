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
public class AcceptFriendRequestCommand implements ICommand {
    private final Client client;
    private final long userId;
    private final String userName;

    @Override
    public SendMessage getMessage(Update update) {
        try {
            CommonRs<?> response = client.acceptFriendRequest(update.getCallbackQuery().getFrom().getId(), userId);

            if (response == null) {
                return Messages.siteUnavailable(update.getCallbackQuery().getMessage());
            }

            return Messages.acceptFriendRequestMessage(update.getCallbackQuery().getMessage(), userName);
        } catch (ExecutionException | IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        return Messages.somethingWrong(update.getCallbackQuery().getMessage());
    }
}
