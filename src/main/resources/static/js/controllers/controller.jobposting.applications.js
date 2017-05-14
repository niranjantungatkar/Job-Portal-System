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
	
})