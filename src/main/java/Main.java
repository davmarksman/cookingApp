import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

  public static void main(String[] args) {
    String port = System.getenv("PORT");
    if (port == null)
      port = "9002";

    port(Integer.valueOf(port));
    staticFileLocation("/public");

    String[] ingredients = {"Eggs", "Milk", "Rice", "White Rice", "Brown Rice", "Okra", "Tomatoes"};

    get("/ingredient", (req, res) -> {
      ArrayList<Ingredient> results = new ArrayList<Ingredient>();
      String searchIngredient = req.queryParams("ingredient");
      for (String ingred : ingredients) {
        if (ingred.toLowerCase().contains(searchIngredient.toLowerCase())) {
          results.add(new Ingredient(ingred, "images/rice.jpg"));
        }
      }
      return results;
    }, new JsonTransformer());

    get("/food", (req, res) -> {
       List<Reciepe> results = new ArrayList<Reciepe>();
      String request = req.queryParams("ingredients");

      try {
        String foodApiReq = "https://api.edamam.com/search?q=" + request
                + "&app_id=7a66e8c5&app_key=8da9f0e829bb3d2cac3ac07e29f88b39&from=0&to=3&calories=gte%20591,%20lte%20722&health=alcohol-free";
        Response data = makeRestCall(foodApiReq);


        results = parseJson(data.readEntity(String.class));
      }catch(Exception e){

      }
      return results;
    }, new JsonTransformer());
  }


    public static Response makeRestCall(String url) throws Exception {
      Client client = ClientBuilder.newClient();

      WebTarget resource = client.target(url);

      Invocation.Builder request = resource.request();
      request.accept(MediaType.APPLICATION_JSON);

      Response response = request.get();
      return response;
    }

    public static List<Reciepe> parseJson(String json) throws Exception {
      List<Reciepe> reciepes = new ArrayList<Reciepe>();

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(json);
      JsonNode results = root.at("/hits");
      for(JsonNode reciepeNode: results){
        List<String> steps = new ArrayList<>();
        String reciepeName = "";
        JsonNode reqIngredientsNode = reciepeNode.at("/recipe/ingredientLines");
        JsonNode nameNode = reciepeNode.at("/recipe/label");
        JsonNode imageNode = reciepeNode.at("/recipe/image");
        for (final JsonNode step : reqIngredientsNode) {
          steps.add(step.asText());
        }
        reciepes.add(new Reciepe(nameNode.asText(), imageNode.asText(), steps));
      }

      return reciepes;
    }

//    get("/", (request, response) -> {
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("message", "Hello World!");
//
//        return new ModelAndView(attributes, "index.ftl");
//    }, new FreeMarkerEngine());

    //HikariConfig config = new  HikariConfig();
    //config.setJdbcUrl(System.getenv("JDBC_DATABASE_URL"));
    //final HikariDataSource dataSource = (config.getJdbcUrl() != null) ?
    //  new HikariDataSource(config) : new HikariDataSource();
//
    //get("/db", (req, res) -> {
    //  Map<String, Object> attributes = new HashMap<>();
    //  try(Connection connection = dataSource.getConnection()) {
    //    Statement stmt = connection.createStatement();
    //    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
    //    stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
    //    ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//
    //    ArrayList<String> output = new ArrayList<String>();
    //    while (rs.next()) {
    //      output.add( "Read from DB: " + rs.getTimestamp("tick"));
    //    }
//
    //    attributes.put("results", output);
    //    return new ModelAndView(attributes, "db.ftl");
    //  } catch (Exception e) {
    //    attributes.put("message", "There was an error: " + e);
    //    return new ModelAndView(attributes, "error.ftl");
    //  }
    //}, new FreeMarkerEngine());




}
