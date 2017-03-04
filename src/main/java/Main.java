import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

  public static void main(String[] args) {
    String port = System.getenv("PORT");
    if(port == null)
      port = "9000";

    port(Integer.valueOf(port));
    staticFileLocation("/public");

    String[] ingredients = { "Eggs", "Milk", "Rice", "White Rice", "Brown Rice", "Okra", "Tomatoes" };

    get("/ingredient", (req, res) -> {
      ArrayList<Ingredient> results = new ArrayList<Ingredient>();
      String searchIngredient = req.queryParams("ingredient");
      for (String ingred: ingredients) {
        if(ingred.toLowerCase().contains(searchIngredient.toLowerCase())){
          results.add(new Ingredient(ingred, "images/rice.jpg"));
        }
      }
      return results;
    }, new JsonTransformer());

//    get("/food", (req, res) -> {
//      String ingredients = req.queryParams("ingredients");
//
//
//      return "";
//    });


    get("/", (request, response) -> {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", "Hello World!");

        return new ModelAndView(attributes, "index.ftl");
    }, new FreeMarkerEngine());

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

}
