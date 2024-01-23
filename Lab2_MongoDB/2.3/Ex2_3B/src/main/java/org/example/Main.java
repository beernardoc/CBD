package org.example;


import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.util.Scanner;

public class Main {

    static MongoClient mongoClient;
    static MongoDatabase database;
    public static void main(String[] args) {

        MongoCollection<Document> db = mongoConnect();



        System.out.println("Exercício B: ");
        System.out.println();



        IndexOptions indexOptionsLocalidade = new IndexOptions().name("localidade_index");
        db.createIndex(new Document("localidade", 1), indexOptionsLocalidade);

        IndexOptions indexOptionsGastronomia = new IndexOptions().name("gastronomia_index");
        db.createIndex(new Document("gastronomia", 1), indexOptionsGastronomia);

        IndexOptions indexOptionsName = new IndexOptions().name("nome_text_index");
        db.createIndex(new Document("nome", "text"), indexOptionsName);

q
        System.out.println("Localidade: ");
        Document filtroLocalidade = new Document("localidade", "Bronx");
        Find(db, filtroLocalidade);
        System.out.println();

        System.out.println("Gastronomia: ");
        Document filtroGastronomia = new Document("gastronomia", "Bakery");
        Find(db, filtroGastronomia);
        System.out.println();

        System.out.println("Nome: ");
        Document filtroName = new Document("nome", "Wild Asia");
        Find(db, filtroName);
        System.out.println();




    }

    public static MongoCollection<Document> mongoConnect(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Acesse o banco de dados
        database = mongoClient.getDatabase("cbd");

        return database.getCollection("restaurants");
    }

    public static void Find(MongoCollection<Document> collection, Document filtro){
        for (Document document : collection.find(filtro)) {
            System.out.println(document.toJson());
        }
    }





}