package ru.skillbox.snbot.answers;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.skillbox.snbot.enums.ECommands;

import java.util.List;

public class Keyboards {
    private Keyboards() {
        throw new IllegalStateException("Keyboards class");
    }

    public static InlineKeyboardMarkup startKeyboard() {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83E\uDD1F Войти")
                    .callbackData(ECommands.LOGIN.getCommand())
                    .build(),
                InlineKeyboardButton.builder()
                    .text("✒\uFE0F Регистрация")
                    .url("http://81.177.6.228:8080/registration")
                    .build()
            )).build();
    }

    public static InlineKeyboardMarkup confirmRegisterKeyboard() {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("✔\uFE0F Подтвердить регистрацию")
                    .callbackData(ECommands.LOGIN.getCommand())
                    .build()
            )).build();
    }

    public static InlineKeyboardMarkup userKeyboard() {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDCF0 Что нового?")
                    .callbackData(ECommands.FEEDS.getCommand())
                    .build(),
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDCAC Сумничать")
                    .callbackData(ECommands.CREATE_POST.getCommand())
                    .build()
            ))
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDE4C Дружеские пинки")
                    .callbackData(ECommands.FRIENDS_INCOMING_REQUESTS.getCommand())
                    .build()
            ))
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("🗨️ Сообщения")
                    .callbackData(ECommands.MESSAGES.getCommand())
                    .build()
            ))
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDD14 Теребоньки!!!")
                    .callbackData(ECommands.ENABLE_NOTIFICATIONS.getCommand())
                    .build(),
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDD15 Не шуметь")
                    .callbackData(ECommands.DISABLE_NOTIFICATIONS.getCommand())
                    .build()
            ))
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDDDD\uFE0F Смена пароля")
                    .callbackData(ECommands.RESET_PASSWORD.getCommand())
                    .build(),
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDCA9 Самовыпил")
                    .callbackData(ECommands.CONFIRM_KILLYOURSELF.getCommand())
                    .build()
            )).build();
    }

    public static InlineKeyboardMarkup confirmKillyourselfKeyboard() {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83C\uDFC3 Свалить")
                    .callbackData(ECommands.KILLYOURSELF.getCommand())
                    .build(),
                InlineKeyboardButton.builder()
                    .text("\uD83C\uDF44 Я ещё посижу")
                    .callbackData(ECommands.CANCEL_KILLYOURSELF.getCommand())
                    .build()
            )).build();
    }

    public static InlineKeyboardMarkup moreKeyboard(String entity, int offset, int perPage) {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDD3B Далее")
                    .callbackData(ECommands.MORE.getCommand() + ";" + entity + ";" + offset + ";" + perPage)
                    .build(),
                InlineKeyboardButton.builder()
                    .text("❌ Закрыть")
                    .callbackData(ECommands.CLOSE.getCommand())
                    .build()
            )).build();
    }
}
