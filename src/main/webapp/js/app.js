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


//Conversation ID
var cid = '';

$.getJSON("http://localhost:8080/basic-angularjs-ee/resources/conversationwizard",
        function(data) {
            cid = data;
        });

messageApp.factory('messagesService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/all', {}, {
        query: {method: 'GET', isArray: true}
    });
});

messageApp.factory('messageEventPoller', function($http, $timeout) {
    var data = {data: ''};
    var poller = function() {
        $http.get('/basic-angularjs-ee/msgnotification?cid=' + cid).then(function(r) {
            data.data = r.data;
            $timeout(poller, 1);
        });
    };

    poller();
    return data;
});

messageApp.factory('createMessageService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message?cid=' + cid, {}, {
        create: {method: 'POST'}
    });
});

messageApp.factory('messageService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/:id?cid=' + cid, {}, {
        show: {method: 'GET'},
        update: {method: 'PUT', params: {id: '@id'}},
        delete: {method: 'DELETE', params: {id: '@id'}}
    });
});

messageApp.controller('messageCtrl', function($scope, messageEventPoller, createMessageService, messagesService, messageService) {
    $scope.messages = messagesService.query();
    $scope.newMsg = '';

    $scope.$watch(
            function() {
                return messageEventPoller.data;
            },
            function(messageEvent) {
                $scope.newMsg = messageEvent;
                var msg = messageEvent.message;
                if (messageEvent.eventType === 'CREATE') {
                    $scope.messages.push(msg);
                }
                else if (messageEvent.eventType === 'DELETE') {
                    $scope.messages.splice(findIndexOfElementById(msg.id, $scope.messages), 1);
                } else if (messageEvent.eventType === 'UPDATE') {
                    var msgEntry = $.grep($scope.messages, function(compMsg, index) {
                        return compMsg.id === msg.id;
                    })[0];
                    msgEntry.subject = msg.subject;
                    msgEntry.content = msg.content;
                }
            }
    );

    $scope.newMessage = function() {
        var newEntry = createMessageService.create($scope.message);
        $scope.comp = newEntry;
    };

    $scope.deleteMessage = function(msg) {
        messageService.delete({id: msg.id});
    };

    $scope.updateMessage = function(message) {
        delete message['event'];
        messageService.update(message);
    };

    function findIndexOfElementById(id, array) {
        for (var i = 0; i < array.length; i++) {
            if (array[i]['id'] === id) {
                return i;
            }
        }
        return -1;
    }
});

