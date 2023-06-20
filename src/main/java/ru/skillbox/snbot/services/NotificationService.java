package ru.skillbox.snbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.skillbox.snbot.bot.SNBot;
import ru.skillbox.snbot.enums.ENotificationsType;
import ru.skillbox.snbot.session.SessionStorage;
import socialnet.api.request.KafkaMessageRq;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SNBot snBot;
    private static final Map<String, String> NOTIFICATIONS = new HashMap<>() {{
        put(ENotificationsType.POST.name(), "\uD83D\uDCA1 %s умную мысль высказал, зацени.\n");
        put(ENotificationsType.POST_COMMENT.name(), "\uD83D\uDCAC %s прокомментировал вашу умную мысль.\n");
        put(ENotificationsType.FRIEND_BIRTHDAY.name(), "\uD83C\uDF7B %s родиться в этот замечательный день посмел. Зайди и поздравь его, что ли.\n");
        put(ENotificationsType.POST_LIKE.name(), "❤\uFE0F %s заценил вашу умную мысль.\n");
        put(ENotificationsType.FRIEND_REQUEST.name(), "\uD83D\uDC4B %s дружить с вами желает.\n");
        put(ENotificationsType.MESSAGE.name(), "\uD83D\uDD4A\uFE0F %s шлёт вам благую весть. Или не очень. В общем, зайди и прочти.\n");
        put(ENotificationsType.COMMENT_COMMENT.name(), "\uD83D\uDCAD %s великий комментатор комментариев к комментарию для комментария.\n");
    }};

    @KafkaListener(
        topics = "${spring.kafka.consumer.topic-name}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory")
    public void messageListener(@Payload KafkaMessageRq message) {
        try {
            long tgId = message.getToTgId();
            String notifications = SessionStorage.getParam(tgId, "notifications");

            if (notifications.equals("0")) {
                return;
            }

            long chatId = Long.parseLong(SessionStorage.getParam(tgId, "chatId"));
            sendNotification(String.format(NOTIFICATIONS.get(message.getType()), message.getFrom()), chatId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void sendNotification(String message, long chatId) throws TelegramApiException {
        snBot.execute(
            SendMessage.builder()
                .text(message)
                .chatId(chatId)
                .build()
        );
    }
}
