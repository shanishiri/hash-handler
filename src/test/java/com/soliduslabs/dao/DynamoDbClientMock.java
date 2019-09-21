package com.soliduslabs.dao;

import com.soliduslabs.model.HashMessage;

import java.util.HashMap;
import java.util.Map;

public class DynamoDbClientMock implements DynamoDbClient {

    private Map<String, String> hashToStringMap = new HashMap<>();


    @Override
    public HashMessage getStringByHashKey(String hashKey) {
        String message = hashToStringMap.get(hashKey);
        return new HashMessage(message,hashKey);
    }

    @Override
    public HashMessage saveNewHashKey(HashMessage hashMessage) {
        hashToStringMap.put(hashMessage.getHashKey(), hashMessage.getMessage());
        return hashMessage;
    }

}
