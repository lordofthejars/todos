package org.superbiz.todos;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/todos")
@Singleton
public class UiApplication {

    @Inject
    @Data
    private List<JsonObject> listOfTodos;

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
        System.out.println(content.toString());
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
        System.out.println(listOfTodos.size());
        return Response.noContent().build();
    }
}
