package org.example;


import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

public class Main {

    static MongoClient mongoClient;
    static MongoDatabase database;
    public static void main(String[] args) {


        MongoCollection<Document> db = mongoConnect();

        System.out.println("A) Número de localidades distintas: " + countLocalidades(db));

        System.out.println();

        System.out.println("B) Numero de restaurantes por localidade: ");
        Map<String, Integer> map = countRestByLocalidade(db);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println();

        System.out.println("C) Nome de restaurantes contendo 'Park' no nome: " );
        List<String> list = getRestWithNameCloserTo("Park", db);
        list.forEach(System.out::println);









    }

    public static MongoCollection<Document> mongoConnect(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("cbd");

        return database.getCollection("restaurants");
    }

    public static int countLocalidades(MongoCollection<Document> db){

        int distinctLocal = db.distinct("localidade", String.class).into(new ArrayList<>()).size();
        return distinctLocal;
    }

   public static Map<String, Integer> countRestByLocalidade(MongoCollection<Document> db){

       Map<String, Integer> maplocal = new HashMap<>();

       Document groupFields = new Document("_id", "$localidade");
       groupFields.append("count", new Document("$sum", 1));
       Document group = new Document("$group", groupFields);

       Document project = new Document("$project",
               new Document("localidade", "$_id")
                       .append("_id", 0)
                       .append("count", 1)
       );


       Iterable<Document> results = db.aggregate(Arrays.asList(group, project));


       for (Document result : results) {
           maplocal.put(result.getString("localidade"), result.getInteger("count"));
       }

       return maplocal;

   }

    public static List<String> getRestWithNameCloserTo(String name, MongoCollection<Document> db){

        List<String> list = new ArrayList<>();

        MongoCursor<Document> cursor = db.find(Filters.regex("nome", name)).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            list.add(doc.getString("nome"));
        }

        return list;



    }



}