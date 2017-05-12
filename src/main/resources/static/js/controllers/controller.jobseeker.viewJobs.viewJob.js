jobPortalApp.controller('controllerViewJob', function($scope, $state, $stateParams, $http, $log) {

    $scope.jobisapplied = false;
    $scope.jobisinterested = false;

    console.log("in controller view job");
    console.log($state.params);

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

        console.log(data);
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

        console.log("in get jobseeker applied jobs");
        console.log(data);

        for(var i=0; i<data.length; i++){

            if(data[i].jobPosting.requisitionId == $state.params.jobAndProfile.requisitionId)
                $scope.jobisapplied = true;
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

        console.log("in get jobseeker from view jobs");
        console.log(data);

        for(var i=0; i<data.interestedJobs.length; i++){
            if(data.interestedJobs[i].requisitionId == $state.params.jobAndProfile.requisitionId)
                $scope.jobisinterested = true;
        }


    }).error(function(error) {
        console.log("Error in get jobseeker from view jobs");
        console.log(error);
    });



    $scope.interestedJob = function(){

        console.log("in interestedJob");

        //add to interested list of jobseeker
        $http({
            method: 'POST',
            url: '/jobseeker/interested',
            data : {
                applicant : $state.params.jobAndProfile.profileDet.id,
                jobPostingId :  $state.params.jobAndProfile.requisitionId
            }
        }).success(function(data){

            console.log("success in adding job application to interested list");
            console.log(data);
            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing apply with profile");
            console.log(error);
        });

    }


    $scope.applywithprofile = function(){


        console.log("in apply with profile");

        //get job data
        $http({
            method: 'POST',
            url: '/jobapplication',
            data : {
                applicant : $state.params.jobAndProfile.profileDet.id,
                jobPostingId :  $state.params.jobAndProfile.requisitionId
            }
        }).success(function(data){

            console.log("success in job application");
            console.log(data);
            $state.go('home.viewJobs', {profileDet: $state.params.jobAndProfile.profileDet} );

        }).error(function(error){
            console.log("Error in positing apply with profile");
            console.log(error);
        });


    }

    $scope.applywithresume = function(){

    }

});