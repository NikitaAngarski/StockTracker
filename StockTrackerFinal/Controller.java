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
import java.util.ResourceBundle; 
import java.util.prefs.Preferences; 
import java.util.ArrayList; 
import java.util.Map;
import java.util.LinkedHashMap; 
import java.util.List; 
import java.util.Set;  
import java.util.Collections; 

import javafx.fxml.Initializable; 
import java.net.URL; 
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import javafx.application.Platform; 
import java.lang.Number; 

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

 
import org.joda.time.*;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat; 



public class Controller implements Initializable {

    @FXML
    private AreaChart chart; 
    
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
    
    @FXML
    private NumberAxis yAxis; 
    
    private StockData stockData; 
    
    private HttpClient client; 
    
    private XYChart.Series series; 
    
    //a string representing the user's input 
    private String searchQuery; 
    
    private LinkedHashMap<String, Double> tsMap; 
    
    private JSONObject time; 
    
    private String weeklyPrice; 
    
    //the below are placeholders for the strings that the gson objects represent
    //each of these is declared and instantiated separately for the Platform.runLater 
    //function so that it will use these placeholders to change the UI 
    private String priceHolder;
    private String lowHolder;
    private String highHolder;
    private String openHolder;
    private String changeHolder;
    private String percentChangeHolder; 
    private String tickerHolder; 
    
    
    
  //This is the initialization method that starts up as soon as 
  //the app is up. As shown, the searchQuery is set to AAPL so that
  //upon opening the app, the user is automatically shown Apple stock data  
  public void initialize(URL location, ResourceBundle resources){
      Preferences p = Preferences.userNodeForPackage(Controller.class); 
       
      
      searchQuery = "AAPL";
      
      updateStockData(searchQuery);  
      }
   
   
   //searchStock is connected directly to the goButton 
   //so this executes every time that the app starts up, 
   //very minimal method 
   //
   public void searchStock(){  
      chart.setAnimated(true); 
      searchQuery = searchBox.getText(); 
      updateStockData(searchQuery);
       
       
      

      }
  
   public void makeChart(LinkedHashMap map){
      
      clearChart(); 
      series = new XYChart.Series(); 
      //must set autoranging to false in order to later have the ability to change the 
      //upper and lower bound limits 
      yAxis.setAutoRanging(false); 
      
      List<String> mapKeys = new ArrayList<String>(map.keySet());
            
            //reversing the list of keys to arrange them in chronological order 
            Collections.reverse(mapKeys); 
            
            
            //making the chart is easy with the linkedHashMap, because in one 
            //line you can input the key and the value with the same key
            //the only challenge being that I had to reverse the list of keys 
            //to make them chronological
            for (String revKey: mapKeys) {
               System.out.println(revKey +" : "+map.get(revKey));
               series.getData().add(new XYChart.Data(revKey, map.get(revKey))); 
               }
            //finding min and max of the values to make a buffer 
            //and set upper and lower bound values of the y axis    
            Double min = (Double)Collections.min(map.values());
            Double max = (Double)Collections.max(map.values());
            
            //buffer is somewhat arbitrary, but it takes into account 
            //min and max values for the series and sets a bit of a buffer 
            //around them to make the graph more useful and more visually appealing 
            Double buffer = (max - min)/2; 
            
            yAxis.setLowerBound(min - buffer); 
            yAxis.setUpperBound(max + buffer);  
               
            chart.getData().addAll(series); 
      }
      //test method
      public void fakeSeries() {
         chart.setAnimated(true); 
         yAxis.setLowerBound(0.0); 
         yAxis.setUpperBound(100.0);
         series = new XYChart.Series(); 
         for (int i = 1; i < 14; i++) {
            series.getData().add(new XYChart.Data(""+i+"", 0.0)); 
            }
         chart.getData().addAll(series); 
         }
      
