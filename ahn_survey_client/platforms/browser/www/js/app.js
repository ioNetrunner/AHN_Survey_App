(function() {
  var app = angular.module('conditionSurvey', []);

  app.controller('SurveyController', ['$http', function($http) {

    this.survey = this;
    survey.questions = [];

    $scope.range = function(n) {
        return new Array(n);
    };

    // Will need to use variable to get condition/language.
    $http.get('../json/COPD_en-US.json').success(function(data){
      survey.questions = data;
    }); // End http get request.
  }]); // End app.controller.
})(); // End angular function.