package com.soliduslabs.dao;

import com.soliduslabs.model.HashMessage;

public interface DynamoDbClient {
    HashMessage getStringByHashKey(String hashKey);
    HashMessage saveNewHashKey(HashMessage hashMessage);
}
