jobPortalApp.controller('controllerJobSeekerProfile', function($scope, $stateParams, $log){


	var userDetails = $stateParams.profileDet;

	//get jobseeker profile from id
    $http({
        method : "GET",
        url : '/jobseeker',
		data : {
        	userid : userDetails.id
		}
    }).success(function(data) {

    	console.log("in get jobseeker");
    	console.log(data);
    	$scope.workexperience = data.workexperience;
    	$scope.education = data.education;
    	$scope.skills = data.skills;


    }).error(function(error) {
        console.log("Error in get jobseeker "+error);
    });


    //add new workexperience fields
    $scope.addWorkExperience = function(){
		$scope.workexperience.push({
			company : null,
			positionheld : null,
			startdate : null,
			enddate : null
		});
	}

	//add new education fields
	$scope.addEducation = function() {
    	$scope.education.push({
			institute : null,
			startdate : null,
			enddate : null,
			degree : null
		});
	}

    //add new skills fields
    $scope.addSkills = function() {
        $scope.skills.push({
            institute : null,
            startdate : null,
            enddate : null,
            degree : null
        });
    }


	$scope.updateJobseekerProfile = function() {

        $http({
            method : "POST",
            url : '/updateJobseekerProfile',
            data : {
                "firstname" : $scope.firstname,
				"lastname" : $scope.lastname,
				"email" : $scope.email,
				"password" : $scope.password,
				"selfintroduction" : $scope.selfintroduction,
				"workexperience" : $scope.workexperience,
				"education" : $scope.education,
				"skills" : $scope.skills
            }
        }).success(function(data) {

            console.log("in get jobseeker");
            console.log(data);

			//if success


        }).error(function(error) {
            console.log("Error in get jobseeker "+error);
        });



	}


})