package ru.skillbox.snbot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.configuration.BotConfiguration;
import ru.skillbox.snbot.handlers.CallbackQueryHandler;
import ru.skillbox.snbot.handlers.MessageHandler;

@Component
@RequiredArgsConstructor
public class SNBot extends TelegramLongPollingBot {
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final BotConfiguration botConfiguration;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                execute(callbackQueryHandler.processCallbackQuery(update));
            } else {
                if (update.hasMessage()) {
                    execute(messageHandler.processMessage(update));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }

    /**
     * @deprecated (when, why, refactoring advice...)
     */
    @Deprecated(since="4.2", forRemoval=true)
    @Override
    public String getBotToken() {
        return botConfiguration.getToken();
    }
}
