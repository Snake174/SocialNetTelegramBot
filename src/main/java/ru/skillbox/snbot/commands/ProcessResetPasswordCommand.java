package ru.skillbox.snbot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.answers.Messages;
import ru.skillbox.snbot.api.PasswordSetRq;
import ru.skillbox.snbot.api.RegisterRs;
import ru.skillbox.snbot.enums.EMode;
import ru.skillbox.snbot.handlers.Client;
import ru.skillbox.snbot.session.SessionStorage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
public class ProcessResetPasswordCommand implements ICommand {
    private final String password;
    private final Client client;

    @Override
    public SendMessage getMessage(Update update) {
        PasswordSetRq request = PasswordSetRq.builder()
            .password(password)
            .build();

        try {
            RegisterRs response = client.sendResetPassword(
                update.getMessage().getFrom().getId(),
                request
            );

            if (response == null) {
                return Messages.siteUnavailable(update.getMessage());
            }

            SessionStorage.setMode(update.getMessage().getFrom().getId(), EMode.COMMAND);

            return Messages.successResetPassword(update.getMessage());
        } catch (ExecutionException | IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        return Messages.somethingWrong(update.getMessage());
    }
}
