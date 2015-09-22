package com.tengen;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

/**
 * Author Mike Clovis
 * Date: 8/25/2015
 * Time: 3:50 PM
 */
public class Helper {
    public static void prettyPrintJSON(final Document doc){
        System.out.println(doc.toJson(
                new JsonWriterSettings(JsonMode.SHELL,true)
        ));
    }
}
