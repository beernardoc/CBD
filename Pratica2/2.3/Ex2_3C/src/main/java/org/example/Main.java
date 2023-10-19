package org.example;


import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class Main {

    static MongoClient mongoClient;
    static MongoDatabase database;
    public static void main(String[] args) {

        MongoCollection<Document> db = mongoConnect();

        // 11. Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo "American" ou "Chinese".
        System.out.println("11. Liste o nome, a localidade e a gastronomia dos restaurantes que pertencem ao Bronx e cuja gastronomia é do tipo \"American\" ou \"Chinese\".");





    }

    public static MongoCollection<Document> mongoConnect(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Acesse o banco de dados
        database = mongoClient.getDatabase("cbd");

        return database.getCollection("restaurants");
    }



}