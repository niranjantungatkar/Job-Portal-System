/*
 * Home Controller
 */

jobPortalApp.controller('controllerHome',function($scope, $http, userSession) {
	
	//for storing the type of profile of the current user.
	$scope.profile = {
			type:""
	}
	
	//Sing in variables
	$scope.signindata = {
			email:"",
			password:""
	}
	
	//Signup variables for companay and user
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
			state.go('home.companyprofile', { companyDet: { name: userSession.data.id, type : userSession.data.type, verified: userSession.data.verified } })
		} else if (userSession.type == "jobseeker") {
			$scope.profiletype="jobseeker";
			$scope.isverified=userSession.data.verified;
			$state.go('home.jobseekerprofile', { profileDet: { id: userSession.data.id, type: userSession.data.type, verified: userSession.data.verified } }) 
		}
	} 
	
	/*
	 * login function
	 * Requires signin data object
	 */
	$scope.login = function () {
		$http({
			method: "POST",
			url: '/login',
			data: {
				"email": $scope.signindata.email,
				"password": $scope.signindata.password
			}
		}).success(function(data) {
			if(data.type == "jobseeker") {
				$state.go('home.jobseekerprofile', { profileDet: { id: data.id, type: data.type, verified: data.verified } })
			} else if(data.type == "company") {
				$state.go('home.companyprofile', { companyDet: { id: data.id, type: data.type, verified: data.verified } })
			}
		})
	}
	
	
	/*
	 * Sign up function
	 * Requires userdata & companydata $scope objects
	 */
	$scope.signup = function () {
	
		if($scope.profile.type == "company") {
			$http({
				method : "POST",
				url : '/company',
				data : {
					"name" : $scope.companydata.cname,
					"email" : $scope.companydata.cemail,
					"password" : $scope.companydata.cpassword
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
					"email" : $scope.userdata.uemail,
					"firstname" : $scope.userdata.fname,
					"lastname" : $scope.userdata.lname,
					"password" : $scope.userdata.upassword
				}
			}).success(function(data) {
				console.log(data);
				//state.go(to verification page for job seeker
			})
		}
	} 
	
	
	
	
	
});