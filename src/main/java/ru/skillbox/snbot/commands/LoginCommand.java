package ru.skillbox.snbot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.api.TgApiRequest;
import ru.skillbox.snbot.api.TgApiResponse;
import ru.skillbox.snbot.enums.ECommands;
import ru.skillbox.snbot.enums.EMode;
import ru.skillbox.snbot.handlers.Client;
import ru.skillbox.snbot.session.SessionStorage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
public class LoginCommand implements ICommand {
    private final Client client;

    @Override
    public SendMessage getMessage(Update update) {
        SessionStorage.setMode(update.getCallbackQuery().getFrom().getId(), EMode.LOGIN);

        TgApiRequest request = TgApiRequest.builder()
            .id(update.getCallbackQuery().getFrom().getId())
            .command(ECommands.LOGIN.getCommand())
            .build();

        try {
            TgApiResponse response = client.send(request);

            if (response == null) {
                return Messages.siteUnavailable(update.getCallbackQuery().getMessage());
            }

            if (response.getStatus().equals("fail")) {
                return Messages.failEmail(update.getCallbackQuery().getMessage(), response);
            }

            SessionStorage.setMode(update.getCallbackQuery().getFrom().getId(), EMode.COMMAND);

            return Messages.successEmail(update.getCallbackQuery().getMessage(), response);
        } catch (ExecutionException | IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        SessionStorage.setMode(update.getCallbackQuery().getFrom().getId(), EMode.LOGIN);

        return Messages.somethingWrong(update.getCallbackQuery().getMessage());
    }
}
