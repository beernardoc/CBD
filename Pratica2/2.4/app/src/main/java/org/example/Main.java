package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int limit = 2;
        int timeslot = 3600;

        Manager orderManager = new Manager(limit, timeslot);

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome do usuário: ");
        String username = sc.nextLine();
        orderManager.addUser(username);




        int op;
        while (true) {
            System.out.println("\nSistema de Atendimento - " + username + "\n");
            System.out.println("1.) Listar todos os usuários.");
            System.out.println("2.) Adicionar um produto.");
            System.out.println("3.) Listar produtos do usuário.");
            System.out.println("4.) Deletar produto.");
            System.out.println("5.) Sair.");

            op = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (op) {
                case 1:
                    orderManager.seeAll();
                    break;


                case 2:
                    System.out.println("Digite o nome do produto: ");
                    String product = sc.nextLine();
                    orderManager.addOrder(username, product);
                    break;

                case 3:
                    orderManager.seeProducts(username);
                    break;

                case 4:
                    System.out.println("Digite o nome do produto: ");
                    String DelProduct = sc.nextLine();
                    orderManager.deleteProduct(username, DelProduct);
                    break;

                case 5:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;


                default:
                    System.out.println("Opção inválida.");
                    break;


            }

        }
    }
}
