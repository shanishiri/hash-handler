package com.soliduslabs.controller;

import com.soliduslabs.dao.DynamoDbClient;
import com.soliduslabs.model.HashMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class HashController {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity getHashByString(@RequestBody HashMessage hashMessage) {
        String message = hashMessage.getMessage();
        try {
            String hex = DigestUtils.sha256Hex(message);
            hashMessage.setHashKey(hex);
            dynamoDbClient.saveNewHashKey(hashMessage);
            return new ResponseEntity<>(hex, HttpStatus.OK);
        } catch (Exception e) {
            String responseMessage = "Could not get hash from " + message + " ,error: " + e;
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{hash}")
    public ResponseEntity getMessageByHashKey(@PathVariable("hash") String hashKey) {
        HashMessage hashMessage = dynamoDbClient.getStringByHashKey(hashKey);
        try {
            return new ResponseEntity<>(hashMessage.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            String responseMessage = "Could not find message for key: " + hashKey + " ,error: " + e;
            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }
}
