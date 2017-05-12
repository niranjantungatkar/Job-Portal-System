jobPortalApp.controller('controllerCompanyProfile', function($http, $state, $scope, $rootScope){
	
	if($state.params.companyDet !== null && $state.params.companyDet !== "" && $state.params.companyDet !== undefined) {
		
		console.log("fasdfasds")
		console.log($state.params.companyDet);
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.companyDet.id !== null && $state.params.companyDet.id !== "" && $state.params.companyDet.id !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('cid',JSON.stringify($state.params.companyDet.id));
	    	}
	    	if($state.params.companyDet.type !== null && $state.params.companyDet.type !== "" && $state.params.companyDet.type !== undefined)
	    	{
	    		console.log("aaa1")
	      		localStorage.setItem('comapany_type',JSON.stringify($state.params.companyDet.type));
	    	}
	    	if($state.params.companyDet.verified !== null && $state.params.companyDet.verified !== "" && $state.params.companyDet.verified !== undefined)
	    	{
	    		console.log("herafasdf")
	      		localStorage.setItem('comapany_verified',JSON.stringify($state.params.companyDet.verified));
	    	}
		}
	}
	
	
	$scope.company = {}
	
	$scope.cid = JSON.parse(localStorage.getItem('cid'));
	$scope.companyType = JSON.parse(localStorage.getItem('comapany_type'));
	$scope.companyVerification = JSON.parse(localStorage.getItem('comapany_verified'));
	
	console.log($scope.cid+" "+$scope.companyType+" "+$scope.companyVerification)
	var pathUrl = "/company/"+$scope.cid
	$http({
		method: 'GET',
		url: pathUrl
	}).success(function(data){
		console.log(data);
		$scope.company = data;
	})
	
	
	$rootScope.update = function() {
		if($scope.companyVerification) {
			$state.go('home.update',{ companyDet: {  id: $scope.cid } } )
		} else {
			$state.go('home.verify', { profile : { id: $scope.cid, type: $scope.companyType } })
		}
	}
	
	$rootScope.companyPostJob = function() {
		if($scope.companyVerification) {
			$state.go('home.postjob',{ companyDet: {  id: $scope.cid } } )
		} else {
			$state.go('home.verify', { profile : { id: $scope.cid, type: $scope.companyType } })
		}
	}
	
	$rootScope.viewJobPostings = function() {
		$state.go('home.viewjobpostings', { companyDet: {  id: $scope.cid } })
	}
})