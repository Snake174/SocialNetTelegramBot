package ru.skillbox.snbot.answers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.skillbox.snbot.api.*;
import ru.skillbox.snbot.enums.ECommands;
import ru.skillbox.snbot.enums.EEntity;
import ru.skillbox.snbot.enums.EMode;
import ru.skillbox.snbot.session.SessionStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.InlineKeyboardMarkupBuilder;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.builder;

public class Messages {
    private Messages() {
        throw new IllegalStateException("Messages class");
    }

    public static SendMessage unknownCommand(Message message) {
        return SendMessage.builder()
            .text("Неизвестная команда")
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage invalidEmail(Message message) {
        return SendMessage.builder()
            .text("Ну что за почта такая? Давай ещё раз!")
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage siteUnavailable(Message message) {
        return SendMessage.builder()
            .text("Похоже, сайту хана. Ждите, мы вам перезвоним...")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.startKeyboard())
            .build();
    }

    public static SendMessage confirmEmail(Message message, TgApiResponse response) {
        return SendMessage.builder()
            .text(response.getData())
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.confirmRegisterKeyboard())
            .build();
    }

    public static SendMessage startMessage(Message message) {
        return SendMessage.builder()
            .text("Доступные команды:")
            .replyMarkup(Keyboards.startKeyboard())
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage somethingWrong(Message message) {
        return SendMessage.builder()
            .text("Что-то не то!!!")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.startKeyboard())
            .build();
    }

