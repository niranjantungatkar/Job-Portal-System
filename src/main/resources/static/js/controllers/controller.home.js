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
		cpassword:"",
		website:"",
		logoURL:"",
		address:"",
		companyDesc:""
	}
	
	$scope.header = {
			profile:"",
			verified:"",
			session:false
	}

	/* Check for user session or company session.
	 * If valid company or user session redirect to correct state.
	 */
	if(userSession.data !== null && userSession.data !== "" && userSession !== undefined) {
		$scope.header.session=true;
		$scope.header.verified=userSession.data.verified;
		
		if (typeof(Storage) !== "undefined") {
		   	if(userSession.data.id !== null && userSession.data.id !== "" && userSession.data.id !== undefined)
		   	{
		   		
		   		localStorage.setItem('sessionId',JSON.stringify(userSession.data.id));
		   	}
		   	if(userSession.data.verified !== null && userSession.data.verified !== "" && userSession.data.verified !== undefined)
		   	{
		   		
		   		localStorage.setItem('sessionVerified',JSON.stringify(userSession.data.verified));
		   	}
		   	if(userSession.data.type !== null && userSession.data.type !== "" && userSession.data.type !== undefined)
		   	{
		   		
		   		localStorage.setItem('sessionType',JSON.stringify(userSession.data.type));
		   	}
		}
		
		if(userSession.data.type == "company") {
			$scope.header.profile = "company";
			$state.go('home.companyprofile', { companyDet: { id: userSession.data.id, type : userSession.data.type, verified: userSession.data.verified } })
		} else if (userSession.data.type == "jobseeker") {
			$scope.header.profile = "jobseeker";
			$state.go('home.jobseekerprofile', { profileDet: { id: userSession.data.id, type: userSession.data.type, verified: userSession.data.verified } }) 
		}
	} 
	
	$scope.sessionId = JSON.parse(localStorage.getItem('sessionId'));
	$scope.sessionVerified = JSON.parse(localStorage.getItem('sessionVerified'));
	$scope.sessionType = JSON.parse(localStorage.getItem('sessionType'));
	
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
				if (typeof(Storage) !== "undefined") {
					if(data.id !== null && data.id !== "" && data.id !== undefined)
					{
						localStorage.setItem('sessionId',JSON.stringify(data.id));
					}
					if(data.verified !== null && data.verified !== "" && data.verified !== undefined)
					{
			   			localStorage.setItem('sessionVerified',JSON.stringify(data.verified));
					}
					if(data.type !== null && data.type !== "" && data.type !== undefined)
					{
						localStorage.setItem('sessionType',JSON.stringify(data.type));
					}
				}
				 
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
		}).error(function(data){
			
			alert("Please verify your identity!");
		})
	}
	
	$scope.logout = function() {
		$http({
			method: 'GET',
			url:'/logout'
		}).success(function(data){
			if(data.result) {
				$scope.header.profile="";
				$scope.header.verified=false;
				$scope.header.session=false;
				$scope.signindata.email = "";
				$scope.signindata.password = "";
				$state.go('home');
			}
		}).error(function(data){
			alert("Error while loggin out.")
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
				"companyName" : $scope.companydata.cname,
				"email" : $scope.companydata.cemail,
				"password" : $scope.companydata.cpassword,
				"website":$scope.companydata.website,
				"logoURL":$scope.companydata.logoURL,
				"address":$scope.companydata.address,
				"companyDesc":$scope.companydata.companyDesc
			}
			url="/company"
			//Flush the scope variables
			$scope.companydata.cname = "";	
			$scope.companydata.cemail = "";
			$scope.companydata.cpassword = "";
			$scope.companydata.website ="";
			$scope.companydata.logoURL = "";
			$scope.companydata.address = "";
			$scope.companydata.companyDesc = "";
			
			
		} else if($scope.profile.type == "user") {
			data = {
				"email" : $scope.userdata.uemail,
				"firstname" : $scope.userdata.fname,
				"lastname" : $scope.userdata.lname,
				"password" : $scope.userdata.upassword
			}
			url="/jobseeker";
			//Flush the scope variables
			$scope.userdata.fname = "";
			$scope.userdata.lname = "";
			$scope.userdata.upassword = "";
			$scope.userdata.uemail="";
		}

		//make http request
		$http({
				
			method : "POST",
			url : url,
			data : data
			
		}).success(function(data) {
				
			//If success transition to verification page
			$state.go('home.verify', { profile: { id: data.id, type: data.type , verified: data.verified } } )
				
		}).error(function(data) {
				
			//Alert the user if ther email is already in use. Do not make transition to any page.s
			$scope.invEmail="Email already in use";
		})
	}


	//route to view jobs page
	$scope.toViewJobs = function(){
				
		$state.go("home.viewJobs", { profileDet: { id: JSON.parse(localStorage.getItem('sessionId')),
												   type: JSON.parse(localStorage.getItem('sessionType')),
												   verified: JSON.parse(localStorage.getItem('sessionVerified')) } });
	}

	$scope.gotoprofilepage = function(){
		$state.go("home.jobseekerprofile", { profileDet: { id: JSON.parse(localStorage.getItem('sessionId')),
			   type: JSON.parse(localStorage.getItem('sessionType')),
			   verified: JSON.parse(localStorage.getItem('sessionVerified'))    } }); 
			   
	}

	$scope.tointerestedjobs = function(){
		$state.go("home.interestedjobs", {profileDet: { id: JSON.parse(localStorage.getItem('sessionId')),
			   type: JSON.parse(localStorage.getItem('sessionType')),
			   verified: JSON.parse(localStorage.getItem('sessionVerified')) } });
	}

	$scope.toappliedjobs = function(){
		$state.go("home.appliedjobs", {profileDet: { id: JSON.parse(localStorage.getItem('sessionId')),
			   type: JSON.parse(localStorage.getItem('sessionType')),
			   verified: JSON.parse(localStorage.getItem('sessionVerified')) } });
	}

    $scope.gotoInterviews = function() {
        $state.go("home.jobseekerInterviews", {profileDet: {id: JSON.parse(localStorage.getItem('sessionId')),
            type: JSON.parse(localStorage.getItem('sessionType')),
            verified: JSON.parse(localStorage.getItem('sessionVerified'))}})
    }

	
	$scope.gotoHome = function() {
		$state.go("home.companyprofile", {companyDet: {id: JSON.parse(localStorage.getItem('sessionId')),
			   type: JSON.parse(localStorage.getItem('sessionType')),
			   verified: JSON.parse(localStorage.getItem('sessionVerified'))}})
	}

});