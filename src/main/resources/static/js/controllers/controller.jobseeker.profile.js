jobPortalApp.controller('controllerJobSeekerProfile', function($scope, $state, $stateParams, $http, $log){


	$scope.noworkexperience = false;
	$scope.noeducation = false;
	$scope.noskills = false;
    $scope.noselfIntroduction = false;

    
    if($state.params.profileDet !== null && $state.params.profileDet !== "" && $state.params.profileDet !== undefined) {

        if (typeof(Storage) !== "undefined") {
            if($state.params.profileDet.id != null && $state.params.profileDet.id != "" && $state.params.profileDet.id != undefined)
            {
                localStorage.setItem('jobseekerid',JSON.stringify($state.params.profileDet.id));
            }
            if($state.params.profileDet.type != null && $state.params.profileDet.type != "" && $state.params.profileDet.type != undefined)
            {
                localStorage.setItem('type',JSON.stringify($state.params.profileDet.type));
            }
            if($state.params.profileDet.verified != null && $state.params.profileDet.verified != "" && $state.params.profileDet.verified != undefined)
            {
                localStorage.setItem('jobseeker_verified',JSON.stringify($state.params.profileDet.verified));
            }
        }
    }



    $scope.jobseekerid = JSON.parse(localStorage.getItem('jobseekerid'));
    $scope.type = JSON.parse(localStorage.getItem('type'));
    $scope.jobseekerVerification = JSON.parse(localStorage.getItem('jobseeker_verified'));


	//get jobseeker profile from id
    $http({
        method : "GET",
        url : '/jobseeker/'+ JSON.parse(localStorage.getItem('jobseekerid'))
    }).success(function(data) {

        console.log("in get jobseeker");
        console.log(data)

    	$scope.jobseeker = data;

    	if(data.selfIntroduction == null)
			$scope.noselfIntroduction = true;

    	//keeping workexperience, education, skills seperate
    	$scope.workexperience = data.workExp;
        $scope.education = data.education;
        $scope.skills = data.skills;

        //format workexperience date
        for(var i=0; i<data.workExp.length; i++) {
            $scope.workexperience[i].startDate = $scope.workexperience[i].startDate;
            $scope.workexperience[i].endDate = $scope.workexperience[i].endDate;
        }

        for(var i=0; i<data.education.length; i++){
            $scope.education[i].startDate = $scope.education[i].startDate;
            $scope.education[i].endDate = $scope.education[i].endDate;
        }


        if($scope.workexperience == null || $scope.workexperience.length == 0)
    		$scope.noworkexperience = true;

        if($scope.education == null || $scope.education.length == 0)
            $scope.noeducation = true;

        //check condition if education is null
		if($scope.education == null)
			$scope.eduction = [];

        if($scope.skills == null || $scope.skills.length == 0)
            $scope.noskills = true;


    }).error(function(error) {
        console.log("Error in get jobseeker "+error);
    });




	$scope.getImages = function(){
    	console.log("in get images");
    	images = $window.images;
    	console.log(images);

	}


	//route to update profile
	$scope.toUpdateProfile = function(){

		//check for if user verified
        if($scope.jobseekerVerification) {
            $state.go('home.updateJobseekerProfile',{ profileDet: {  id: $scope.jobseekerid } } )
        } else {
            $state.go('home.verify', { profile : { id: $scope.jobseekerid, type: $scope.type } })
        }

	}

    function timeConverter(date){
        var a = new Date(date * 1000);
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var time = month+'-'+year;
        return time;
    }


})