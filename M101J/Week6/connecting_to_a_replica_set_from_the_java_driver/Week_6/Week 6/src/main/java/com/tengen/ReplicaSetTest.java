package com.tengen;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ReplicaSetTest {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));

        DBCollection test = client.getDB("course").getCollection("replica.test");
        test.drop();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            test.insert(new BasicDBObject("_id", i));
            System.out.println("Inserted document: " + i);
            Thread.sleep(500);
        }
    }
}
