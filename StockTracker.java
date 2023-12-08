import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.layout.*; 
import javafx.event.*; 
import javafx.scene.shape.*; 
import javafx.scene.control.*; 
import javafx.scene.paint.Color;
import javafx.stage.Stage; 
import javafx.scene.Group; 
import javafx.scene.canvas.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.*;

public class StockTracker extends Application {
      
      @Override
      public void start(Stage stage) {
         stage.setTitle("StockTracker"); 
      
         Pane root = new Pane();
         
         Rectangle rect = new Rectangle(30, 40); 
         
         
         
         
         
         
        
         Scene scene = new Scene(root, 700, 700);
        
         stage.setScene(scene); 
         root.getChildren().add(rect);          
         
         
         stage.show(); 
         }
         
      public static void main(String[] args){
         launch(args);
         }
   }