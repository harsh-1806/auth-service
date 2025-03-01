package com.harsh.auth.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsh.auth.eventProducer.UserInfoEvent;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public class UserInfoSerializer implements Serializer<UserInfoEvent> {

    @Override
    public byte[] serialize(String s, UserInfoEvent o) {
        byte[] retVal = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            retVal = objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }

}
