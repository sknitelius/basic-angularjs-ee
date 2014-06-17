/* 
 * Copyright 2014 Stephan Knitelius <stephan@knitelius.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var messageApp = angular.module('messageApp', ['ngResource']);

messageApp.factory('messagesService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/all', {}, {
        query: {method: 'GET', isArray: true}
    });
});

messageApp.factory('createMessageService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message', {}, {
        create: {method: 'POST'}
    });
});

messageApp.factory('messageService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/:id', {}, {
        show: {method: 'GET'},
        update: {method: 'PUT', params: {id: '@id'}},
        delete: {method: 'DELETE', params: {id: '@id'}}
    });
});

messageApp.controller('messageController', function($scope, createMessageService, messagesService, messageService) {
    $scope.messages = messagesService.query();

    (function poll() {
    $.ajax({
        url: "http://localhost:8080/basic-angularjs-ee/newmsg",
        success: function(data) {
            $scope.newMsg = data;
        },
        dataType: "text", complete: poll, timeout: 30000
    });
})(); 

    $scope.newMessage = function() {
        createMessageService.create($scope.message);
        $scope.messages = messagesService.query();
    };
    
    $scope.deleteMessage = function(msgId) {
        messageService.delete({id: msgId});
        $scope.messages = messagesService.query();
    };

    $scope.updateMessage = function(message) {
        messageService.update(message);
    };
});

