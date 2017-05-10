/*
 * Home Controller
 */

jobPortalApp.controller('controllerHome',function($scope, $http, $state, userSession) {
	
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
	
	$scope.header = {
			profile:"",
			verified:"",
			session:false
	}

	/* Check for user session or company session.
	 * If valid company or user session redirect to correct state.
	 */
	if(userSession.data != null && userSession.data != "" && userSession != undefined) {
		$scope.header.session=true;
		$scope.header.verified=userSession.data.verified;
		if(userSession.data.type == "company") {
			$scope.header.profile = "company";
			$state.go('home.companyprofile', { companyDet: { name: userSession.data.id, type : userSession.data.type, verified: userSession.data.verified } })
		} else if (userSession.data.type == "jobseeker") {
			$scope.header.profile = "jobseeker";
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
	
		var data = {};
		var url = "";
		if($scope.profile.type == "company") {
			data = {
				"name" : $scope.companydata.cname,
				"email" : $scope.companydata.cemail,
				"password" : $scope.companydata.cpassword
			}
			url="/company"
			
		} else if($scope.profile.type == "user") {
			data = {
				"email" : $scope.userdata.uemail,
				"firstname" : $scope.userdata.fname,
				"lastname" : $scope.userdata.lname,
				"password" : $scope.userdata.upassword
			}
			url="/jobseeker"
		}
		
		$http({
			method : "POST",
			url : url,
			data : data
		}).success(function(data){
			console.log(data)
			$state.go('home.verify', { profile: { id: data.id, type: data.type , verified: data.verified } } ) 
		})
	} 
});