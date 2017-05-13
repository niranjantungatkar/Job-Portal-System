jobPortalApp.controller('controllerViewJobs', function($scope, $state, $stateParams, $http, $log) {


    $scope.nojobsfound = false;
    console.log("in controller view jobs");
    console.log($state.params.profileDet);



    //get all jobs
    $http({
        method: 'GET',
        url: '/jobposting/open'
    }).success(function(data){

        console.log("in get open job posting");
        console.log(data);

        if(data != null && data.length > 0) {
            $scope.jobs = data;
        }

    }).error(function(error){
        console.log("Error in getting all jobs");
        console.log(error);
    });





    $scope.searchJobs = function(){

        var skipwords = ['in', 'at', 'to', 'from', 'with', 'like'];


    }


    $scope.toViewJob = function(requisitionId){

        $state.go("home.viewJob", {jobAndProfile: {profileDet: $state.params.profileDet, requisitionId: requisitionId}} );
    }


})