      public void clearChart() {
         chart.getData().clear();
         
          
         
         }
     
       
      //This is the main method that executes from the app.    
      public void updateStockData(String symbol) {
      
         if (this.client == null){
            this.client = HttpClient.newHttpClient(); 
         }
         
         try {
         
            //this HttpRequest object is calling to the api every time the user searches a new stock
            //You can see the symbol String is used to call the api for the stock the user would like
            //to see the info for
            HttpRequest requestData = HttpRequest.newBuilder()
                                             .uri(new URI("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+"&apikey="+System.getenv("APIKEY")))
                                             .GET()
                                             .build();
                                             
            client.sendAsync(requestData, BodyHandlers.ofString())
                  .thenApply(HttpResponse::body)
                  .thenAccept(this::processStock);
                  
                  
            HttpRequest requestTimeSeries = HttpRequest.newBuilder()
                                                       .uri(new URI("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol="+symbol+"&apikey="+System.getenv("APIKEY")))
                                                       .GET()
                                                       .build(); 
            
            client.sendAsync(requestTimeSeries, BodyHandlers.ofString())
                  .thenApply(HttpResponse::body)
                  .thenAccept(this::processTimeSeries); 
                  
                  
            
            
            }
            
            //If we do experience an error, the big Title where the stock ticker is will be turned into 
            // "Error" 
            catch(URISyntaxException e){
               System.out.println("Issue with request"); 
               bigTitle.setText("Error"); 

               }
         
         
         }
         
         protected void processTimeSeries(String data){
            //System.out.println(data);
            //Creates some JSON Objects that layer on top of one another to get the startdate from the 
            //API output 
            JSONObject output = (JSONObject)JSONValue.parse(data); 
            JSONObject metaData = (JSONObject)output.get("Meta Data");
            JSONObject timeSeries = (JSONObject)output.get("Weekly Time Series"); 
            String date = (String)metaData.get("3. Last Refreshed");
            
            //this method makes a linkedHashMap to copy the 
            tsMap = new LinkedHashMap<>(); 
             
            
            for (String s: getWeeks(date)){ 
               time = (JSONObject)timeSeries.get(s); 
               weeklyPrice = (String)time.get("4. close"); 
               tsMap.put(s, Double.parseDouble(weeklyPrice)); 
                
               }
               
            Platform.runLater(new Runnable() {
                @Override
                 public void run(){ 
                 
                 makeChart(tsMap);
                 }
             }
                              );
             
             
            
           
             
            }
            
         // This is a static method I developed with the joda time 
         // library to make an array of dates with the 
         // yyyy-MM-dd format that will allow me to access the JSON
         // objects with the same key.   
         protected static String[] getWeeks(String startDate){
         String[] weeks = new String[13]; 
         DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd"); 
         DateTime start = format.parseDateTime(startDate); 
         String dateToString; 
         
         //the start date is what the API tells me is the last refreshed day
         //which is always the first data point of the time series
         weeks[0] = startDate;
         //the start date is offset by 1 day so that if the first day is a friday 
         // it will not be put twice into the map  
         start = start.minusDays(1); 
         //This makes the map friday by friday 
         int i = 1;
         while (i < weeks.length) {
            if (start.getDayOfWeek() == DateTimeConstants.FRIDAY) {
               weeks[i] = format.print(start); 
               i++;
               }
            start = start.minusDays(1); 
            }
    
          return weeks; 
         }
         
         //this method within the method parses the json output and makes new pojo's with it. 
         //the string of data (json) is passed to the method to then make objects and manipulate them. 
         protected void processStock(String data){
         
            //checking to see what the data is 
            System.out.println(data); 
            
            Gson gson1 = new Gson(); 
            //the gson object parses through the json to make POJO's 
            this.stockData = gson1.fromJson(data, StockData.class); 
            
            
            //we take the searchQuery and set it to the searchBox's text. 
            searchQuery = searchBox.getText();
         
            System.out.println(searchQuery);
            
             
            
            
            
            //this runnable method checks to see if the data is not there for some reason. 
            //The if statement here checks if the 8th character of the output is a G 
            //for Global Quote, which is the output we're looking for. If we are given 
            //an error message that says "please pay for an upgraded key" or "Error stock doesn't
            //exist", this will let the user know by changing everything into an error in the UI
            if(data.charAt(7) != 'G' || this.stockData.globalQuote.price == null){
               Platform.runLater(new Runnable() {
                @Override
                 public void run() {
                 
                        bigTitle.setText("Invalid Query");
         
                        price.setText("Price: "); 
         
                        low.setText("Low: "); 
         
                        high.setText("High: "); 
               
                        open.setText("Open: "); 
            
                        change.setText("Change: ");
               
                        percentChange.setText("Percent Change: ");
                        
                        clearChart(); 
                        fakeSeries();  
                     }
                 });
            
            }
            else {
            
            //upon a successful retrieval and processing, the placeholders are set
            //to the pojo's created via the gson object. 
               
               tickerHolder = this.stockData.globalQuote.symbol; 
               priceHolder = this.stockData.globalQuote.price;
               lowHolder = this.stockData.globalQuote.low;
               highHolder = this.stockData.globalQuote.high;
               openHolder = this.stockData.globalQuote.open;
               changeHolder = this.stockData.globalQuote.change;
               percentChangeHolder = this.stockData.globalQuote.percentChange;
            
            //then these placeholders are used to set the all the UI's variables 
            //from Platform.runLater, which separates the thread from the backend 
            //to the frontend
               Platform.runLater(new Runnable() {
                @Override
                 public void run() {
                 
                     bigTitle.setText(tickerHolder);
         
                     price.setText("Price: "+priceHolder); 
         
                     low.setText("Low: "+lowHolder); 
         
                     high.setText("High: "+highHolder); 
               
                     open.setText("Open: "+openHolder); 
            
                     change.setText("Change: "+changeHolder);
               
                     percentChange.setText("Percent Change: "+percentChangeHolder); 
                                 }
                        
                           }
                     );
                     
             }
     
                     
                     
                 }
           
            
            
            
           
         
            }
   
   
   
