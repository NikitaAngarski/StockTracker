import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.layout.*; 
import javafx.event.*; 
import javafx.scene.Parent; 
import javafx.scene.control.*; 
import javafx.scene.paint.Color;
import javafx.stage.Stage; 
import javafx.scene.Group; 
import javafx.scene.canvas.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.*;
import javafx.fxml.FXMLLoader;

public class StockTracker extends Application {
      
      @Override
      public void start(Stage stage) throws Exception {
         stage.setTitle("StockTracker"); 
      
         Parent root = FXMLLoader.load(getClass().getResource("StockTracker.fxml"));
        
         Scene scene = new Scene(root, 400, 400);
        
         stage.setScene(scene); 
                  
         stage.show(); 
         }
         
      public static void main(String[] args){
         launch(args);
         }
   }