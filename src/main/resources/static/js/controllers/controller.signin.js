jobPortalApp.controller('controllerSignin', function($scope, $stateParams, $state, $log, $http){ 
	
	$scope.logincredentials = {
			email:"",
			password:""
	}
	
	$scope.login = function() {
		$http({
			method:'POST',
			url:'/login',
			data:{
				"email": $scope.logincredentials.email,
				"password": $scope.logincredentials.password
			}
		}).success(function(data){
			if(data.type == "jobseeker") {
				$scope.header.profile="jobseeker";
				$scope.header.verified=data.verified;
				$scope.header.session=true;
				$state.go('home.jobseekerprofile', { profileDet: { id: data.id, type: data.type, verified: data.verified } })
			} else if(data.type == "company") {
				$scope.header.profile="company";
				$scope.header.verified=data.verified;
				$scope.header.session=true;
				$state.go('home.companyprofile', { companyDet: { id: data.id, type: data.type, verified: data.verified } })
			}
		})
	}
});