package org.superbiz.todos;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/todos")
@Singleton
public class UiApplication {

    private static List<JsonObject> listOfTodos = new ArrayList<>();

    @PostConstruct
    public void init() {
        JsonObjectBuilder firstTodo = Json.createObjectBuilder();
        firstTodo.add("text", "First Todo");
        firstTodo.add("done", true);
        JsonObjectBuilder secondTodo = Json.createObjectBuilder();
        secondTodo.add("text", "Second Todo");
        secondTodo.add("done", false);

        listOfTodos.add(firstTodo.build());
        listOfTodos.add(secondTodo.build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTodos() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        listOfTodos.stream().forEach(todo -> arrayBuilder.add(todo));
        StringWriter content = new StringWriter();
        JsonWriter writer = Json.createWriter(content);
        writer.writeArray(arrayBuilder.build());

        return Response.ok(content.toString()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTodo(InputStream todo) {
        JsonReader reader = Json.createReader(todo);
        listOfTodos.add(reader.readObject());
        return Response.ok().build();
    }
    @DELETE
    @Path("{message}")
    public Response deleteTodo(@PathParam("message") String message) {
        Optional<JsonObject> text = listOfTodos.stream().filter(todo -> todo.getString("text").equals(message)).findFirst();
        if(text.isPresent()) {
            listOfTodos.remove(text.get());
        }
        return Response.noContent().build();
    }
}
