package server;

import javafx.util.Pair;
import server.models.Course;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * La classe Server représente le serveur qui attend les connexions entrantes et traite les commandes envoyées
 * par les clients
 */
public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Cette méthode est un constructeur qui initialise un objet ServerSocket pour écouter sur le port spécifié et initialise une ArrayList vide pour stocker les gestionnaires d'évènements.
     * @param port - Ce paramètre permet de crée le serveur sur le port spécifié.
     * @throws IOException - Il y a une exception qui pourrait être lancé suite au lancement du serveur.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Cette méthode ajoute un manipulateur d'évènements à la liste des manipulateurs d'événements du serveur.
     * @param h - manipulateur d'évènements qui se fait ajouter à la liste des manipulateurs d'évènements
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Cette méthode privée appelle la méthode handle() pour chaqye manipulateur d'événements enregistré avec la commande et l'argument spécifiés.
     * @param cmd - paramètre de commande qui est une série de caractères
     * @param arg - paramètre sous forme d'une série de caractères
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Cette méthode permet d'exécuter une boucle infinie pour attendre les connexions entrantes et traite les commandes reçues.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette méthode permet de lire la commande envoyée par le client et appelle la méthode processCommandLine() pour la traiter.
     * De plus, cette méthode appelle des cas exceptions qui nous avertir qu'il pourrait avoir des Exceptions.
     * @throws IOException - avertissement qu'il pourrait avoir une erreur à la sortie
     * @throws ClassNotFoundException - avertissement que la classe listen() n'existe pas.
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Cette méthode traite une ligne de commande et la divise en deux parties, soit la commande et l'argument.
     * @param line - paramètre de caractère nommé line qui correspond à une requête de la ligne de commande.
     * @return - retourne une nouvelle séquence de paire de commande et d'argument
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Ceci est une méthode qui ferme les flux de données et la connexion avec le client.
     * De plus, un avertissement est imprimer avec IOException.
     * @throws IOException - avertissement qu'il pourrait avoir une erreur à la sortie de ce processus de déconnexion
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Cette méthode traite les commandes spécifiées en appelant les méthodes handleRegistration() ou handleLoadCourses() en fonction de la commande.
     * @param cmd - paramètre correspondant à 'Inscrire'
     * @param arg - liste de cours
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        List<Course> courses = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("cours.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);

                // Course course = Course.fromString(line);
                /* if (course.getSession().equals(arg)) {
                    ((ArrayList<?>) courses).add(course);
                } */
            }
            br.close();
            objectOutputStream.writeObject(courses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {

        // TODO: implémenter cette méthode
    }
}

