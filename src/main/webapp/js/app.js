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

messageApp.run(function(newMessagePoller) {});

messageApp.factory('messagesService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/all', {}, {
        query: {method: 'GET', isArray: true}
    });
});

messageApp.factory('newMessagePoller', function($http, $timeout) {
    var data = {response: {}};
    var poller = function() {
        $http.get('/basic-angularjs-ee/newmsg').then(function(r) {
            data.response = r.data;
            $timeout(poller, 1);
        });
    };
    poller();
    return {data: data};
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

messageApp.controller('messageCtrl', function($scope, newMessagePoller, createMessageService, messagesService, messageService) {
    $scope.messages = messagesService.query();
    $scope.newMsg = newMessagePoller.data;

    $scope.newMessage = function() {
        var newEntry = createMessageService.create($scope.message);
        $scope.messages.push(newEntry);
    };

    $scope.deleteMessage = function(msg) {
        messageService.delete({id: msg.id});
        $scope.messages.splice($.inArray(msg, $scope.messages), 1);
    };

    $scope.updateMessage = function(message) {
        messageService.update(message);
    };
});

