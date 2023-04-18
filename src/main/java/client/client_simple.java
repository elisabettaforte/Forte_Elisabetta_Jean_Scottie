package client;

import javafx.util.Pair;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cette classe Client permet à un utilisateur de se connecter à un serveur distant et d'envoyer des commandes pour charger ou s'inscrire à des cours.
 */
public class client_simple {

    private final static String SERVER_ADDRESS = "localhost";
    private final static int SERVER_PORT = 1337;
    private final static String LOAD_COMMAND = "CHARGER";
    private final static String REGISTER_COMMAND = "INSCRIRE";

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    /**
     * Le constructeur Client permet de se connecter au serveur distant. Les flux de sortie et d'entrée de l'objet sont initialisés pour envoyer et recevoir des données.
     */
    public client_simple() {
        try {
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode, run, permet à l'utilisateur d'entrer des commandes en continu.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Client démarré!");
        while (true) {
            System.out.print("Entrez une commande: ");
            String input = scanner.nextLine();
            Pair<String, String> parts = processCommandLine(input);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            try {
                switch (cmd) {
                    case LOAD_COMMAND:
                        loadCourses();
                        break;
                    case REGISTER_COMMAND:
                        registerCourse(arg);
                        break;
                    default:
                        System.out.println("Commande non reconnue.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette méthode envoie la commande de chargement de cours au serveur et attend une réponse sous forme de chaîne de caractères qui est imprimée à la console.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void loadCourses() throws IOException, ClassNotFoundException {
        this.objectOutputStream.writeObject(LOAD_COMMAND);
        String response = (String) this.objectInputStream.readObject();
        System.out.println(response);
    }

    /**
     * Cette méthode envoie la commande d'inscription à un cours avec le sigle du cours, quelle session, la matricule, le nom, le prénom et le courriel du client sous forme d'argument et attend qu'une réponse s'imprime à la console.
     * @param arg - session, sigle du cours, matricule, prénom, nom et email
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void registerCourse(String arg) throws IOException, ClassNotFoundException {
        this.objectOutputStream.writeObject(REGISTER_COMMAND + " " + arg);
        String response = (String) this.objectInputStream.readObject();
        System.out.println(response);
    }

    /**
     * Cette méthode permet d'analyser les commandes entrées par l'utilisateur/client et séparer le nom de la commande de son argument.
     * @param line - commande entré contenant la session, le sigle, la matricule, le prénom, le nom et l'email de l'utilisateur
     * @return
     */
    private Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String arg = "";
        if (parts.length > 1) {
            arg = String.join(" ", parts).substring(parts[0].length() + 1);
        }
        return new Pair<>(cmd, arg);
    }

    /**
     * Cette méthode, main, instancie un nouvel objet Client et appelle la méthode run pour démarrer l'application client.
     * @param args - l'information de l'inscription à un cours de l'utilisateur
     */
    public static void main(String[] args) {
        client_simple clientsimple = new client_simple();
        clientsimple.run();
    }
}