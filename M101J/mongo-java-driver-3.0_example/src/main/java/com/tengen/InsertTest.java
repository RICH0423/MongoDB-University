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
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class InsertTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        try {

            MongoDatabase courseDB = client.getDatabase("course");
            MongoCollection<Document> collection = courseDB.getCollection("insertTest");

            collection.drop();

            Document doc = new Document("x",1).append("y",2);

            collection.insertOne(doc);

            ArrayList<Document> docs = collection.find().into(new ArrayList<Document>());

            for(Document d:docs){
                System.out.println(d);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
