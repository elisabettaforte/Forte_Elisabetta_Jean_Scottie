package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try{
            Socket server = new Socket(int port)
            System.out.println("Connecté au serveur");

            // Envoyer une requête "charger" au serveur
            PrintWriter pw = new PrintWriter(server.getOutputStream(), true);
            pw.println("charger");

            // Recuperer la liste des cours envoyee par le serveur
            BufferedReader br = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String cours = br.readLine();
            System.out.println(cours);

            // Fermer les flux et la connexion
            br.close();
            pw.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
