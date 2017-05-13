jobPortalApp.controller('controllerCompanyPostJob', function($scope, $stateParams, $state, $log, $http) {
	
	if($state.params.companyDet !== null && $state.params.companyDet !== "" && $state.params.companyDet !== undefined) {
		console.log("fasdfasds")
		console.log($state.params.companyDet);
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.companyDet.id !== null && $state.params.companyDet.id !== "" && $state.params.companyDet.id !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('postCid',JSON.stringify($state.params.companyDet.id));
	    	}
		}
	}
	
	$scope.sMsg=false;
	$scope.fMsg=false;
	
	$scope.postCid = JSON.parse(localStorage.getItem('postCid'));
	console.log("company post job");
	$scope.company={}
	
	var pathUrl = "/company/"+$scope.postCid
	$http({
		method: 'GET',
		url: pathUrl
	}).success(function(data){
		console.log(data);
		$scope.company=data;
	})
	
	$scope.jobpostingData={
			companyName:$scope.postCid,
			title:"",
			jobDescription:"",
			responsibilities:"",
			skills:"",
			salary:"",
			location:""
	}
	
	$scope.postJob = function() {
		$scope.sMsg=false;
		$scope.fMsg=false;
		console.log($scope.jobpostingData)
		$http({
			method:'POST',
			url:'/jobposting',
			data:$scope.jobpostingData
		}).success(function(data){
			console.log("job created");
			$scope.sMsg=true;
		}).error(function(data){
			console.log("errro");
			$scope.fMsg=true;
		})
	}
	
	console.log("afasdfasdfdsf");
})