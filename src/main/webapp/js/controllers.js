'use strict';

/* Controllers */
var messageControllers = angular.module('messageControllers', []);

var messageApp = angular.module('messageApp', []);

messageApp.controller('MessageController', function($scope) {
    $scope.world = 'world';
});
