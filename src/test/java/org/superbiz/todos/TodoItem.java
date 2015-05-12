package org.superbiz.todos;

/**
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

public class TodoItem {

    private final String description;

    private final boolean selected;

    public TodoItem(String description, boolean selected) {
        this.description = description;
        this.selected = selected;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSelected() {
        return selected;
    }

    public static TodoItem selectedTodo(String description) {
        return new TodoItem(description, true);
    }

    public static TodoItem newTodo(String description) {
        return new TodoItem(description, false);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TodoItem)) {
            return false;
        }

        final TodoItem todoItem = (TodoItem) o;

        if (selected != todoItem.isSelected())  {
            return false;
        }

        if (!description.equals(todoItem.getDescription())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }
}
