/*angular.module('hello', [])
  .controller('home', function($scope) {
	  console.log("teste");
	  $scope.greeting = {
    		id : "test",
    		content : "teteetests"
	  }
})*/

jobPortalApp.controller('controllerHome', function($scope,$http, $log, $state){
	$scope.greeting = {
			id : "Test101",
			content : "Hello, this is a test app"
	}
});