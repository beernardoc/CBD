package org.example;

import com.mongodb.MongoWriteException;
import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;



public class Manager {

    private final int limit;
    private final int timeslot;

    static MongoCollection<Document> db;

    public Manager(int limit, int timeslot) {
        this.limit = limit;
        this.timeslot = timeslot;
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("cbd"); // cbd é o nome da base de dados
        db = database.getCollection("Orders"); // restaurants é o nome da coleção
    }




    public void addOrder(String username, String product, int quantity) {
        long currentTimestamp = System.currentTimeMillis();

        Document user = db.find(new Document("username", username)).first();
        List<Document> products = user.get("products", List.class);

        long janelatempo = currentTimestamp - (timeslot * 1000);

        // Calcula a quantidade total de produtos pedidos na timeslot atual
        long totalQuantidadeNaTimeslot = products.stream()
                .filter(productDoc -> productDoc.getLong("timestamp") >= janelatempo)
                .mapToLong(productDoc -> productDoc.getInteger("quantity"))
                .sum();

        // Verifique se o usuário ultrapassou o limite
        if (totalQuantidadeNaTimeslot + quantity > limit) {
            System.out.println("Usuário " + username + " ultrapassou o limite de produtos por tempo.");
        } else {
            Document produtoDoc = new Document("product", product)
                    .append("quantity", quantity)
                    .append("timestamp", currentTimestamp);

            products.add(produtoDoc);

            Document update = new Document("$set", new Document("products", products));

            UpdateResult updateResult = db.updateOne(new Document("username", username), update);

            System.out.println("Produto " + product + " adicionado com sucesso.");
        }
    }



    public static void seeAll(){
        DistinctIterable<String> distinctUsernames = db.distinct("username", String.class);
        int count = 0;
        for(String username : distinctUsernames){
            count++;
            System.out.println(count + ". " + username);
        }

    }

    public static void addUser(String username) {
        Document userDocument = new Document("username", username)
                .append("products", new ArrayList<Document>());

        IndexOptions indexOptions = new IndexOptions().unique(true);
        db.createIndex(new Document("username", 1), indexOptions);

        try {
            db.insertOne(userDocument);
            System.out.println("Usuário " + username + " adicionado com sucesso.");
        } catch (MongoWriteException e) {
            System.out.println("Login feito com: " + username);
        }
    }

    public static void seeProducts(String username) {
        Document user = db.find(new Document("username", username)).first();
        List<Document> products = user.get("products", List.class);

        System.out.println("Produtos do usuário " + username + ":");
        for (Document product : products) {
            System.out.println(product.getString("product") + " - " + product.getInteger("quantity"));
        }
    }

    public static void deleteProduct(String username, String product) {




        try{

                Document user = db.find(new Document("username", username)).first();
                List<Document> products = user.get("products", List.class);

                Iterator<Document> iterator = products.iterator();
                while (iterator.hasNext()) {
                    Document productDoc = iterator.next();
                    if (productDoc.getString("product").equals(product)) {
                        iterator.remove(); // Remove o produto da lista
                    }
                }

                Document update = new Document("$set", new Document("products", products));

                db.updateOne(new Document("username", username), update);

                System.out.println("Produto " + product + " removido com sucesso.");


        }
        catch(Exception e){
            System.out.println("Produto " + product + " não encontrado.");
        }



    }





}
