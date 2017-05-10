/*
 * Home Controller
 */

jobPortalApp.controller('controllerHome',function($scope, $http, userSession) {
	

	$scope.profile = {
			type:""
	}
	
	$scope.signindata = {
			
	}
	
	$scope.userdata = { 
		fname:"",
		lname:"",
		uemail:"",
		upassword:""
	}
	
	$scope.companydata = { 
		cname:"",
		cemail:"",
		cpassword:""
	}

	/* Check for user session or company session.
	 * If valid company or user session redirect to correct state.
	 */
	if(userSession != null) {
		if(userSession.data.type == "company") {
			$scope.profiletype="company";
			$scope.isverified=userSession.data.verified;
			//state.go('home.jobseekerprofile', { companyDet: { name: userSession.id } })
		} else if (userSession.type == "jobseeker") {
			$scope.profiletype="jobseeker";
			$scope.isverified=userSession.data.verified;
			//$state.go('home.companyprofile', { profileDet: { id: userSession.id } }) 
		}
	} 
	
	
	/*function validate() {
		
	}*/
	
	$scope.signup = function () {
	
		if($scope.profile.type == "company") {
			$http({
				method : "POST",
				url : '/company',
				data : {
					name : $scope.companydata.cname,
					email : $scope.companydata.cemail,
					password : $scope.companydata.cpassword
				}
			}).success(function(data){
				console.log(data)
				//state.go(to verification page for company) // check logic if it can be generic
			})
		} else if($scope.profile.type == "user") {
			$http({
				method : "POST",
				url : '/jobseeker',
				data : {
					firstname : $scope.userdata.firstname,
					lastname : $scope.userdata.lastname,
					email : $scope.userdata.uemail,
					password : $scope.userdata.upassword
				}
			}).success(function(data) {
				console.log(data);
				//state.go(to verification page for job seeker
			})
		}
	} 
});