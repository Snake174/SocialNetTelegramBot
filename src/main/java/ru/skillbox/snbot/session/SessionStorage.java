package ru.skillbox.snbot.session;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import ru.skillbox.snbot.enums.EMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SessionStorage {
    private static final String HOST = "81.177.6.228";
    private static final int PORT = 6379;
    private static final String USER = "default";
    private static final String PASSWORD = "ReDiSsUpErPaSsWoRd";

    private SessionStorage() {
        throw new IllegalStateException("SessionStorage class");
    }

    public static String getToken(long telegramId) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            return jedis.hgetAll("tg" + telegramId).get("token");
        }
    }

    public static void setToken(long telegramId, String token) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            Map<String, String> hash = jedis.hgetAll("tg" + telegramId);
            hash.put("token", token);
            jedis.hset("tg" + telegramId, hash);
        }
    }

    public static EMode getMode(long telegramId) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            return EMode.valueOf(jedis.hgetAll("tg" + telegramId).get("mode"));
        }
    }

    public static void setMode(long telegramId, EMode mode) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            Map<String, String> hash = jedis.hgetAll("tg" + telegramId);
            hash.put("mode", mode.name());
            jedis.hset("tg" + telegramId, hash);
        }
    }

    public static long getUserId(long telegramId) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            return Long.parseLong(jedis.hgetAll("tg" + telegramId).get("userId"));
        }
    }

    public static void setUserId(long telegramId, long userId) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            Map<String, String> hash = jedis.hgetAll("tg" + telegramId);
            hash.put("userId", String.valueOf(userId));
            jedis.hset("tg" + telegramId, hash);
        }
    }

    public static String getParam(long telegramId, String param) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            return jedis.hgetAll("tg" + telegramId).get(param);
        }
    }

    public static void setParam(long telegramId, String param, String value) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            Map<String, String> hash = jedis.hgetAll("tg" + telegramId);
            hash.put(param, value);
            jedis.hset("tg" + telegramId, hash);
        }
    }

    public static void removeParam(long telegramId, String param) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            jedis.hdel("tg" + telegramId, param);
        }
    }

    public static List<String> usersWithNotifications() {
        List<String> result = new ArrayList<>();

        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            ScanParams scanParam = new ScanParams();
            scanParam.match("tg*");
            String cursor = ScanParams.SCAN_POINTER_START;

            do {
                ScanResult<String> scan = jedis.scan(cursor, scanParam);

                scan.getResult().forEach(k -> {
                    try {
                        long telegramId = Long.parseLong(k.substring(2));
                        long userId = getUserId(telegramId);
                        String notifications = getParam(telegramId, "notifications");

                        if (notifications.equals("1")) {
                            result.add(String.valueOf(userId));
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });

                cursor = scan.getCursor();
            } while(!cursor.equals(ScanParams.SCAN_POINTER_START));
        }

        return result;
    }

    public static long getTelegramIdByUserId(long userId) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            ScanParams scanParam = new ScanParams();
            scanParam.match("tg*");
            String cursor = ScanParams.SCAN_POINTER_START;

            do {
                ScanResult<String> scan = jedis.scan(cursor, scanParam);

                for (String k : scan.getResult()) {
                    try {
                        long telegramId = Long.parseLong(k.substring(2));

                        if (userId == getUserId(telegramId)) {
                            return telegramId;
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

                cursor = scan.getCursor();
            } while(!cursor.equals(ScanParams.SCAN_POINTER_START));
        }

        return 0;
    }

    public static void remove(long telegramId) {
        try (JedisPooled jedis = new JedisPooled(HOST, PORT, USER, PASSWORD)) {
            jedis.hgetAll("tg" + telegramId).forEach((k, v) -> jedis.hdel("tg" + telegramId, k));
        }
    }
}
