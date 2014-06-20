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

messageApp.run(function(newMessagePoller) {
});

messageApp.factory('messagesService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/all', {}, {
        query: {method: 'GET', isArray: true}
    });
});

messageApp.factory('newMessagePoller', function($http, $timeout, $q) {
    var data = {data: ''};
    var poller = function() {
        $http.get('/basic-angularjs-ee/newmsg').then(function(r) {
            data.data = r.data;
            $timeout(poller, 1);
        });
    };

    poller();
    return data;
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
    $scope.newMsg = '';

    $scope.$watch(
            function() {
                return newMessagePoller.data;
            },
            function(messageEvent) {
                $scope.newMsg = messageEvent;
                if (messageEvent.event === 'CREATE') {
                    $scope.messages.push(messageEvent);
                }
                else if (messageEvent.event === 'DELETE') {
                    $scope.messages.splice(findIndexForId(messageEvent.id, $scope.messages),1);
                } else if (messageEvent.event === 'UPDATE') {
                    var oldMsg = $.grep($scope.messages, function(msg, index) {
                        return msg.id === messageEvent.id;
                    })[0];
                    oldMsg.subject = messageEvent.subject;
                    oldMsg.content = messageEvent.content;
//                    $scope.messages[$.inArray(newMsg.id, $scope.messages)] = newMsg;
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

    function findIndexForId(id, array) {
        for (var i = 0; i < array.length; i++) {
            if (array[i]['id'] === id) {
                return i;
            }
        }
        return -1;
    }
});

