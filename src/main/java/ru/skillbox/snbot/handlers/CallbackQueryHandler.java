package ru.skillbox.snbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skillbox.snbot.commands.*;
import ru.skillbox.snbot.enums.ECommands;
import ru.skillbox.snbot.enums.EEntity;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {
    private final Client client;
    private static final int FEEDS_PER_PAGE = 3;
    private static final int FRIENDS_PER_PAGE = 5;
    private static final int MESSAGES_PER_PAGE = 5;

    public synchronized SendMessage processCallbackQuery(Update update) {
        SendMessage sendMessage = getCommandResponse(update);
        sendMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        sendMessage.enableHtml(true);

        return sendMessage;
    }

    private SendMessage getCommandResponse(Update update) {
        final String data = update.getCallbackQuery().getData();

        if (data == null) {
            throw new IllegalArgumentException();
        }

        if (data.equals(ECommands.LOGIN.getCommand())) {
            return new LoginCommand(client).getMessage(update);
        }

        if (data.equals(ECommands.KILLYOURSELF.getCommand())) {
            return new LogoutCommand(client).getMessage(update);
        }

        if (data.equals(ECommands.CONFIRM_KILLYOURSELF.getCommand())) {
            return new ConfirmLogoutCommand().getMessage(update);
        }

        if (data.equals(ECommands.CANCEL_KILLYOURSELF.getCommand())) {
            return new CancelLogoutCommand().getMessage(update);
        }

        if (data.equals(ECommands.RESET_PASSWORD.getCommand())) {
            return new ResetPasswordCommand().getMessage(update);
        }

        if (data.equals(ECommands.FEEDS.getCommand())) {
            return new FeedsCommand(client, 0, FEEDS_PER_PAGE).getMessage(update);
        }

        if (data.equals(ECommands.MESSAGES.getCommand())) {
            return new MessagesCommand(client, 0, MESSAGES_PER_PAGE).getMessage(update);
        }

        if (data.equals(ECommands.FRIENDS_INCOMING_REQUESTS.getCommand())) {
            return new FriendsIncomingRequestCommand(client, 0, FRIENDS_PER_PAGE).getMessage(update);
        }

        if (data.equals(ECommands.CREATE_POST.getCommand())) {
            return new CreatePostCommand().getMessage(update);
        }

        if (data.equals(ECommands.CLOSE.getCommand())) {
            return new CloseCommand().getMessage(update);
        }

        if (data.equals(ECommands.ENABLE_NOTIFICATIONS.getCommand())) {
            return new EnableNotificationsCommand().getMessage(update);
        }

        if (data.equals(ECommands.DISABLE_NOTIFICATIONS.getCommand())) {
            return new DisableNotificationsCommand().getMessage(update);
        }

        if (data.startsWith(ECommands.MORE.getCommand())) {
            String[] params = data.split(";");
            String entity = params[1];
            int offset = Integer.parseInt(params[2]);
            int perPage = Integer.parseInt(params[3]);

            if (entity.equals(EEntity.FEEDS.name())) {
                return new FeedsCommand(client, offset, perPage).getMessage(update);
            }

            if (entity.equals(EEntity.FRIENDS_IN_REQ.name())) {
                return new FriendsIncomingRequestCommand(client, offset, perPage).getMessage(update);
            }

            if (entity.equals(EEntity.MESSAGES.name())) {
                return new MessagesCommand(client, offset, perPage).getMessage(update);
            }
        }

        if (data.startsWith(ECommands.ACCEPT_FRIEND_IN_REQ.getCommand())) {
            String[] params = data.split(";");
            long userId = Long.parseLong(params[1]);
            String userName = params[2];

            return new AcceptFriendRequestCommand(client, userId, userName).getMessage(update);
        }

        if (data.startsWith(ECommands.DECLINE_FRIEND_IN_REQ.getCommand())) {
            String[] params = data.split(";");
            int userId = Integer.parseInt(params[1]);
            String userName = params[2];

            return new DeclineFriendRequestCommand(client, userId, userName).getMessage(update);
        }

        return new UnknownCommand().getMessage(update);
    }
}
