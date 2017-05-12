jobPortalApp.controller('controllerCompanyProfile', function($http, $state, $scope, $rootScope, Upload, $timeout){
	
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
	
	
	$http({
		method:'POST',
		url:'/signature'
	}).success(function(data){
		$scope.policy = data.policy;
		$scope.signature = data.signature;
		console.log($scope.policy+" "+$scope.signature);
	})
	
	$scope.uploadFiles = function(file, errFiles) {
        $scope.f = file;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            file.upload = Upload.upload({
                url: 'https://angular-file-upload.s3-us-west-2.amazonaws.com/',
                //data: {file: file},
            	data: {
            		key: file.name, // the key to store the file on S3, could be file name or customized
            		AWSAccessKeyId: "",
            		acl: 'private', // sets the access to the uploaded file in the bucket: private, public-read, ...
            		policy: $scope.policy, // base64-encoded json policy (see article below)
            		signature: $scope.signature, // base64-encoded signature based on policy string (see article below)
            		"Content-Type": file.type != '' ? file.type : 'application/octet-stream', // content type of the file (NotEmpty)
            		filename: file.name, // this is needed for Flash polyfill IE8-9
            		file: file
            	}
            });

            file.upload.then(function (response) {
            	console.log(response)
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
            	console.log(response)
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
            });
        }   
    }
	
	
	
	
})