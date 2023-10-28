package com.example.demo.service;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.example.demo.db.Lyric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AwsDynamoDbService {

    private final AmazonDynamoDBClient amazonDynamoDBClient;

    @Autowired
    public AwsDynamoDbService(AmazonDynamoDBClient amazonDynamoDBClient) {
        this.amazonDynamoDBClient = amazonDynamoDBClient;
    }

    public void createItem(String id, String _lyric, String chat, Map<String,String> map) throws SdkBaseException {
        try{
            Lyric lyric = new Lyric(id, _lyric, chat,map);

            // Save Lyric To DynamoDB
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
            mapper.save(lyric);

        } catch(Exception e){
            System.out.println("e:: " + e);;
        }
    }
}