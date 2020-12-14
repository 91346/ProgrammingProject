package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("article/") //HTTP request handler
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Article{
    @GET
    @Path("get/{ArticleID}") //Command gets a single article
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetArticle(@PathParam("ArticleID") Integer ArticleID) { //PathParam gets the value at the end of the command
        System.out.println("Invoked Articles.ArticleGet() with ArticleID " + ArticleID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Articles.Title, Articles.Description, Topics.TopicName, Users.Username, Articles.DateAdded, Articles.Picture, Articles.ArticleBody\n" +
                    "FROM Articles JOIN Topics ON Articles.TopicID = Topics.TopicID JOIN Accesses ON Articles.ArticleID = Accesses.ArticleID JOIN Users ON Accesses.UserID = Users.UserID\n" +
                    "WHERE Articles.ArticleID = ?"); //pre-prepared SQL statement
            ps.setInt(1, ArticleID); //identifies a variable to be used in the SQL
            ResultSet results = ps.executeQuery(); //runs SQL
            JSONObject response = new JSONObject(); //creates a new JSON object using the values below
            if (results.next()== true) {
                response.put("Title", results.getString(1));
                response.put("Description", results.getString(2));
                response.put("Topic", results.getString(3));
                response.put("Author", results.getString(4));
                response.put("Date", results.getString(5));
                response.put("Picture", results.getString(6));
                response.put("Article", results.getString(7));
            }
            return response.toString(); //converts the response to a string
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("add") //command creates a new article
    public String ArticleAdd(@FormDataParam("Title") String Title, @FormDataParam("Description") String Description, @FormDataParam("Topic") String Topic, @FormDataParam("Author") String Author, @FormDataParam("Date") String Date, @FormDataParam("Picture") String Picture, @FormDataParam("Article") String Article){
        //PathParam gets the values at the end of the command
        System.out.println("Invoked Article.ArticleAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Articles (Title, Description, Topic, Author, Date, Picture, Article) VALUES (?, ?, ?, ?, ?, ?, ?)"); //pre-prepared SQL statement
            ps.setInt(1, Title); //these lines identify the variables to be used in the SQL
            ps.setString(2, Description);
            ps.setString(3, Topic);
            ps.setString(4, Author);
            ps.setString(5, Date);
            ps.setString(6, Picture);
            ps.setString(7, Article);
            ps.execute(); //runs SQL
            return "{\"OK\": \"Added Article.\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{ArticleID}") //command deletes an article
    public String DeleteArticle(@PathParam("ArticleID") Integer ArticleID) throws Exception { //PathParam gets the value at the end of the command
        System.out.println("Invoked Articles.DeleteArticle()");
        if (ArticleID == null) {
            throw new Exception("ArticleID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Articles WHERE ArticleID = ?"); //pre-prepared SQL statement
            ps.setInt(1, ArticleID); //identifies a variable to be used in the SQL
            ps.execute(); //runs SQL
            return "{\"OK\": \"Article deleted\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("bookmark") //command updates a user's attributes
    public String bookmarkAdd(@FormDataParam("ArticleID") Integer ArticleID, @FormDataParam("Token") String Token) { //PathParam gets the values at the end of the command
        try {
            System.out.println("Invoked Article.Bookmark() ArticleID=" + ArticleID);
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?"); //SQL statement
            ps.setString(1, Token); //identifies a variable to be used in the SQL
            ResultSet UserID = ps.executeQuery(); //runs SQL
            PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Accesses SET Bookmarked = 1 WHERE UserID = ? AND ArticleID = ?"); //pre-prepared SQL statement
            ps2.setString(1, UserID); //these lines identify the variables to be used in the SQL
            ps2.setInt(2, ArticleID);
            ps2.execute(); //runs SQL
            return "{\"OK\": \"Article bookmarked\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to bookmark item, please see server console for more info.\"}";
        }
    }

}