    public static SendMessage failEmail(Message message, TgApiResponse response) {
        return SendMessage.builder()
            .text(response.getError())
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage successEmail(Message message, TgApiResponse response) {
        JSONObject jo = new JSONObject(response.getData());
        long telegramId = Long.parseLong(jo.get("id").toString());

        SessionStorage.setToken(telegramId, jo.get("token").toString());
        SessionStorage.setUserId(telegramId, Long.parseLong(jo.get("userId").toString()));
        SessionStorage.setParam(telegramId, "notifications", "1");
        SessionStorage.setParam(telegramId, "chatId", String.valueOf(message.getChatId()));

        return SendMessage.builder()
            .text(jo.get("name").toString())
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage confirmKillyourself(Message message) {
        return SendMessage.builder()
            .text("Куда-то собрался?")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.confirmKillyourselfKeyboard())
            .build();
    }

    public static SendMessage cancelKillyourself(Message message) {
        return SendMessage.builder()
            .text("Ну ладно, сиди пока")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage failKillyourself(Message message, TgApiResponse response) {
        return SendMessage.builder()
            .text(response.getError())
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage successKillyourself(Message message, TgApiResponse response) {
        SessionStorage.remove(message.getFrom().getId());

        return SendMessage.builder()
            .text(response.getData())
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.startKeyboard())
            .build();
    }

    public static SendMessage newPassword(Message message) {
        return SendMessage.builder()
            .text("Введите новый пароль. Пароль должен состоять из латинских букв, цифр и знаков. " +
                "Обязательно содержать одну заглавную букву, одну цифру и состоять из 8 символов.")
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage successResetPassword(Message message) {
        return SendMessage.builder()
            .text("Пароль был изменён. Запиши его на бумажку, а перед прочтением сожги \uD83D\uDE08")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage enableNotifications(Message message) {
        return SendMessage.builder()
            .text("Теребоньки включены! Будь аккуратен и сильно не бойся.")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage disableNotifications(Message message) {
        return SendMessage.builder()
            .text("Теребоньки покинули чат \uD83D\uDE14")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage createPostTitle(Message message) {
        return SendMessage.builder()
            .text("Озаглавь своё творение:")
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage createPostText(Message message) {
        return SendMessage.builder()
            .text("Теперь пора бы написать что-то умное:")
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage createPostTags(Message message) {
        return SendMessage.builder()
            .text("Теги через запятую или '-', если их нет:")
            .chatId(message.getChatId())
            .build();
    }

    public static SendMessage createPostSuccess(Message message) {
        SessionStorage.setMode(message.getFrom().getId(), EMode.COMMAND);

        return SendMessage.builder()
            .text("Ваша умная мысль навеки запечетлена на сайте, да не снизойди до неё кривая рука модератора!")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage createPostFail(Message message) {
        SessionStorage.setMode(message.getFrom().getId(), EMode.COMMAND);

        return SendMessage.builder()
            .text("Что-то не так! Возможно, с сайтом проблема, может мысль кривая. Перекури и попробуй позже.")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage yoooMessage(Message message) {
        return SendMessage.builder()
            .text("Доступные команды:")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage acceptFriendRequestMessage(Message message, String userName) {
        return SendMessage.builder()
            .text("\uD83D\uDE43 " + userName + " схватил вас за руку и вы умчались в закат")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage declineFriendRequestMessage(Message message, String userName) {
        return SendMessage.builder()
            .text("\uD83D\uDDD1\uFE0F " + userName + " прогнан прочь")
            .chatId(message.getChatId())
            .replyMarkup(Keyboards.userKeyboard())
            .build();
    }

    public static SendMessage feedsMessage(Message message, CommonRs<?> response) {
        int total = response.getTotal().intValue() / response.getPerPage();
        int current = response.getOffset() / response.getPerPage() + 1;
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<String> news = new ArrayList<>();
        List<PostRs> posts;

        String newLine = "\n\n";
        String delim = newLine + StringUtils.repeat("=", 20) + newLine;

        try {
            posts = mapper.readValue(
                mapper.writeValueAsString(response.getData()),
                mapper.getTypeFactory().constructCollectionType(List.class, PostRs.class)
            );
        } catch (IOException e) {
            return Messages.somethingWrong(message);
        }

        for (PostRs post : posts) {
            StringBuilder sb = new StringBuilder();
            PersonRs author;

            try {
                author = mapper.readValue(mapper.writeValueAsString(post.getAuthor()), PersonRs.class);
            } catch (IOException e) {
                return Messages.somethingWrong(message);
            }

            ZonedDateTime zdt = ZonedDateTime.parse(post.getTime());
            LocalDateTime ldt = zdt.toLocalDateTime();

            sb.append("[").append(Jsoup.parse(post.getTitle()).text()).append("]").append(newLine);
            sb.append(Jsoup.parse(post.getPostText()).text()).append(newLine);
            sb.append("Автор: ").append(author.getFirstName()).append(" ").append(author.getLastName())
                .append(" / ").append(ldt.format(dateTimeFormatter));

            news.add(sb.toString());
        }

        SendMessage.SendMessageBuilder builder = SendMessage.builder()
            .chatId(message.getChatId())
            .parseMode(ParseMode.HTML);

        if (response.getTotal().intValue() - (response.getOffset() + response.getPerPage()) > response.getPerPage()) {
            builder
                .text("Новости [" + current + " / " + total + "]:" + newLine + StringUtils.join(news, delim))
                .replyMarkup(Keyboards.moreKeyboard(
                    EEntity.FEEDS.name(),
                    response.getOffset() + response.getPerPage(),
                    response.getPerPage())
                );
        } else {
            builder
                .text("Новостей больше нет, но вы держитесь! ✊" + newLine + StringUtils.join(news, delim))
                .replyMarkup(Keyboards.userKeyboard());
        }

        return builder.build();
    }

    public static SendMessage friendsInReqMessage(Message message, CommonRs<?> response) {
        int total = response.getTotal().intValue() / response.getPerPage();
        int current = response.getOffset() / response.getPerPage() + 1;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<PersonRs> friends;

        try {
            friends = mapper.readValue(
                mapper.writeValueAsString(response.getData()),
                mapper.getTypeFactory().constructCollectionType(List.class, PersonRs.class)
            );
        } catch (IOException e) {
            return Messages.somethingWrong(message);
        }

        InlineKeyboardMarkupBuilder kBuilder = builder();

        for (PersonRs friend : friends) {
            String fullName = friend.getFirstName() + " " + friend.getLastName();
            String friendId = friend.getId();

            kBuilder.keyboardRow(List.of(
                InlineKeyboardButton.builder()
                    .text("\uD83D\uDE0F " + fullName)
                    .callbackData(
                        ECommands.ACCEPT_FRIEND_IN_REQ.getCommand() + ";" + friendId + ";" + fullName
                    )
                    .build(),
                InlineKeyboardButton.builder()
                    .text("\uD83E\uDD22 Боже, упаси!")
                    .callbackData(
                        ECommands.DECLINE_FRIEND_IN_REQ.getCommand() + ";" + friendId + ";" + fullName
                    )
                    .build()
            ));
        }

        SendMessage.SendMessageBuilder builder = SendMessage.builder()
            .chatId(message.getChatId())
            .parseMode(ParseMode.HTML);

        if (response.getTotal().intValue() - (response.getOffset() + response.getPerPage()) > response.getPerPage()) {
            builder
                .text("Дружеский пинок \uD83D\uDC62 Принять? [" + current + " / " + total + "]:")
                .replyMarkup(kBuilder.keyboard(Keyboards.moreKeyboard(
                    EEntity.FRIENDS_IN_REQ.name(),
                    response.getOffset() + response.getPerPage(),
                    response.getPerPage()
                ).getKeyboard()).build());
        } else {
            builder
                .text("Больше ты никому не нужен, смирись \uD83D\uDE1E")
                .replyMarkup(kBuilder.keyboard(Keyboards.userKeyboard().getKeyboard()).build());
        }

        return builder.build();
    }

    public static SendMessage messagesMessage(Message message, CommonRs<?> response) {
        int total = response.getTotal().intValue() / response.getPerPage();
        int current = response.getOffset() / response.getPerPage() + 1;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<String> unreadedMessages = new ArrayList<>();
        List<TgMessagesRs> messages;

        String newLine = "\n\n";
        String delim = newLine + StringUtils.repeat("=", 20) + newLine;

        try {
            messages = mapper.readValue(
                mapper.writeValueAsString(response.getData()),
                mapper.getTypeFactory().constructCollectionType(List.class, TgMessagesRs.class)
            );
        } catch (IOException e) {
            return Messages.somethingWrong(message);
        }

        for (TgMessagesRs msg : messages) {
            unreadedMessages.add(
                "\uD83D\uDE43 " + msg.getFrom() + newLine + Jsoup.parse(msg.getMessage()).text()
            );
        }

        SendMessage.SendMessageBuilder builder = SendMessage.builder()
            .chatId(message.getChatId())
            .parseMode(ParseMode.HTML);

        if (response.getTotal().intValue() - (response.getOffset() + response.getPerPage()) > response.getPerPage()) {
            builder
                .text("Шептунчики [" + current + " / " + total + ":" + newLine + StringUtils.join(unreadedMessages, delim))
                .replyMarkup(Keyboards.moreKeyboard(
                    EEntity.MESSAGES.name(),
                    response.getOffset() + response.getPerPage(),
                    response.getPerPage()
                ));
        } else {
            builder
                .text("Полковнику никто не пишет, а-а-а-а-а-а-а!!!" + newLine + StringUtils.join(unreadedMessages, delim))
                .replyMarkup(Keyboards.userKeyboard());
        }

        return builder.build();
    }
}
