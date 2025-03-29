package com.plainprog.auth_session_redis.service;

import com.plainprog.auth_session_redis.model.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import redis.clients.jedis.Jedis;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;

@Service
public class SessionExplorerService {
    @Autowired
    RedisSerializer serializer;

    public SessionData getSessionData(String sessionId) {
        String redisKey = "spring:session:sessions:" + sessionId;
        Jedis jedis = new Jedis("localhost", 5050);  // Ensure the host and port match your Redis setup
        Map<byte[], byte[]> sessionDataByte = jedis.hgetAll(redisKey.getBytes());
        jedis.close();

        SessionData sessionData = new SessionData();
        for (Map.Entry<byte[], byte[]> entry : sessionDataByte.entrySet()) {
            String field = new String(entry.getKey());
            byte[] value = entry.getValue();

            try {
                if (field.equals("sessionAttr:data")) {
                    Object data = serializer.deserialize(value);
                    sessionData.setData(data);
                }
                else if (field.equals("creationTime")) {
                    Object creationTime = serializer.deserialize(value);
                    if (creationTime instanceof Long) {
                        long creationTimeLong = (long) creationTime;
                        Instant instant = Instant.ofEpochMilli(creationTimeLong);
                        sessionData.setCreationTime(instant);
                    }
                }
                else if (field.equals("lastAccessedTime")) {
                    Object lastAccessedTime = serializer.deserialize(value);
                    if (lastAccessedTime instanceof Long) {
                        long lastAccessedTimeLong = (long) lastAccessedTime;
                        Instant instant = Instant.ofEpochMilli(lastAccessedTimeLong);
                        sessionData.setLastAccessedTime(instant);
                    }
                } else if (field.equals("sessionAttr:SPRING_SECURITY_CONTEXT")) {
                    Object context = serializer.deserialize(value);
                    if (context instanceof SecurityContextImpl) {
                        Principal principal = ((SecurityContextImpl) context).getAuthentication();
                        sessionData.setPrincipal(principal);
                    }
                } else if (field.equals("maxInactiveInterval")) {
                    Object maxInactiveInterval = serializer.deserialize(value);
                    if (maxInactiveInterval instanceof Integer) {
                        sessionData.setMaxInactiveInterval((int) maxInactiveInterval);
                    }
                }
            } catch (Exception e) {
                System.out.println(field + " : " + "Could not deserialize");
                e.printStackTrace();
            }
        }
        sessionData.setSessionId(sessionId);
        return sessionData;
    }

}
