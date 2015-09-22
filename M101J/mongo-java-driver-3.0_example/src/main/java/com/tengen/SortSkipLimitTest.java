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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SortSkipLimitTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        try {
            MongoDatabase db = client.getDatabase("course");
            MongoCollection<Document> lines = db.getCollection("SortSkipLimitTest");

            lines.drop();
            Random rand = new Random();

            // insert 10 lines with random start and end points
            for (int i = 0; i < 10; i++) {
                lines.insertOne(
                        new Document("_id", i)
                                .append("start",
                                        new Document("x", rand.nextInt(2))
                                                .append("y", rand.nextInt(90) + 10)
                                )
                                .append("end",
                                        new Document("x", rand.nextInt(2))
                                                .append("y", rand.nextInt(90) + 10)
                                )
                );
            }

            FindIterable<Document> results = lines.find()
                    .sort(new BasicDBObject("start.x", 1).append("start.y", -1))
                    .skip(2).limit(5);

//            Iterator itr = results.iterator();
//
//
//                while (itr.hasNext()) {
//                    Document doc  = (Document) itr.next();
//                    System.out.println(doc);
//                }

            // or

            List<Document> docs = results.into(new ArrayList<Document>());

            int ctr=0;
            for(Document doc:docs){
                System.out.println("Document # "+ ctr++);
                Helper.prettyPrintJSON(doc);
            }

            System.out.println("Total docs "+ctr);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
