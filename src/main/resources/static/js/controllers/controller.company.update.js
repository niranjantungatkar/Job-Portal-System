jobPortalApp.controller('controllerCompanyUpdate', function($http, $state, $scope) {
	
	if($state.params.companyDet !== null && $state.params.companyDet !== "" && $state.params.companyDet !== undefined) {
		
		console.log("fasdfasds")
		console.log($state.params.companyDet);
		if (typeof(Storage) !== "undefined") {
	    	if($state.params.companyDet.id !== null && $state.params.companyDet.id !== "" && $state.params.companyDet.id !== undefined)
	    	{console.log("aaa")
	      		localStorage.setItem('updCid',JSON.stringify($state.params.companyDet.id));
	    	}
		}
	}
	
	$scope.companyid = JSON.parse(localStorage.getItem('updCid'));
	console.log("company Update");
	
	$scope.updateCompanyData = {
			address:"",
			companyDesc:"",
			website:"",
			companyName: $scope.companyid,
			logoURL:""
		}
	
	var pathUrl = "/company/"+$scope.companyid
	$http({
		method: 'GET',
		url: pathUrl
	}).success(function(data){
		console.log(data);
		$scope.company=data;
		$scope.updateCompanyData = data;
	})
	
	$scope.updateCompanyData = {
		address:"",
		companyDesc:"",
		website:"",
		companyName: $scope.companyid,
		logoURL:""
	}
	
	$scope.updateCompanyError = "";
	
	$scope.updateCompany = function() {
		$http({
			method:'PUT',
			url: '/company',
			data : $scope.updateCompanyData
		}).success(function(data){
			console.log("asfasdfasdfasd");
		}).error(function(data){
			$scope.updateCompanyError=true;
		})	
	}
	
	console.log($scope.header.verified);
})