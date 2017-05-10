jobPortalApp.controller('controllerVerify', function($scope, $stateParams, $state, $log, $http){
	
	console.log($state.params.profile.id+" "+$state.params.profile.type)
	if (typeof(Storage) !== "undefined") {
    	if($state.params.profile.id != null && $state.params.profile.id != "" && $state.params.profile.id != undefined)
    	{
      		localStorage.setItem('profile_id',JSON.stringify($state.params.profile.id));
    	}
    	if($state.params.profile.type != null && $state.params.profile.type != "" && $state.params.profile.type != undefined)
    	{
      		localStorage.setItem('profile_type',JSON.stringify($state.params.profile.type));
    	}
	}
	
	$scope.profileId = JSON.parse(localStorage.getItem('profile_id'));
	$scope.profileType = JSON.parse(localStorage.getItem('profile_type'));
	console.log($scope.profileId+" "+$scope.profileType);
	
	var verification = {
			code:""
	}
	
	$scope.verify = function() {
		console.log($scope.profileId+" "+$scope.profileType+" "+$scope.verification.code)
		$http({
			method: "POST",
			url: '/verify',
			data: {
				"id": $scope.profileId,
				"type": $scope.profileType,
				"verificationCode": $scope.verification.code
			}
		}).success(function(data){
			console.log(data)
			if(data.result) {
				$state.go('home');
			}
			
		})
	}
})