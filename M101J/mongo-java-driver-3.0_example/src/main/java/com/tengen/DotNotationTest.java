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
import static com.mongodb.client.model.Filters.*;

import static com.mongodb.client.model.Projections.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DotNotationTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> lines = db.getCollection("DotNotationTest");
        lines.drop();
        Random rand = new Random();

        // insert 10 lines with random start and end points
        for (int i = 0; i < 10; i++) {
            lines.insertOne(
                    new Document("_id", i)
                            .append("start",
                                    new Document("x", rand.nextInt(90) + 10)
                                            .append("y", rand.nextInt(90) + 10)
                            )
                            .append("end",
                                    new Document("x", rand.nextInt(90) + 10)
                                            .append("y", rand.nextInt(90) + 10)
                            )
            );
        }

        System.out.println(lines.count());
        ArrayList<Document>docs = lines.find().into(new ArrayList<Document>());
        for(Document doc:docs){
            System.out.println(doc);
        }

        lines.updateOne(new Document("_id",5),
                new Document("$set",
                        new Document("start.x",0)));

        docs = lines.find().into(new ArrayList<Document>());

        System.out.println(" After update");

        for(Document doc:docs){
            System.out.println(doc);
        }


        Bson filterDoc = new Document("start.x",
                new Document("$gt",50));

        //or
        Bson filterBson = gt("starts.x",50);


        Bson projectionDoc = new Document("_id",0)
                .append("start.y", true);

        Bson projectionBson = fields(excludeId(),
                include("start.x"));


        List<Document> docsFiltered = lines.find(filterDoc).projection(projectionBson).into(new ArrayList<Document>());

       for(Document doc:docsFiltered){
           Helper.prettyPrintJSON(doc);
       }
    }
}
