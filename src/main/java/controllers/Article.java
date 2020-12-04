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
    @Path("get/{ArticleID}") //Command
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetArticle(@PathParam("ArticleID") Integer ArticleID) { //PathParam gets the value at the end of the command
        System.out.println("Invoked Articles.ArticleGet() with ArticleID " + ArticleID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Articles.Title, Articles.Description, Topics.TopicName, Users.Username, Articles.DateAdded, Articles.Picture, Articles.ArticleBody\n" +
                    "FROM Articles JOIN Topics ON Articles.TopicID = Topics.TopicID JOIN Accesses ON Articles.ArticleID = Accesses.ArticleID JOIN Users ON Accesses.UserID = Users.UserID\n" +
                    "WHERE Articles.ArticleID = ?");
            ps.setInt(1, ArticleID);
            ResultSet results = ps.executeQuery();
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
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

}
