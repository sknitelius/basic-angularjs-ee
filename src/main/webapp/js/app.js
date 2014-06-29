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

var cid = '';

$.ajax({
    url: "http://localhost:8080/basic-angularjs-ee/resources/conversationwizard"
}).then(function(data) {
    console.log('cid: ' + data);
    cid = data;
});

var messageApp = angular.module('messageApp', ['ngResource']);

messageApp.factory('conversationService', function() {
//    var data = {cid: ''};
//    $http({method: "GET", url: '/basic-angularjs-ee/resources/conversationwizard'}, {cache: true})
//            .success(function(d) {
//                data.cid = d;
//            })
//            .error(function(data) {
//                alert("Could not initiate conversation" + data);
//            });
//    return data;
    var data = {cid: ''};
//    data.cid = cid.data;

    $.ajax({
        url: '/basic-angularjs-ee/resources/conversationwizard',
        async : false 
       })
       .done(function(d) {
            data.cid = d;
        })
       .fail(function(){
            alert("Could not initiate conversation" + data);  
        });
    return data;
});

messageApp.factory('messagesService', function($resource) {
    return $resource('/basic-angularjs-ee/resources/message/all', {}, {
        query: {method: 'GET', isArray: true}
    });
});

messageApp.factory('messageEventPoller', function($http, $timeout, conversationService) {
    var data = {data: ''};
    var poller = function() {
        $http.get('/basic-angularjs-ee/msgnotification?cid=' + conversationService.cid).then(function(r) {
            data.data = r.data;
            $timeout(poller, 1);
        });
    };
    poller();
    return data;
});

messageApp.factory('createMessageService', function($resource, conversationService) {
    return $resource('/basic-angularjs-ee/resources/message?cid=:cid', {}, {
        create: {method: 'POST', params: {cid: conversationService.cid}}
    });
});

messageApp.factory('messageService', function($resource, conversationService) {
    return $resource('/basic-angularjs-ee/resources/message/:id?cid=:cid', {}, {
        show: {method: 'GET'},
        update: {method: 'PUT', params: {id: '@id', cid: conversationService.cid}},
        delete: {method: 'DELETE', params: {id: '@id', cid: conversationService.cid}}
    });
});

messageApp.controller('messageCtrl', function($scope, messageEventPoller, createMessageService, messagesService, messageService, conversationService) {
    $scope.messages = messagesService.query();
    $scope.message = {id: '', subject: '', content: ''};
    $scope.cid = conversationService.cid;
    $scope.$watch(
            function() {
                return messageEventPoller.data;
            },
            function(messageEvent) {
                $scope.messageEvent = messageEvent;
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

