package org.superbiz.todos;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.superbiz.todos.TodoItem.newTodo;

/**
 * Created by alsoto on 11/05/15.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TodoTest {

    @Deployment
    public static WebArchive createDeployment() {
        GenericArchive webAppDirectory = ShrinkWrap
                .create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("src/main/resources/app").as(GenericArchive.class);

        return ShrinkWrap.create(WebArchive.class, "angular-test.war")
                         .addClasses(ApplicationConfig.class, UiApplication.class)
                         .merge(webAppDirectory, "/",
                                 Filters.include(".*\\.(js|css|html|xml)$"))
                         .addAsLibraries(Maven.resolver().resolve("org.glassfish:javax.json:1.0.4")
                                 .withTransitivity().as(JavaArchive.class))
                         .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @Page
    private TodoListPageObject todoList;

    @Before
    public void loadPage() {
        browser.navigate().to(contextRoot + "index.html");
    }

    @Test
    public void should_have_two_items_on_todo_list_when_started() {
        assertThat(todoList.openTasks()).hasSize(2);
    }

    @Test
    public void should_archive_item_which_is_selected_by_default() {
        // when
        todoList.archive();

        // then
        assertThat(todoList.openTasks()).hasSize(1);
    }

    @Test
    public void should_add_new_todo_item() {
        // when
        todoList.add("This is a new TODO item");

        // then
        assertThat(todoList.openTasks()).hasSize(3)
                .contains(newTodo("This is a new TODO item"));
    }
}
