(function() {
  var app = angular.module("AHNAdminApp", ["ngRoute", "ngAnimate"]);
  app.constant('urls', {
       BASE: '',
       BASE_API: ''
   });
  app.config(function($routeProvider, $httpProvider) {
    $routeProvider
    .when("/", {
      templateUrl: "logIn.htm",
      controller: "AHNLogInCtrl"
    })
    .when("/admin", {
      templateUrl: "admin.htm",
      controller: "AHNAdminCtrl"
    })
    .when("/finished", {
      templateUrl: "exit.htm",
      controller: "AHNExitCtrl"
    })
  });

  app.controller("AHNLogInCtrl", function($scope, $location, $log, $rootScope) {
    $scope.badLogin = false;

    $rootScope.user = {
      grant_type:"password",
      username: "",
      password: "",
      client_id: "AHNSurveyApp",
    };

    $scope.submitLogIn = function() {
      $rootScope.user.encoded = btoa($rootScope.user.username:$rootScope.user.password);
      var request = {
        method: 'POST',
        url: "api/oauth2/token",
        headers: {
          "Authorization": "Basic " + $scope.encoded,
          "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
        },
        user: $httpParamSerializer($scope.user)
      }
      $http(request).then(function(data){
        $http.defaults.headers.common.Authorization = 'Bearer ' + data.access_token;
        $cookies.put("access_token", data.access_token);
      });
    }

  });

  app.controller("AHNAdminCtrl", function($scope, $http, $log, $location, $rootScope) {
    $rootScope.loggedIn = true;
    $scope.logInShow = true;
    $scope.badLogin = false;
    $scope.phonePattern = /^([0-1]([\s-./\\])?)?(\(?[2-9]\d{2}\)?|[2-9]\d{3})([\s-./\\])?(\d{3}([\s-./\\])?\d{4}|[a-zA-Z0-9]{7})$/;
    $scope.emailPattern = /[\w-]+@([\w-]+\.)+[\w-]+/;

    // Pull from server:
    $scope.tests = [
    {name: "COPD", value: "COPD"},
    {name: "Other", value: "Other"}
    ];
    $scope.languages = [
    {name: "English", value: "en-US"},
    {name: "Spanish", value: "es-MX"}
    ];

    $rootScope.patient = {};
    $rootScope.newUser = {};
    $rootScope.newUser["lang"] = $scope.languages[0].value;
    $rootScope.newUser["testToken"] = $scope.tests[0].value;

    $scope.viewDataForm = function() {
      $scope.newUserShow = false;
      $scope.logInShow = true;
    };
    $scope.newUserForm = function() {
      $scope.logInShow = false;
      $scope.newUserShow = true;

    };

    $scope.submitLogIn = function() {
      $http.get("api/users/" + $rootScope.patient.username).then(function(response) {
        $rootScope.patient = response.data;
      });
    };

    $scope.submitNewUser = function() {
      $http.get("api/oauth2/new").then(function(response) {
        $rootScope.newUser = response.data;
      });
    };

  });

  app.controller("AHNExitCtrl", function($scope, $http, $log, $location, $rootScope) {

  });

})();
