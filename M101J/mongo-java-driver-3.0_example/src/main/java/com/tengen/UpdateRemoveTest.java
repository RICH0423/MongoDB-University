/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tengen;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class UpdateRemoveTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoCollection<Document> collection = createCollection();

        List<String> names = Arrays.asList("alice", "bobby", "cathy", "david", "ethan");
        for (String name : names) {
            collection.insertOne(new Document("_id", name));
        }

        // see scratch method
        scratch(collection);

        printCollection(collection);
    }

    // these are all the statement I used throughout the lecture.
    private static void scratch(final MongoCollection<Document> collection) {
//


        collection.updateMany(new Document(),
                new Document("$set",
                new Document("age",null)));

        collection.updateOne(new Document("_id", "alice"),
                new Document("$set", new Document("age", 28)));

        collection.updateOne(new Document("_id", "alice"),
                new Document("$set",new Document("gender", "F")));



        collection.updateOne(new Document("_id", "frank"),

                new Document("$set", new Document("age", 26)));

        UpdateOptions uOpts = new UpdateOptions();
        uOpts.upsert(true);

        collection.updateMany(new Document(),
                new Document("$set", new Document("title", "Dr")), uOpts);

//        collection.replaceOne(Filters.eq("_id", "bobby"),
//                new Document("_id","Robert"));
        // above statement will  not run,
        // do you know why?

        collection.deleteOne(new Document("_id", "frank"));
    }

    private static MongoCollection<Document> createCollection() throws UnknownHostException {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> collection = db.getCollection("UpdateRemoveTest");
        collection.drop();
        return collection;
    }

    private static void printCollection(final MongoCollection<Document> collection) {
        Iterator cursor = collection.find().sort(new Document("_id", 1)).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } catch (Exception e){
            e.printStackTrace();
            
        }

    }
}
