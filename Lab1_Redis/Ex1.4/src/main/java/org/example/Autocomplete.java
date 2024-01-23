package org.example;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import redis.clients.jedis.Jedis;
public class Autocomplete {

    private final Jedis jedis;

    public static String NAME = "name";

    public Autocomplete(){
        this.jedis = new Jedis("redis://localhost:6379");
    }

    public void add(String name) {
        jedis.zadd(NAME, jedis.zcard(NAME), name);
    }


    public Set<String> getAutocomplete(String name) { // alterado para que não seja necessario carregar todos os nomes sempre
        Set<String> autocomplete = new HashSet<>();

        List<String> result = jedis.zrangeByLex(NAME, "[" + name, "[" + name + "\uffff");
        autocomplete.addAll(result);

        return autocomplete;
    }




    public static void main(String[] args) {
// Ensure you have redis-server running
        Autocomplete auto = new Autocomplete();

        try{
            FileInputStream file = new FileInputStream("names.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                auto.add(sc.nextLine());
            }
            sc.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        String search;

        while(true){
            System.out.print("Search for ('Enter' for quit): ");
            search = sc.nextLine();
            if(search.length() == 0){
                break;
            }

            auto.getAutocomplete(search).forEach(System.out::println);
            System.out.println();

        }



    }
}