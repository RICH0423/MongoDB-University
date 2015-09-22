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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FindCriteriaTest {
    public static void main(String[] args) throws UnknownHostException {
//        MongoClient client = new MongoClient();
//        DB db = client.getDB("course");
//        DBCollection collection = db.getCollection("findCriteriaTest");


        MongoClient client = new MongoClient();

        try {
            MongoDatabase db = client.getDatabase("course");
            MongoCollection<Document> collection = db.getCollection("findCriteriaTest");

            collection.drop();

            // insert 10 documents with two random integers
            for (int i = 0; i < 10; i++) {
                collection.insertOne(
                        new Document("x", new Random().nextInt(2))
                                .append("y", new Random().nextInt(100)));
            }


            Document filterImplicitAnd = new Document("x",0)
                    .append("y", new Document("$gt", 10))
                    .append("y", new Document("$lt", 70));


            // or
            Document longFilter = new Document("x",0);
            List<Document> andVals = new ArrayList<Document>();
            andVals.add(new Document("y", new Document("$gt", 10)));
            andVals.add(new Document("y", new Document("$lt", 70)));

            longFilter.append("$and", andVals);
            //or

            Bson bsonFiler = and(eq("x", 0), gt("y", 10), lt("y", 70));

            System.out.println("\nCount:");
            long count = collection.count(filterImplicitAnd);

            assert collection.count(filterImplicitAnd)==collection.count(longFilter);
            assert collection.count(filterImplicitAnd) == collection.count(bsonFiler);
            System.out.println(count);

            System.out.println("\nFind all: ");
            FindIterable results = collection.find(filterImplicitAnd);
            Iterator itr = results.iterator();

                while (itr.hasNext()) {
                    Document doc = (Document) itr.next();
                    Helper.prettyPrintJSON(doc);
                }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
