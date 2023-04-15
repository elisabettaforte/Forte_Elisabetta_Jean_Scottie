package client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.awt.*;


public class clientGraphique extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Text nameLabel = new Text("Prénom");

        TextField nameText = new TextField();

        Text surnameLabel = new Text("Nom");

        TextField surnameText = new TextField();

        Text emailLabel = new Text("Email");

        TextField emailText = new TextField();

        Text matriculeLabel = new Text("Matricule");

        TextField matriculeText = new TextField();


    }
}
