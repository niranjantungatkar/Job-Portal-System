jobPortalApp.controller('controllerUpdateJobPosting', function($http, $state, $scope, $rootScope) {
	
	if($state.params.companyDet !== null && $state.params.companyDet !== "" && $state.params.companyDet !== undefined) {
		
		console.log("fasdfasds")
		console.log($state.params.companyDet);
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.companyDet.id !== null && $state.params.companyDet.id !== "" && $state.params.companyDet.id !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('updJobpostingCid',JSON.stringify($state.params.companyDet.id));
	    	}
	    	if($state.params.companyDet.reqId !== null && $state.params.companyDet.reqId !== "" && $state.params.companyDet.reqId !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('reqId',JSON.stringify($state.params.companyDet.reqId));
	    	}
		}
	}
	$scope.updJobpostingCid = JSON.parse(localStorage.getItem('updJobpostingCid'));
	$scope.reqId = JSON.parse(localStorage.getItem('reqId'));
	$scope.company={}
	
	$scope.fMsg = false
	$scope.sMsg = false
	
	var pathUrl = "/company/"+$scope.updJobpostingCid
	$http({
		method: 'GET',
		url: pathUrl
	}).success(function(data){
		console.log(data);
		$scope.company=data;
	})
	
	$scope.updateJobPosting = {
		requisitionId: $scope.reqId,
		title:"",
		jobDescription:"",
		responsibilities:"",
		skills:"",
		salary:"",
		status:"",
		location:""
	};
	
	$scope.skills ="";
	var pathUrlJobposting = "/jobposting/"+$scope.reqId;
	$http({
		method:'GET',
		url:pathUrlJobposting
	}).success(function(data){
		console.log(data);
		
		$scope.updateJobPosting = data;
		
		for(var i=0; i<$scope.updateJobPosting.skills.length; i++) {
			$scope.skills += $scope.updateJobPosting.skills[i].skill+", "
			console.log($scope.updateJobPosting.skills[i].skill)
		}
		$scope.skills = $scope.skills.slice(0,-2);
		
		$scope.updateJobPosting.skills = $scope.skills;
		
		if($scope.updateJobPosting.status == 0)
			$scope.updateJobPosting.status="o";
		else if($scope.updateJobPosting.status == 1)
			$scope.updateJobPosting.status="c";
		else if($scope.updateJobPosting.status == 2)
			$scope.updateJobPosting.status="f";
		
	})
	
	$scope.updateJPDetails = function() {
		
		$scope.fMsg = false;
		$scope.sMsg = false;
		
		console.log($scope.updateJobPosting);
		if($scope.updateJobPosting.status == "o")
			$scope.updateJobPosting.status=0;
		else if($scope.updateJobPosting.status == "c")
			$scope.updateJobPosting.status=2;
		else if($scope.updateJobPosting.status == "f")
			$scope.updateJobPosting.status=1;
			
		$http({
			method:'PUT',
			url:'/jobposting',
			data:$scope.updateJobPosting
		}).success(function(data){
			console.log("Job Posting successfully updated");
			$scope.sMsg = true;
			$scope.fMsg = false;
		}).error(function(data){
			console.log("Job posting error");
			$scope.sMsg = false;
			$scope.fMsg = true;
		})
	}
	
	
	$scope.viewApplications = function() {
		$state.go('home.applications',{job:{id: $scope.reqId}})
	}
	
})