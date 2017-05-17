jobPortalApp.controller('controllerViewJob', function($scope, $state, $stateParams, Upload, $timeout, $http) {

    $scope.jobisapplied = false;
    $scope.jobisinterested = false;
    $scope.jobisoffered = false;
    $scope.jobisrejected = false;
    $scope.offeraccepted = false;
    $scope.offerrejected = false;
    $scope.applicationispending = false;

    //check whether user is verified
    if($state.params.jobAndProfile.profileDet.verified == true)
        $scope.jobseekerverified = true;
    else
        $scope.jobseekerverified = false;

    //get job data
    $http({
        method: 'GET',
        url: '/jobposting/'+$state.params.jobAndProfile.requisitionId
    }).success(function(data){

        $scope.job = data;

    }).error(function(error){
        console.log("Error in getting all jobs");
        console.log(error);
    });


    //check whether jobseeker has applied for this job
    $http({
        method : "GET",
        url : '/jobapplication/jobseeker/'+ $state.params.jobAndProfile.profileDet.id
    }).success(function(data) {

        console.log("check if job application is applied");
        console.log(data);

        for(var i=0; i<data.length; i++){

            if(data[i].jobPosting.requisitionId == $state.params.jobAndProfile.requisitionId) {
            	$scope.jobisapplied = true
                $scope.applicationId = data[i].applicationId;

                //check if offered
                if(data[i].applicationStatus == 3) {
                    $scope.jobisoffered = true;
                    $scope.applicationispending = false;
                }

            	//check for rejected
                if(data[i].applicationStatus == 1) {
                    $scope.jobisrejected = true;
                    $scope.applicationispending = false;
                }

                if(data[i].applicationStatus == 4) {
                    $scope.offeraccepted = true;
                    $scope.applicationispending = false;
                }

                if(data[i].applicationStatus == 5) {
                    $scope.offerrejected = true;
                    $scope.applicationispending = false;
                }

                if(data[i].applicationStatus == 0)
                    $scope.applicationispending = true;
            }        
        }
    }).error(function(error) {
        console.log("Error in get jobseeker applied jobs");
        console.log(error);
    });



    //check for user interested jobs
    $http({
        method : "GET",
        url : '/jobseeker/'+ JSON.parse(localStorage.getItem('jobseekerid'))
    }).success(function(data) {


        for(var i=0; i<data.interestedJobs.length; i++){
            if(data.interestedJobs[i].requisitionId == $state.params.jobAndProfile.requisitionId)
                $scope.jobisinterested = true;
        }


    }).error(function(error) {
        console.log("Error in get jobseeker from view jobs");
        console.log(error);
    });

    //for resume upload file
    $http({
        method:'POST',
        url:'/signature'
    }).success(function(data){
        $scope.policy = data.policy;
        $scope.signature = data.signature;
        console.log($scope.policy+" "+$scope.signature);
    })

    $scope.interestedJob = function(){


        //add to interested list of jobseeker
        $http({
            method: 'POST',
            url: '/jobseeker/interested',
            data : {
                applicant : $state.params.jobAndProfile.profileDet.id,
                jobPostingId :  $state.params.jobAndProfile.requisitionId
            }
        }).success(function(data){

            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing apply with profile");
            console.log(error);
        });

    }


    $scope.applywithprofile = function(){

        //get job data
        $http({
            method: 'POST',
            url: '/jobapplication',
            data : {
                applicant : $state.params.jobAndProfile.profileDet.id,
                jobPostingId :  $state.params.jobAndProfile.requisitionId
            }
        }).success(function(data){

            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing apply with profile");
            console.log(error);
        });


    }


    //for apply with resume
    $scope.uploadFiles = function(file, errFiles) {
        $scope.f = file;
        var newFileName = file.name+'-'+$state.params.jobAndProfile.profileDet.id;
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

            //set resume url
            $scope.resumeUrl = 'https://angular-file-upload.s3-us-west-2.amazonaws.com/'+newFileName;

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
                file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
            });
        }
    }

    $scope.applywithresume = function(){


        //post job application
        $http({
            method: 'POST',
            url: '/jobapplication',
            data : {
                applicant : $state.params.jobAndProfile.profileDet.id,
                jobPostingId :  $state.params.jobAndProfile.requisitionId,
                resume : $scope.resumeUrl
            }
        }).success(function(data){

            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing apply with resume");
            console.log(error);
        });


    }


    $scope.acceptOffer = function() {

        $http({
            method: 'POST',
            url: '/jobapplication/jobseeker/updatestatus',
            data : {
                jobApplicationId : $scope.applicationId,
                status : 4
            }

        }).success(function(data){

            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing accept offer");
            console.log(error);
        });

    }

    $scope.rejectOffer = function() {

        $http({
            method: 'POST',
            url: '/rejectOffer/',
            data : {
                jobApplicationId : $scope.applicationId,
                status : 5
            }

        }).success(function(data){

            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing reject offer");
            console.log(error);
        });

    }

    $scope.cancelApplication = function() {


        $http({
            method: 'POST',
            url: '/jobapplication/jobseeker/updatestatus',
            data : {
                jobApplicationId : $scope.applicationId,
                status : 2
            }

        }).success(function(data){

            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing cancel application");
            console.log(error);
        });

    }

});