jobPortalApp.controller('controllerCompanyUpdate', function($http, $state, $scope, Upload, $timeout) {
	
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
	
	$scope.sMsg=false;
	$scope.fMsg=false;
	
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
		$scope.sMsg=false;
		$scope.fMsg=false;
		$http({
			method:'PUT',
			url: '/company',
			data : {
				address:$scope.updateCompanyData.address,
				companyDesc:$scope.updateCompanyData.companyDesc,
				website:$scope.updateCompanyData.website,
				companyName: $scope.companyid,
				logoURL : $scope.company.logoURL
			}
			//data : $scope.updateCompanyData
		}).success(function(data){
			console.log("asfasdfasdfasd");
			$scope.sMsg=true;
			$scope.fMsg=false;
		}).error(function(data){
			$scope.updateCompanyError=true;
			$scope.sMsg=false;
			$scope.fMsg=true;
		})	
	}
	
	console.log($scope.header.verified);
	
	//for image upload file
    $http({
        method:'POST',
        url:'/signature'
    }).success(function(data){
        $scope.policy = data.policy;
        $scope.signature = data.signature;
    })
	
	//for upload photo
    $scope.uploadFiles = function(file, errFiles) {
        $scope.f = file;
        var newFileName = file.name+'-'+$scope.companyid;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            file.upload = Upload.upload({
                url: 'https://angular-file-upload.s3-us-west-2.amazonaws.com/',
                //data: {file: file},
                data: {
                    key: newFileName, // the key to store the file on S3, could be file name or customized
                    AWSAccessKeyId: "AKIAJPWE3LFVDSTG5IUQ",
                    acl: 'public-read-write', // sets the access to the uploaded file in the bucket: private, public-read, ...
                    policy: $scope.policy, // base64-encoded json policy (see article below)
                    signature: $scope.signature, // base64-encoded signature based on policy string (see article below)
                    "Content-Type": file.type != '' ? file.type : 'application/octet-stream', // content type of the file (NotEmpty)
                    filename: file.name, // this is needed for Flash polyfill IE8-9
                    file: file
                }
            });


            file.upload.then(function (response) {
                console.log(response)
                $scope.company.logoURL='https://s3-us-west-2.amazonaws.com/angular-file-upload/'+newFileName
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                console.log(response)
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
            });
        }
    }
})