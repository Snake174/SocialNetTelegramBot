package ru.skillbox.snbot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.api.TgApiRequest;
import ru.skillbox.snbot.api.TgApiResponse;
import ru.skillbox.snbot.enums.ECommands;
import ru.skillbox.snbot.handlers.Client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
public class ProcessEmailCommand implements ICommand {
    private final String email;
    private final Client client;

    @Override
    public SendMessage getMessage(Update update) {
        boolean isEmailValid = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
            .matcher(email)
            .matches();

        if (!isEmailValid) {
            return Messages.invalidEmail(update.getMessage());
        }

        TgApiRequest request = TgApiRequest.builder()
            .id(update.getMessage().getFrom().getId())
            .command(ECommands.REGISTER.getCommand())
            .data(email)
            .build();

        try {
            TgApiResponse response = client.send(request);

            if (response == null) {
                return Messages.siteUnavailable(update.getMessage());
            }

            return Messages.confirmEmail(update.getMessage(), response);
        } catch (ExecutionException | IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        return Messages.somethingWrong(update.getMessage());
    }
}
