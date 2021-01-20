package dograce;

import communicatorclient.Communicator;
import communicatorclient.CommunicatorClientWebSocket;
import communicatorclient.CommunicatorMessage;
import dograce.models.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Documented;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DogRace extends Application implements IDogRace, Observer
{
    int playerNr;
    private Communicator communicator = null;

    public ColorPicker colorPicker;
    public Button registerButton;
    public Button connectButton;
    public Button saveDogButton;
    public Button readyButton;
    public TextField playerName;
    public TextField password;
    public TextField dogName;
    public Circle playerDog = new Circle(0,180,19.0,Color.BLACK);
    public Circle opponentDog = new Circle(0, 320.0,19.0, Color.GREEN);
    public Text countdownText;
    public Label racePlayerName = new Label();
    public Label raceOpponentName = new Label();

    private Scene scene;
    private double playerNewX = 0;

    DogRaceGame game = new DogRaceGame();

    @Override
    public void start(Stage primaryStage) throws Exception{
        racePlayerName.setLayoutX(29.0);
        racePlayerName.setLayoutY(179);
        racePlayerName.setText("Player");
        raceOpponentName.setLayoutX(29.0);
        raceOpponentName.setLayoutY(179);
        raceOpponentName.setText("Opponent");
        VBox root = FXMLLoader.load(getClass().getClassLoader().getResource("dograceGUI.fxml"));
        root.getChildren().add(racePlayerName);
        root.getChildren().add(playerDog);
        root.getChildren().add(raceOpponentName);
        root.getChildren().add(opponentDog);
        primaryStage.setTitle("Animal race");
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this::keyPressed);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void notifyWhenReady()
    {

    }

    private void keyPressed(KeyEvent key) {

        if (key.getCode() == KeyCode.E) {
            moveDog();
        }
    }

    @Override
    public void registerPlayer()
    {
        if(playerName.getText() != null && password.getText() != null)
        {
            game.registerPlayer(playerName.getText(), password.getText());
            registerButton.setDisable(true);
            playerName.setDisable(true);
            password.setDisable(true);
            saveDogButton.setDisable(false);
            dogName.setDisable(false);
            colorPicker.setDisable(false);
        }
        else {
            showMessage("Vul naam en wachtwoord in.");
        }
    }

    @Override
    public void connect()
    {
        // Create the client web socket to communicate with other white boards
        communicator = CommunicatorClientWebSocket.getInstance();
        communicator.addObserver(this);

        // Establish connection with server
        communicator.start();
    }

    @Override
    public void readyPlayer()
    {
        game.readyPlayer(playerNr);
    }

    @Override
    public void saveDog()
    {
        if(dogName.getText() != null)
        {
            game.saveDog(dogName.getText(), colorPicker.getValue());
            dogName.setDisable(true);
            colorPicker.setDisable(true);
            saveDogButton.setDisable(true);
            playerDog.setFill(colorPicker.getValue());
        }
        else {
            showMessage("Kies een naam voor de hond.");
        }
    }

    private void moveDog()
    {
        playerNewX = playerNewX + 10;
        playerDog.setTranslateX(playerNewX);
        playerDog.setLayoutX(playerNewX);
        System.out.println(playerNewX);
    }

    private void moveOpponentDog(double pos)
    {
        opponentDog.setTranslateX(pos);
        opponentDog.setLayoutX(pos);
    }

    private void showMessage(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(message);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (Exception ex) {
            Logger.getLogger(DogRace.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (communicator != null) {
            communicator.stop();
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        CommunicatorMessage message = new CommunicatorMessage();
        String check = "";
        String[] splittedmessage = new String[]{};
        try
        {
            check = (String) arg;

        }
        catch(Exception e)
        {}
        try
        {
            message = (CommunicatorMessage) arg;

        }
        catch(Exception e)
        {}
        if(check.length() == 1)
        {
            playerNr = Integer.parseInt(check);
            System.out.println(playerNr);
        }
        else if(check.equals("USERDETAILS"))
        {
            communicator.sendDetails(playerName.getText() + " " + dogName.getText() + " " + playerNr);
        }
        else if(message.getDetails() != null)
        {
            String mes = message.getDetails();
            String[] messplit = mes.split(" ");
            if(messplit.length == 2)
            {
                String movement = "";
                if(messplit[0].length() >= 5)
                {
                    movement = messplit[0].substring(0, messplit[0].length() - 4);
                }
                else{
                    movement = messplit[0];
                }
                //movement
                if(Integer.parseInt(messplit[1]) != playerNr)
                {
                    moveOpponentDog(Double.parseDouble(movement));
                }

                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                System.setOut(new java.io.PrintStream(out));

                String convert = out.toString();
                communicator.sendMove(convert + " " + playerNr);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                communicator.sendMove(playerNewX + " " + playerNr);
            }
        }
        else
            {

        }
    }
}

