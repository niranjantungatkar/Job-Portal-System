jobPortalApp.controller('controllerVerify', function($scope, $stateParams, $state, $log, $http){
	
	$scope.invVerirficationMsg="";
	$scope.showInvMsg=false;
	if($state.params.profile !== null && $state.params.profile !== "" && $state.params.profile !== undefined) {
		
		console.log($state.params.profile);
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.profile.id !== null && $state.params.profile.id !== "" && $state.params.profile.id !== undefined)
	    	{
	      		localStorage.setItem('profile_id',JSON.stringify($state.params.profile.id));
	    	}
	    	if($state.params.profile.type !== null && $state.params.profile.type !== "" && $state.params.profile.type !== undefined)
	    	{
	      		localStorage.setItem('profile_type',JSON.stringify($state.params.profile.type));
	    	}
		}
	}
	
	
	$scope.profileId = JSON.parse(localStorage.getItem('profile_id'));
	$scope.profileType = JSON.parse(localStorage.getItem('profile_type'));
	
	var verification = {
			code:""
	}
	
	$scope.verify = function() {
		console.log($scope.profileId+" "+$scope.profileType+" "+$scope.verifcation);
		$http({
			method: "POST",
			url: '/verify',
			data: {
				"id": $scope.profileId,
				"type": $scope.profileType,
				"verificationCode": $scope.verification.code
			}
		}).success(function(data){
			if(data.result) {
				if($scope.header.session)
					$state.go('home',{},{reload:true});
				else
					$state.go('home.signin',{},{reload: true})
			} else if (data.code == "200") {
				if($scope.header.session)
					$state.go('home',{},{reload:true});
				else
					$state.go('home.signin',{},{reload: true})
			}
		}).error(function(data){
			$scope.invVerirficationMsg = data.msg;
			$scope.showInvMsg=true
		})
	}
})