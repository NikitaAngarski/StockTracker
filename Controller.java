import javafx.fxml.FXML; 
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.chart.XYChart; 


public class Controller {
    
   
    @FXML
    private Text bigTitle;

    @FXML
    private AreaChart<?, ?> chart;

    @FXML
    private Text companyName;

    @FXML
    private Button goButton;

    @FXML
    private Text highPrice;

    @FXML
    private Text lastClosingPrice;

    @FXML
    private Text lowPrice;

    @FXML
    private NumberAxis priceAxis;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField searchBox;

    @FXML
    private Text ticker;

    @FXML
    private CategoryAxis weeksAxis;
    
    private XYChart.Series series; 
    
    private String searchQuery; 
   
   
   
   
   public void searchStock(){  
      chart.setLegendVisible(false); 
      makeChart(); 
      changeTexts();  
      

      }
      
   public void makeChart(){
      clearChart(); 
      series = new XYChart.Series(); 
      series.getData().add(new XYChart.Data("W1", 54));
      series.getData().add(new XYChart.Data("W2", 54));
      series.getData().add(new XYChart.Data("W3", 54));
      series.getData().add(new XYChart.Data("W4", 54));
      series.getData().add(new XYChart.Data("W5", 54));
      series.getData().add(new XYChart.Data("W6", 54));
      series.getData().add(new XYChart.Data("W7", 54));
      series.getData().add(new XYChart.Data("W8", 54));
      series.getData().add(new XYChart.Data("W9", 54));
      series.getData().add(new XYChart.Data("W10", 54));
      series.getData().add(new XYChart.Data("W11", 54));
      series.getData().add(new XYChart.Data("W12", 54));
      series.getData().add(new XYChart.Data("W13", 54));

 
      chart.getData().addAll(series); 
      }
      
      
      
      public void clearChart() {
         chart.getData().removeAll(series); 
         }
         
      public void changeTexts() {
         searchQuery = searchBox.getText();
         
         System.out.println(searchQuery);
         
         bigTitle.setText(searchQuery+" Company Name");
         
         ticker.setText(searchQuery+" Company Name"); 
         
         companyName.setText(searchQuery+" Company name"); 
         
         lowPrice.setText("Low: "+searchQuery+" lowprice"); 
         
         highPrice.setText("High: "+searchQuery+" highprice"); 
         
         lastClosingPrice.setText("Last Closing:"+searchQuery+"closingprice"); 
         
         
         
         }
   
   
   
   
   }