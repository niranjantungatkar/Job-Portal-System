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
	
	$scope.jobpostings = {};
	$scope.jobpostingcid = JSON.parse(localStorage.getItem('jobpostingcid'));
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