package prjtrabalhomlp_backpropagation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrjTrabalhoMLP_Backpropagation extends Application
{    
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("TelaPrincipal.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Redes Neurais");
        stage.setResizable(false);
        
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e->{ System.exit(0); });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}