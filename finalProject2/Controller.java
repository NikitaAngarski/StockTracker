import javafx.fxml.FXML; 
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.chart.XYChart; 

import java.net.http.HttpClient; 
import java.net.http.HttpRequest; 
import java.net.http.HttpResponse; 
import java.net.http.HttpResponse.BodyHandlers; 
import java.net.URL; 
import java.net.URI; 
import java.net.URISyntaxException; 
import java.io.IOException; 

import java.util.Scanner; 
import java.util.GregorianCalendar; 
import java.util.ArrayList; 
import java.util.Date; 
import java.text.SimpleDateFormat; 

import javafx.fxml.Initializable; 
import java.net.URL; 
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import javafx.application.Platform; 


public class Controller {
    
    @FXML
    private Label bigTitle;

    @FXML
    private Label change;

    @FXML
    private Button goButton;

    @FXML
    private Label high;

    @FXML
    private Label low;

    @FXML
    private Label open;

    @FXML
    private Label percentChange;

    @FXML
    private Label price;

    @FXML
    private TextField searchBox;
    
    private StockData stockData; 
    
    private HttpClient client; 
    
    //private XYChart.Series series; 
    
    private String searchQuery; 
    
    private Date today; 
    
    private String priceHolder;
    private String lowHolder;
    private String highHolder;
    private String openHolder;
    private String changeHolder;
    private String percentChangeHolder; 
   
   
   
   public void searchStock(){  
      searchQuery = searchBox.getText(); 
      updateStockData(searchQuery);
       
      

      }
   /*   
   public void makeChart(){
      clearChart(); 
      series = new XYChart.Series(); 
      series.getData().add(new XYChart.Data("W1", 154));
      series.getData().add(new XYChart.Data("W2", 54));
      series.getData().add(new XYChart.Data("W3", 124));
      series.getData().add(new XYChart.Data("W4", 54));
      series.getData().add(new XYChart.Data("W5", 34));
      series.getData().add(new XYChart.Data("W6", 13));
      series.getData().add(new XYChart.Data("W7", 65));
      series.getData().add(new XYChart.Data("W8", 87));
      series.getData().add(new XYChart.Data("W9", 54));
      series.getData().add(new XYChart.Data("W10", 23));
      series.getData().add(new XYChart.Data("W11", 51));
      series.getData().add(new XYChart.Data("W12", 112));
      series.getData().add(new XYChart.Data("W13", 74));

 
      chart.getData().addAll(series); 
      }
      
      
      
      public void clearChart() {
         chart.getData().removeAll(series); 
         }
      */ 
         
      public void updateStockData(String symbol) {
      
         if (this.client == null){
            this.client = HttpClient.newHttpClient(); 
         }
         
         try {
         
            System.out.println(System.getenv("APIKEY"));
            
            HttpRequest requestWeekly = HttpRequest.newBuilder()
                                             .uri(new URI("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+"&apikey="+System.getenv("APIKEY")))
                                             .GET()
                                             .build();
                                             
            client.sendAsync(requestWeekly, BodyHandlers.ofString())
                  .thenApply(HttpResponse::body)
                  .thenAccept(this::processStock);
            
            }
            
            catch(URISyntaxException e){
               System.out.println("Issue with request"); 
               bigTitle.setText("Error"); 

               }
         
         
         }
         
     
         protected void processStock(String data){
         
            this.today = new Date(); 
            
            System.out.println(data); 
            
            Gson gson1 = new Gson(); 
            
            this.stockData = gson1.fromJson(data, StockData.class); 
            
            
            
            searchQuery = searchBox.getText();
         
            System.out.println(searchQuery);
            
            System.out.println(data.charAt(7)); 
            
            
            
            
            if(data.charAt(7) != 'G' || this.stockData.globalQuote.price == null){
               Platform.runLater(new Runnable() {
                @Override
                 public void run() {
                 
                        bigTitle.setText("Error");
         
                        price.setText("Price: error"); 
         
                        low.setText("Low: error"); 
         
                        high.setText("High: error"); 
               
                        open.setText("Open: error"); 
            
                        change.setText("Change: error");
               
                        percentChange.setText("Percent Change: error");
                     }
                 });
            
            }
            else {
               
               priceHolder = this.stockData.globalQuote.price;
               lowHolder = this.stockData.globalQuote.low;
               highHolder = this.stockData.globalQuote.high;
               openHolder = this.stockData.globalQuote.open;
               changeHolder = this.stockData.globalQuote.change;
               percentChangeHolder = this.stockData.globalQuote.percentChange;
            
               Platform.runLater(new Runnable() {
                @Override
                 public void run() {
                 
                     bigTitle.setText(searchQuery);
         
                     price.setText("Price: "+priceHolder); 
         
                     low.setText("Low: "+lowHolder); 
         
                     high.setText("High: "+highHolder); 
               
                     open.setText("Open: "+openHolder); 
            
                     change.setText("Change: "+changeHolder);
               
                     percentChange.setText("Percent Change: "+percentChangeHolder); 
                                 }
                        
                            });
                     
             }
     
                     
                     
                 }
           
            
            
            
           
         
            }
   
   
   
