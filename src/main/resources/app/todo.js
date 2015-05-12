/*
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
angular.module('todo.controller', []).controller('TodoCtrl', function($scope, $http, $q) {
  $scope.todos = [];

  var refresh = function() {$http.get('rest/todos')
            .success(function(data){
                $scope.todos = data;
            });
  }
  refresh();
  $scope.addTodo = function() {
  $http.post('rest/todos', {text:$scope.todoText, done:false})
       .success(function(data) {
        refresh();
        $scope.todoText = '';
    })
  };

  $scope.remaining = function() {
    var count = 0;
    angular.forEach($scope.todos, function(todo) {
      count += todo.done ? 0 : 1;
    });
    return count;
  };

  $scope.archive = function() {
    var oldTodos = $scope.todos;
    $scope.todos = [];
    var deletes = [];
    angular.forEach(oldTodos, function(todo) {
      if (!todo.done) {
        deletes.push($http.delete('rest/todos/'+todo.text));
      }
    });
    $q.all(deletes).then(function() {
        refresh();
    });
  };
});
