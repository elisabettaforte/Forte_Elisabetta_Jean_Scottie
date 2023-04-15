package client;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final static String SERVER_ADDRESS = "localhost";
    private final static int SERVER_PORT = 1337;
    private final static String LOAD_COMMAND = "CHARGER";
    private final static String REGISTER_COMMAND = "INSCRIRE";

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client() {
        try {
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void loadCourses() throws IOException, ClassNotFoundException {
        this.objectOutputStream.writeObject(LOAD_COMMAND);
        String response = (String) this.objectInputStream.readObject();
        System.out.println(response);
    }

    private void registerCourse(String arg) throws IOException, ClassNotFoundException {
        this.objectOutputStream.writeObject(REGISTER_COMMAND + " " + arg);
        String response = (String) this.objectInputStream.readObject();
        System.out.println(response);
    }

    private Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String arg = "";
        if (parts.length > 1) {
            arg = String.join(" ", parts).substring(parts[0].length() + 1);
        }
        return new Pair<>(cmd, arg);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}