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

@Path("user/") //HTTP request handler
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class User{
    @POST
    @Path("login") //command logs a user in
    public String UsersLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) { //PathParam gets the value at the end of the command
        System.out.println("Invoked loginUser() on path user/login");
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password, Admin FROM Users WHERE Username = ?"); //pre-prepared SQL statement
            ps1.setString(1, Username); //identifies a variable to be used in the SQL
            ResultSet loginResults = ps1.executeQuery(); //runs SQL
            if (loginResults.next() == true) {
                String correctPassword = loginResults.getString(1);
                int Admin = loginResults.getInt(2);
                if (Password.equals(correctPassword)) {
                    String Token = UUID.randomUUID().toString(); //converts Token to string
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?"); //pre-prepared SQL statement
                    ps2.setString(1, Token); //these lines identify the variables to be used in the SQL
                    ps2.setString(2, Username);
                    ps2.executeUpdate(); //runs SQL
                    JSONObject userDetails = new JSONObject(); //creates a new JSON object using the values below
                    userDetails.put("Username", Username);
                    userDetails.put("Token", Token);
                    userDetails.put("Admin", Admin);
                    return userDetails.toString(); //converts userDetails to a string
                } else {
                    return "{\"Error\": \"Incorrect password!\"}";
                }
            } else {
                return "{\"Error\": \"Incorrect username.\"}";
            }
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"Error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("logout") //command logs a user out
    public static String logout(@CookieParam("Token") String Token){
        try{
            System.out.println("user/logout "+ Token);
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token=?"); //pre-prepared SQL statement
            ps.setString(1, Token); //identifies a variable to be used in the SQL
            ResultSet logoutResults = ps.executeQuery(); //runs SQL
            if (logoutResults.next()){
                int UserID = logoutResults.getInt(1);
                //Set the token to null to indicate that the user is not logged in
                PreparedStatement ps1 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?"); //pre-prepared SQL statement
                ps1.setInt(1, UserID); //identifies a variable to be used in the SQL
                ps1.executeUpdate(); //runs SQL
                return "{\"status\": \"OK\"}";
            } else {
                return "{\"error\": \"Invalid token!\"}";

            }
        } catch (Exception ex) { //catches any errors to make debugging easier
            System.out.println("Database error during /users/logout: " + ex.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("add") //command creates a new user
    public String UsersAdd(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password, @FormDataParam("DateJoined") String DateJoined, @FormDataParam("Admin") Integer Admin, @FormDataParam("Token") String Token){
        //PathParam gets the value at the end of the command
        System.out.println("Invoked Users.UserAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (Username, Password, DateJoined, Admin, Token) VALUES (?, ?, ?, ?, ?)"); //pre-prepared SQL statement
            ps.setString(1, Username); //these lines identify the variables to be used in the SQL
            ps.setString(2, Password);
            ps.setString(3, DateJoined);
            ps.setInt(4, Admin);
            ps.setString(5, Token);
            ps.execute(); //runs SQL
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    /*/ I was going to use this for identifying an admin, but decided to instead put it in user/login
    @GET
    @Path("identify") //command updates a user's attributes
    public String identifyAdmin(@FormDataParam("Token") String Token) { //PathParam gets the values at the end of the command
        try {
            System.out.println("Invoked User.Identify() Token=" + Token);
            PreparedStatement ps = Main.db.prepareStatement("SELECT Admin FROM Users WHERE Token = ?"); //Pre-prepared SQL statement
            ps.setString(1, Token); //identifies a variable to be used in the SQL
            ResultSet admin = ps.executeQuery(); //runs SQL
            int Admin = admin.getInt(); //turns it into an integer
            JSONObject response = new JSONObject();  //creates a new JSON object using the value below
            if (Admin == 1){
                response.put("Admin", Admin.getInt(1));
                return "{\"OK\": \"Admin Recognised\"}";
            } else{
                response.put("Admin", Admin.getInt(1));
                return "{\"OK\": \"Not Admin\"}";
            }
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to bookmark item, please see server console for more info.\"}";
        }
    }
    */

    @GET
    @Path("list") //Command lists all the users
    public String UserList() {
        System.out.println("Invoked Users.UserList()"); //Prints for error checking
        JSONArray response = new JSONArray(); //creates a new array
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Users"); //Pre-prepared SQL statement
            ResultSet results = ps.executeQuery();  //runs SQL
            while (results.next()==true) {
                JSONObject row = new JSONObject(); //creates a new JSON object using the values below
                row.put("UserID", results.getInt(1));
                row.put("Username", results.getString(2));
                row.put("Password", results.getString(3));
                response.add(row);
            }
            return response.toString(); //converts response to string
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @GET
    @Path("getUser/{UserID}") //command gets a single user
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUser(@PathParam("UserID") Integer UserID) { //PathParam gets the value at the end of the command
        System.out.println("Invoked Users.GetUser() with UserID " + UserID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Username, Password, Token FROM Users WHERE UserID = ?"); //pre-prepared SQL statement
            ps.setInt(1, UserID); //identifies a variable to be used in the SQL
            ResultSet results = ps.executeQuery(); //runs SQL
            JSONObject response = new JSONObject();  //creates a new JSON object using the values below
            if (results.next()== true) {
                response.put("Username", results.getString(1));
                response.put("Password", results.getString(2));
                response.put("Token", results.getString(3));
            }
            return response.toString(); //converts response to string
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update") //command updates a user's attributes
    public String updateUser(@FormDataParam("UserID") Integer UserID, @FormDataParam("Username") String Username) { //PathParam gets the value at the end of the command
        try {
            System.out.println("Invoked Users.Update() UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Username = ? WHERE UserID = ?"); //pre-prepared SQL statement
            ps.setString(1, Username); //these lines identify the variables to be used in the SQL
            ps.setInt(2, UserID);
            ps.execute(); //runs SQL
            return "{\"OK\": \"Users updated\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{UserID}") //command deletes a user
    public String DeleteUser(@PathParam("UserID") Integer UserID) throws Exception { //PathParam gets the value at the end of the command
        System.out.println("Invoked Users.DeleteUser()");
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?"); //pre-prepared SQL statement
            ps.setInt(1, UserID); //identifies a variable to be used in the SQL
            ps.execute(); //runs SQL
            return "{\"OK\": \"User deleted\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


}

