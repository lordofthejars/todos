/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.superbiz.todos;

import java.io.StringReader;
import java.net.URL;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonReader;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class TodoBasicTest {
    @Deployment
    public static WebArchive createDeployment() {
        return TestUtils.createWebArchive("basic-app.war");
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @FindByJQuery(".todo-list")
    private WebElement todoList;

    @Inject
    private UiApplication application;

    @Before
    public void loadPage() {
        browser.navigate().to(contextRoot + "index-ng-app.html");
    }

    @Test
    @InSequence(0)
    public void emptyList() {
        application.deleteTodo("First Todo");
        application.deleteTodo("Second Todo");
        String json = application.getAllTodos().getEntity().toString();
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        assertThat(jsonReader.readArray()).hasSize(0);
    }

    @Test
    @RunAsClient
    @InSequence(1)
    public void assertEmptyList() {
        assertThat(todoList.findElements(By.tagName("li"))).hasSize(0);
    }
}
