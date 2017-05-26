jobPortalApp.controller('controllerApplications', function($http, $state, $scope, $rootScope){
	
	if($state.params.job!==null && $state.params.job!== "" && $state.params.job!== undefined) {
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.job.id !== null && $state.params.job.id !== "" && $state.params.job.id !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('jobAppReqId',JSON.stringify($state.params.job.id));
	    	}
		}
	}
	/*
	 * make request to get all applications
	 */
	$scope.company = {}
	$scope.interview = {};
    $scope.showSetInterviewSection = false;
    $scope.interviewScheduled = false;
	
	console.log("in applications")
	$http({
		method:'GET',
		url: '/jobapplication/jobposting/'+JSON.parse(localStorage.getItem('jobAppReqId'))
	}).success(function(data){
		console.log(data);
		$scope.applications = data;
		$scope.company = $scope.applications[0].jobPosting.company;
	})
	
	
	$scope.viewProfile = function(id) {
		$state.go('home.jobseekerprofile', {profileDet: {id: id, type:"company", verified:"false"} })
	}
	
	$scope.offer = function(id, status) {
		$http({
			method: 'POST',
			url: '/jobapplication/company/updatestatus',
			data : {
				jobApplicationId : id,
				status: status
			}
		}).success(function(data){
			$state.reload();
		})
	}
	
	$scope.reject = function(id, status) {
		$http({
			method: 'POST',
			url: '/jobapplication/company/updatestatus',
			data : {
				jobApplicationId : id,
				status: status
			}
		}).success(function(data){
			$state.reload();
		})
	}

	$scope.showSetInterview = function() {
		$scope.showSetInterviewSection = true;
	}

	$scope.setInterview = function(id) {
        var data = {
		   	jobApplicationId : id,
			startTime : moment($scope.interview.from).format("YYYY-MM-DD:HH-mm-ss"),
			endTime : moment($scope.interview.to).format("YYYY-MM-DD:HH-mm-ss")
		}
        $http({
            method: 'POST',
            url: '/jobapplication/interview/schedule',
            data : data
        }).success(function(data){

            $scope.showSetInterviewSection = false;
            $scope.interviewScheduled = true;

        }).error(function(err){
        	alert("There was some error in setting an interview. Please refresh and try again");
		});
	}
	
})