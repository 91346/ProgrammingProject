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
    @GET
    @Path("list") //Command
    public String UserList() {
        System.out.println("Invoked Users.UserList()"); //Prints for error checking
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Users"); //Pre-prepared SQL statement
            ResultSet results = ps.executeQuery();
            while (results.next()==true) {
                JSONObject row = new JSONObject(); //creates a new JSON object using the values below
                row.put("UserID", results.getInt(1));
                row.put("Username", results.getString(2));
                row.put("Password", results.getString(3));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @GET
    @Path("getUser/{UserID}") //command
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetUser(@PathParam("UserID") Integer UserID) { //PathParam gets the value at the end of the command
        System.out.println("Invoked Users.GetUser() with UserID " + UserID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Username, Password, Token FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();  //creates a new JSON object using the values below
            if (results.next()== true) {
                response.put("Username", results.getString(1));
                response.put("Password", results.getString(2));
                response.put("Token", results.getString(3));
            }
            return response.toString();
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("add")
    public String UsersAdd(@FormDataParam("UserID") Integer UserID, @FormDataParam("Username") String Username, @FormDataParam("Password") String Password, @FormDataParam("DateJoined") String DateJoined, @FormDataParam("Admin") Integer Admin, @FormDataParam("Token") String Token){
        //PathParam gets the value at the end of the command
        System.out.println("Invoked Users.UserAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, Username, Password, DateJoined, Admin, Token) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, Username);
            ps.setString(3, Password);
            ps.setString(4, DateJoined);
            ps.setInt(5, Admin);
            ps.setString(6, Token);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    public String updateUser(@FormDataParam("UserID") Integer UserID, @FormDataParam("Username") String Username) { //PathParam gets the value at the end of the command
        try {
            System.out.println("Invoked Users.Update() UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Username = ? WHERE UserID = ?");
            ps.setString(1, Username);
            ps.setInt(2, UserID);
            ps.execute();
            return "{\"OK\": \"Users updated\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/{UserID}")
    public String DeleteUser(@PathParam("UserID") Integer UserID) throws Exception { //PathParam gets the value at the end of the command
        System.out.println("Invoked Users.DeleteUser()");
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();
            return "{\"OK\": \"User deleted\"}";
        } catch (Exception exception) { //catches any errors to make debugging easier
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("login")
    public String UsersLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) { //PathParam gets the value at the end of the command
        System.out.println("Invoked loginUser() on path user/login");
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps1.setString(1, Username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next() == true) {
                String correctPassword = loginResults.getString(1);
                if (Password.equals(correctPassword)) {
                    String Token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1, Token);
                    ps2.setString(2, Username);
                    ps2.executeUpdate();
                    JSONObject userDetails = new JSONObject(); //creates a new JSON object using the values below
                    userDetails.put("Username", Username);
                    userDetails.put("Token", Token);
                    return userDetails.toString();
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
    @Path("logout")
    public static String logout(@CookieParam("Token") String Token){
        try{
            System.out.println("user/logout "+ Token);
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token=?");
            ps.setString(1, Token);
            ResultSet logoutResults = ps.executeQuery();
            if (logoutResults.next()){
                int UserID = logoutResults.getInt(1);
                //Set the token to null to indicate that the user is not logged in
                PreparedStatement ps1 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps1.setInt(1, UserID);
                ps1.executeUpdate();
                return "{\"status\": \"OK\"}";
            } else {
                return "{\"error\": \"Invalid token!\"}";

            }
        } catch (Exception ex) { //catches any errors to make debugging easier
            System.out.println("Database error during /users/logout: " + ex.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }
}

