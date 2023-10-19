package org.example;


import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class Main {

    static MongoClient mongoClient;
    static MongoDatabase database;
    public static void main(String[] args) {

        MongoCollection<Document> db = mongoConnect();


        /* exemplo da query: db.restaurants.find({localidade: "Bronx"}).count()
        // Crie um filtro para a consulta
        Document filter = new Document("localidade", "Bronx");
        // Conte os documentos que correspondem ao filtro
        long count = db.countDocuments(filter);
        // Imprima o resultado
        System.out.println("Número de restaurantes em Bronx: " + count);
        */
        System.out.println("Exercício A: ");
        System.out.println();
        while (true) {

            System.out.println("Menu:");
            System.out.println("1. Inserir registro");
            System.out.println("2. Editar registro");
            System.out.println("3. Pesquisar registro");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");


            Scanner sc = new Scanner(System.in);
            int op = sc.nextInt();

            switch (op) {
                case 1:
                    // Inserir registro
                    System.out.println("Inserindo restaurante...");
                    System.out.print("Nome: ");
                    String nome = sc.next();
                    System.out.print("Building: ");
                    String building = sc.next();
                    System.out.print("Rua: ");
                    String rua = sc.next();
                    System.out.print("Zipcode: ");
                    String zipcode = sc.next();
                    System.out.print("Localidade: ");
                    String localidade = sc.next();
                    System.out.print("Gastronomia: ");
                    String gastronomia = sc.next();

                    insert(db, nome, building, rua, zipcode, localidade, gastronomia);
                    break;


                case 2:
                    System.out.print("Nome do restaurante a ser editado: ");
                    String nomeParaEditar = sc.next();
                    System.out.print("Campo a ser editado: ");
                    String campoParaEditar = sc.next();

                    Document filtro = new Document("nome", nomeParaEditar);

                    // Primeiro verifique se o campo a ser editado existe
                    Document documento = db.find(filtro).first();

                    if(documento == null){
                        System.out.println("Restaurante não encontrado.");
                        break;
                    }

                    if (!documento.containsKey(campoParaEditar)) {
                        System.out.println("O campo especificado não existe.");
                        break;
                    }
                    update(db, nomeParaEditar, campoParaEditar);
                    break;


                case 3:

                    System.out.print("Pesquisa a partir de: ");
                    String campo = sc.next();

                    System.out.print("Valor a ser pesquisado: ");
                    String valor = sc.next();

                    Document search = new Document(campo, valor);

                    for (Document resultado : db.find(search)) {
                        System.out.println(resultado.toJson());
                    }

                    break;

                case 4:

                    System.out.println("Encerrando o programa.");
                    mongoClient.close();
                    System.exit(0);

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }


        }


    }

    public static MongoCollection<Document> mongoConnect(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Acesse o banco de dados
        database = mongoClient.getDatabase("cbd");

        return database.getCollection("restaurants");
    }

    public static void insert(MongoCollection<Document> db, String nome, String building, String rua, String zipcode, String localidade, String gastronomia){
        Document document = new Document("nome", nome)
                .append("building", building)
                .append("rua", rua)
                .append("zipcode", zipcode)
                .append("localidade", localidade)
                .append("gastronomia", gastronomia);

        db.insertOne(document);
        System.out.println("Restaurante " + nome + " inserido com sucesso.");
    }

    public static void update(MongoCollection<Document> db, String nome, String campo){
        Scanner sc = new Scanner(System.in);
        System.out.print("Novo valor: ");
        String novoValor = sc.next();

        // Crie a atualização com o novo valor
        Document update = new Document("$set", new Document(campo, novoValor));

        // Atualize o documento com o mesmo filtro
        db.updateOne(new Document("nome", nome), update);
        System.out.println("Registro editado com sucesso.");
    }

}