package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.List;
import java.util.Scanner;



public class SistemaExA {

    private final Jedis jedis;

    private final int product_limit = 2;
                                            // 2 produtos por hora
    private final int timeslot  = 3600;


    public SistemaExA(){this.jedis = new Jedis("redis://localhost:6379");}

    public void addUser(String name){
        jedis.sadd("users", name );
    }

    public void addProduct(String name, String product){
        jedis.zadd(name, System.currentTimeMillis(), product);
    }

    public void seeAll(){
        System.out.println("Usuários: " + jedis.smembers("users"));
    }

    public void listProducts(String username) {
        List<Tuple> userProducts = jedis.zrangeWithScores(username, 0, -1);
        System.out.println("Produtos de " + username + ":");
        for (Tuple product : userProducts) {
            System.out.println(product.getElement() + "(" + product.getScore() + ")");
        }
    }

    public void deleteProduct(String username, String product) {
        jedis.zrem(username, product);
        System.out.println("Produto removido com sucesso.");
    }


    public boolean Verifica(String username) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - timeslot * 1000;


        List<Tuple> records = jedis.zrangeByScoreWithScores(username, windowStart, currentTime); // pega os produtos do usuário no intervalo de tempo


        long productCount = records.stream().distinct().count();
        if (productCount >= product_limit) {
            System.out.println("Limite de produtos atingido para o usuário " + username);
            return false;
        }

        return true;
    }


    public static void main(String[] args) {

        SistemaExA sistema = new SistemaExA();

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome do usuário: ");
        String username = sc.nextLine();

        sistema.addUser(username);

        int op;
        while(true){
            System.out.println("\nSistema de Atendimento - " + username + "\n");
            System.out.println("1.) Listar todos os usuários.");
            System.out.println("2.) Adicionar um produto.");
            System.out.println("3.) Listar produtos do usuário.");
            System.out.println("4.) Deletar produto.");
            System.out.println("5.) Sair.");

            op = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch(op){
                case 1:
                    sistema.seeAll();
                    break;
                case 2:
                    System.out.println("Digite o nome do produto para adicionar: ");
                    String product = sc.nextLine();


                    if (sistema.Verifica(username)) {
                        System.out.println("Solicitação aceita para o usuário " + username + " para o produto " + product);
                        sistema.addProduct(username, product);
                    }
                    break;

                case 3:
                    sistema.listProducts(username);
                    break;

                case 4:
                    System.out.println("Digite o nome do produto para deletar: ");
                    String productToDelete = sc.nextLine();
                    sistema.deleteProduct(username, productToDelete);
                    break;

                case 5:
                    System.exit(0);
                    sc.close();
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;


            }


            }








    }

}