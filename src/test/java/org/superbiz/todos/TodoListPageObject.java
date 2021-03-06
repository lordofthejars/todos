package org.superbiz.todos; /**
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.angular.findby.FindByNg;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@Location("index.html")
public class TodoListPageObject {

    @FindByNg(model = "todo.done")
    private List<WebElement> todos;

    @FindByNg(model = "todoText")
    private WebElement todoEntry;

    @FindByNg(action = "archive()")
    private WebElement archive;

    @FindByNg(action = "addTodo()")
    private WebElement addTodo;

    @FindByNg(repeat = "todo in todos")
    private List<WebElement> todoRepeat;

    @Drone
    private WebDriver driver;

    public List<TodoItem> openTasks() {
        final List<WebElement> currentTodos = todoRepeat;
        List<TodoItem> todoItems = currentTodos.stream().map((input) -> {
            final WebElement checkbox = input.findElement(By.tagName("input"));
            final WebElement description = input.findElement(By.tagName("span"));
            return new TodoItem(description.getText(), checkbox.isSelected());
        }).collect(Collectors.toList());

        return todoItems;
    }

    public void archive() {
        archive.click();
    }

    public void add(String todo) {
        todoEntry.sendKeys(todo);
        addTodo.submit();
    }

    public void tick(int positionOnTheList) {
        todoRepeat.get(positionOnTheList - 1).findElement(By.tagName("input")).click();
    }
}
