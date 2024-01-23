package org.example;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import static java.util.stream.Collectors.toMap;

public class AutoCompleteB {

    private final Jedis jedis;

    public static String NAME = "nameB";

    public AutoCompleteB(){
        this.jedis = new Jedis("redis://localhost:6379");
    }

    public void add(String name, Double score){
        jedis.zadd(NAME, score, name);
    }

    public List<Tuple> getAll(){
        return jedis.zrevrangeWithScores(NAME, 0, -1);
    }

    public HashMap<String, Double> getAutocomplete(String name){
        List<Tuple> all = getAll();
        HashMap<String, Double> autocomplete = new HashMap<>();
        for(Tuple t : all){
            if(t.getElement().startsWith(name)){
                autocomplete.put(t.getElement(), t.getScore());
            }
        }

        return autocomplete;
    }


    public static void main(String[] args) {
// Ensure you have redis-server running
        AutoCompleteB auto = new AutoCompleteB();

        try{
            FileInputStream file = new FileInputStream("nomes-pt-2021.csv");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()){
                String[] line = sc.nextLine().split(";");
                auto.add(line[0], Double.parseDouble(line[1]));
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

            System.out.println();
            HashMap<String, Double> result = auto.getAutocomplete(search);

            List<Map.Entry<String, Double>> entryList = new ArrayList<>(result.entrySet());

            entryList.sort(Map.Entry.<String, Double>comparingByValue().reversed());
            result = entryList.stream().collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

            for(Map.Entry<String,Double> entry : result.entrySet()){
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }

            System.out.println();




        }



    }
}