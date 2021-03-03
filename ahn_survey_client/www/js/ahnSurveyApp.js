/**
 * Copyright 2017 - Allegheny Health Network
 * @author Io Netrunner
 */
 (function() {
  var app = angular.module("AHNSurveyApp", ["ngResource", "ngRoute", "ngCookies", "ngStorage"]);
  // Load template and controller based on route.
  app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
      templateUrl: "logIn.htm",
      controller: "AHNLogInCtrl"
    })
    .when("/survey", {
      templateUrl: "survey.htm",
      controller: "AHNSurveyCtrl as survey"
    })
    .when("/finished", {
      templateUrl: "exit.htm",
      controller: "AHNExitCtrl"
    })
    .otherwise({
      redirectTo: "/"
   });
  });

  // Controller for Log in page.
  app.controller("AHNLogInCtrl", function($scope, $resource, $http, $httpParamSerializer, $cookies, $location, $log, $rootScope, $localStorage) {
    $scope.badLogin = false;
    $rootScope.user = {
      grant_type:"password",
      username: "",
      password: "",
      client_id: "AHNSurveyApp",
      lang: "",
      test: "",
      collection: 000
    };

    $scope.submitLogIn = function() {
      var encoded = btoa($rootScope.user.username + ":" + $rootScope.user.password);
      $log.log($rootScope.user);
      var request = {
        method: "POST",
        url: "api/oauth2/access_token",
        headers: {
          "Authorization": "Basic " + encoded,
          "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
        },
        user: $httpParamSerializer($scope.user)
      }
      $http(request).then(function(data){
        $http.defaults.headers.common.Authorization = "Bearer " + data.access_token;
        $localStorage.access_token = data.access_token;
        $location.path("survey");
      }, function(data) {
        $log.log("Error logging in.")
      });
      // Go to survey no matter what right now.
      // Only go on success once working.
      $location.path("survey");
    }

  });

  app.controller("AHNSurveyCtrl", function($scope, $http, $log, $location, $rootScope, $localStorage) {
    $scope.survey = [];
    $scope.testBegin = Date.now();
    $scope.currQuestion = 0;
    $scope.questionIDs = [];
    $scope.filledSurvey = {};
    $scope.filledSurvey.startTime = $scope.testBegin;
    $scope.showSurveySubmit = false;

    // Test data:
    $rootScope.user.test = "COPD";
    $rootScope.user.lang = "en-US";

    $http.get("questions/" + $rootScope.user.test + "_" + $rootScope.user.lang + ".json")
      .then(function(response){
        $scope.survey = response.data;
      }, function(data) {
        $log.log("Error getting survey.")
      });
    $scope.answerQuestion = function() {
      // Prevents going to next question if current question has no value.
      if ($scope.filledSurvey["question" + $scope.currQuestion]) {
        $scope.currQuestion++;
        if ($scope.currQuestion == $scope.questionIDs.length) {
          $scope.showSurveySubmit = true;
        }
      }
    }
    $scope.redoSurvey = function() {
      $scope.currQuestion = 0;
      $scope.showSurveySubmit = false;
    }
    $scope.submitSurvey = function() {
      $scope.filledSurvey.endTime = Date.now();
      $scope.filledSurvey.collection = $rootScope.user.collection;
      $log.log($scope.filledSurvey);
      $http.post("/api/survey/submit", $scope.filledSurvey)
        .then(function(data){
          $location.path("finished");
        }, function(data) {
          $log.log("Error submitting response.")
        });
      $location.path("finished");
    }
  });

  app.controller("AHNExitCtrl", function($scope, $http, $log, $location, $rootScope) {

  });

})();
