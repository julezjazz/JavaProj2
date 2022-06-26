package main;

import dao.UserDao;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/** This class creates an app that displays screens. */
public class Main extends Application {
    public static ResourceBundle rb;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/LogIn.fxml"));
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();

    }


    public static void main(String[] args) {
        //Locale.setDefault(Locale.CANADA);
        try {rb = ResourceBundle.getBundle("main/Lang", Locale.getDefault());}
        catch (MissingResourceException e){
            System.out.println("No resource bundle available for " + Locale.getDefault().getDisplayCountry());
        }
        JDBC.openConnection();
        UserDao.populateUserList();
        launch(args);
        JDBC.closeConnection();
    }
}
