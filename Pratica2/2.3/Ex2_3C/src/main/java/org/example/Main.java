package org.example;


import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static MongoClient mongoClient;
    static MongoDatabase database;
    public static void main(String[] args) {

        MongoCollection<Document> db = mongoConnect();

        System.out.println();
        // 11. Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo "American" ou "Chinese".
        System.out.println("11. Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo \"American\" ou \"Chinese\".");
        Document query11 = new Document("localidade", "Bronx")
                .append("$or", Arrays.asList(
                        new Document("gastronomia", "American"),
                        new Document("gastronomia", "Chinese")
                ));

        for (Document document : db.find(query11).projection(new Document("nome", 1).append("localidade", 1).append("gastronomia", 1))) {
            System.out.println(document.toJson());
        }



        System.out.println();
        // 10. Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome começam por "Wil".
        System.out.println("10. Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome começam por \"Wil\".");
        Document query10 = new Document("nome", new Document("$regex", "^Wil"));

        for (Document document : db.find(query10).projection(new Document("restaurant_id", 1).append("nome", 1).append("localidade", 1).append("gastronomia", 1))) {
            System.out.println(document.toJson());
        }



        System.out.println();
        // 7. Encontre os restaurantes que obtiveram uma ou mais pontuações (score) entre[80 e 100].
        System.out.println("7. Encontre os restaurantes que obtiveram uma ou mais pontuações (score) entre[80 e 100].");
        Document query7 = new Document("grades", new Document("$elemMatch", new Document("score",
                new Document("$gte", 80).append("$lte", 100))));

        for (Document document : db.find(query7)) {
            System.out.println(document.toJson());
        }



        System.out.println();
        // 13. Liste o nome, a localidade, o score e gastronomia dos restaurantes que alcançaram sempre pontuações inferiores ou igual a 3.
        System.out.println("13. Liste o nome, a localidade, o score e gastronomia dos restaurantes que alcançaram sempre pontuações inferiores ou igual a 3.");
        Document query = new Document("grades.score", new Document("$not", new Document("$gt", 3)));

        for (Document document : db.find(query).projection(new Document("_id", 0).append("nome", 1).append("localidade", 1).append("gastronomia", 1))) {
            System.out.println(document.toJson());
        }


        System.out.println();
        // 23. Indique os restaurantes que têm gastronomia "Portuguese", o somatório de score é superior a 50 e estão numa latitude inferior a -60.
        System.out.println("23. Indique os restaurantes que têm gastronomia \"Portuguese\", o somatório de score é superior a 50 e estão numa latitude inferior a -60.");
        Document addFields = new Document("$addFields", new Document("total_score", new Document("$sum", "$grades.score")));

        Document filter = new Document("$match", Filters.and(
                Filters.gt("total_score", 50),
                Filters.eq("gastronomia", "Portuguese"),
                Filters.lt("address.coord.0", -60)
        ));

        // executa a agregação
        db.aggregate(Arrays.asList(addFields, filter)).forEach((Document document) -> {
            System.out.println(document.toJson());
        });





    }

    public static MongoCollection<Document> mongoConnect(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Acesse o banco de dados
        database = mongoClient.getDatabase("cbd");

        return database.getCollection("restaurants");
    }



}