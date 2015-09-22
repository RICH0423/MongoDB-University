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
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Iterator;

public class FindAndModifyTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoCollection<Document> collection = createCollection();
        collection.drop();

        final String counterId = "abc";
        int first;
        int numNeeded;

        numNeeded = 2;
        first = getRange(counterId, numNeeded, collection);
        System.out.println("Range: " + first + "-" + (first + numNeeded - 1));

        numNeeded = 3;
        first = getRange(counterId, numNeeded, collection);
        System.out.println("Range: " + first + "-" + (first + numNeeded - 1));

        numNeeded = 10;
        first = getRange(counterId, numNeeded, collection);
        System.out.println("Range: " + first + "-" + (first + numNeeded - 1));

        System.out.println();

        printCollection(collection);
    }

    private static int getRange(String id, int range, MongoCollection<Document> collection) {
       FindOneAndUpdateOptions uOpt = new FindOneAndUpdateOptions();
        uOpt.upsert(true);
        
        
        Document doc = collection.findOneAndUpdate(
                new Document("_id", id),
                new Document("$inc", new Document("counter", range)),uOpt);
        return (Integer) doc.get("counter") - range + 1;
   }

    private static MongoCollection<Document> createCollection() throws UnknownHostException {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        return db.getCollection("FindModifyTest");
    }

    private static void printCollection(final MongoCollection<Document> collection) {
        Iterator cursor = collection.find().sort(new Document("_id", 1)).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } catch(Exception e) {
            e.printStackTrace();

        }

    }
}
