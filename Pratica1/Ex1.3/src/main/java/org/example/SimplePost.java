package org.example;
import redis.clients.jedis.Jedis;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimplePost {

    private final Jedis jedis;
    public static String USERS = "users"; // Key set for users' name

    public SimplePost() {
        this.jedis = new Jedis("redis://localhost:6379");
    }


    public Set<String> getUser() {
        return jedis.smembers(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

    public void DelUser(){
        jedis.del(USERS);
    }

    public void close(){
        jedis.close();
    }

    public static void main(String[] args) {

        SimplePost board = new SimplePost();

        // set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
        for (String user: users)
            board.jedis.sadd("Ex_Set", user);


        System.out.println("In the Set:");

        System.out.print("Redis All Keys: ");
        board.jedis.keys("*").forEach(value -> System.out.println(value + " "));

        System.out.print("Redis values: ");
        board.jedis.smembers("Ex_Set").forEach(value -> System.out.print(value + " "));
        board.jedis.del("Ex_Set");


        System.out.println();
        System.out.println();

        // some users with List
       String[] lista = {"Carlos","Antonio", "Thiago", "Maria"};

        board.jedis.lpush("Ex_List", lista); // add the users to the HashMap

        System.out.println("i) In the List:");

        System.out.print("Redis all Keys: ");
        board.jedis.keys("*").forEach(value -> System.out.println(value + " "));

        System.out.print("Redis values: ");
        board.jedis.lrange("Ex_List", 0, -1).forEach(value -> System.out.print(value + " "));
        board.jedis.del("Ex_List");

        System.out.println();
        System.out.println();




        // some users with HashMap
        Map<String, String> newUsers = new HashMap<>();
        newUsers.put("1", "José");
        newUsers.put("2", "Antonio");
        newUsers.put("3", "Bernardo");
        newUsers.put("4", "Carlos");


        board.jedis.hmset("Ex_Hash", newUsers); // add the users to the HashMap

        System.out.println("ii) In the HashMap:");

        System.out.print("Redis all Keys: ");
        board.jedis.keys("*").forEach(value -> System.out.println(value + " "));

        System.out.print("Redis values: ");
        board.jedis.hvals("Ex_Hash").forEach(value -> System.out.print(value + " "));
        board.jedis.del("Ex_Hash");
        System.out.println();




    }



}
