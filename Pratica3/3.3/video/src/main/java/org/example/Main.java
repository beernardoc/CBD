package org.example;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;


public class Main {
    public static void main(String[] args) {

        Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1").build();
        Session session = cluster.connect("videos");

        System.out.println("Connected to cluster: " + cluster.getMetadata().getClusterName());

        String query;

        System.out.println("\n\nAlínea a) CRUD:");


        //a) Create

        query = "INSERT INTO users( username, email, nome, selo) VALUES ('bernardopinto', 'bernardo@ua.pt', 'bernardo', toTimestamp(now()));";
        System.out.println("Create: " + query);
        session.execute(query);

        // b) Read

        query = "SELECT * FROM users WHERE username = 'bernardopinto';";

        ResultSet rs = session.execute(query);
        System.out.println("Read: " + query);
        System.out.println(rs.all());

        // c) Update

        query = "UPDATE users SET email='novoemail@ua.pt' WHERE username = 'bernardopinto';";
        System.out.println("Update: " + query);
        session.execute(query);

        // d) Delete

        query = "DELETE FROM users WHERE username = 'bernardopinto';";
        System.out.println("Delete: " + query);
        session.execute(query);

        System.out.println("\n\nAlínea b):");


        query = "SELECT * FROM video_comentario WHERE video_id = 4 LIMIT 3;";
        System.out.println("Query 1: " + query);
        rs = session.execute(query);
        System.out.println(rs.all());

        query = "SELECT * FROM followers WHERE video_id = 3;";
        System.out.println("Query 2: " + query);
        rs = session.execute(query);
        System.out.println(rs.all());

        query = "SELECT * FROM videos.eventos WHERE video_id = 1 AND autor = 'user1';";
        System.out.println("Query 3: " + query);
        rs = session.execute(query);
        System.out.println(rs.all());

        query = "SELECT tags FROM video WHERE id = 1;";
        System.out.println("Query 4: " + query);
        rs = session.execute(query);
        System.out.println(rs.all());








    }
}