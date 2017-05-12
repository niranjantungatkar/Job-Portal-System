jobPortalApp.controller('controllerCompanyPostings', function($http, $state, $scope, $rootScope){

	console.log("in job postings")
	if($state.params.companyDet !== null && $state.params.companyDet !== "" && $state.params.companyDet !== undefined) {
		
		console.log("fasdfasds")
		console.log($state.params.companyDet);
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.companyDet.id !== null && $state.params.companyDet.id !== "" && $state.params.companyDet.id !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('jobpostingcid',JSON.stringify($state.params.companyDet.id));
	    	}
		}
	}
	
	$scope.status = "o";
	$scope.statusFilter = function(jobposting) {
		if($scope.status == "o")
			return jobposting.status == 0;
		else if ($scope.status == "c")
			return jobposting.status == 1;
		else if ($scope.status == "f")
			return jobposting.status == 2;
	  };
	
	
	
	$scope.jobpostings = {};
	$scope.jobpostingcid = JSON.parse(localStorage.getItem('jobpostingcid'));
	console.log($scope.jobpostingcid)
	var pathUrl = "/jobposting/company/"+$scope.jobpostingcid;
	$http({
		method:'GET',
		url:pathUrl
	}).success(function(data){
		console.log(data);
		$scope.jobpostings = data;
	})
	
	$scope.updateJobPosting = function(reqId) {
		console.log("asfdasdfasf");
		console.log(reqId);
		console.log($scope.jobpostingcid);
		$state.go('home.updateJobPosting', {companyDet: { id : $scope.jobpostingcid, reqId : reqId }})
	}
	
	$scope.viewApplicationsJobPosting = function() {
		console.log("Transition to be added");
	}
	
	
})