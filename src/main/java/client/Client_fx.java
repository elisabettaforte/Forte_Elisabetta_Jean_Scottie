package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client_fx extends Application {

    private final static String SERVER_ADDRESS = "localhost";
    private final static int SERVER_PORT = 1337;
    private final static String LOAD_COMMAND = "CHARGER";
    private final static String REGISTER_COMMAND = "INSCRIRE";

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private ChoiceBox<String> sessionChoiceBox;
    private Button loadButton;
    private Label courseListLabel;
    private Label statusLabel;
    private TextField courseCodeTextField;
    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField studentIdTextField;
    private TextField emailTextField;
    private Button registerButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupNetworking();
        setupUI(primaryStage);
    }

    private void setupNetworking() {
        try {
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupUI(Stage primaryStage) {
        // Session selection
        Label sessionLabel = new Label("Session:");
        sessionChoiceBox = new ChoiceBox<>();
        sessionChoiceBox.getItems().addAll("Hiver", "Été", "Automne");
        sessionChoiceBox.setValue("Hiver");

        // Load courses button
        loadButton = new Button("Charger");
        loadButton.setOnAction(event -> {
            try {
                loadCourses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        // Course list display
        courseListLabel = new Label("");

        VBox courseListPane = new VBox(10, new HBox(10, sessionLabel, sessionChoiceBox), loadButton, courseListLabel);
        courseListPane.setAlignment(Pos.CENTER_LEFT);
        courseListPane.setPadding(new Insets(10));

        // Registration form
        Label courseCodeLabel = new Label("Code du cours:");
        courseCodeTextField = new TextField();

        Label firstNameLabel = new Label("Prénom:");
        firstNameTextField = new TextField();

        Label lastNameLabel = new Label("Nom:");
        lastNameTextField = new TextField();

        Label studentIdLabel = new Label("Matricule:");
        studentIdTextField = new TextField();

        Label emailLabel = new Label("Email:");
        emailTextField = new TextField();

        GridPane registrationForm = new GridPane();
        registrationForm.setHgap(10);
        registrationForm.setVgap(10);
        registrationForm.addRow(0, courseCodeLabel, courseCodeTextField);
        registrationForm.addRow(1, firstNameLabel, firstNameTextField);
        registrationForm.addRow(2, lastNameLabel, lastNameTextField);
        registrationForm.addRow(3, studentIdLabel, studentIdTextField);
        registrationForm.addRow(4, emailLabel, emailTextField);

        // Registration button
        registerButton = new Button("S'inscrire");
        registerButton.setOnAction(event -> registerCourse());

        // Status label
    }

    private void registerCourse(String arg) throws IOException, ClassNotFoundException {
        this.objectOutputStream.writeObject(REGISTER_COMMAND + " " + arg);
        String response = (String) this.objectInputStream.readObject();
        System.out.println(response);
    }

    private void loadCourses() throws IOException, ClassNotFoundException {
        this.objectOutputStream.writeObject(LOAD_COMMAND);
        String response = (String) this.objectInputStream.readObject();
        System.out.println(response);
    }
}