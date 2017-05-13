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
        console.log(data.content);

        $scope.jobs = data.content;

    }).error(function(error){
        console.log("Error in getting all jobs");
        console.log(error);
    });


    $scope.pageChanged = function(page) {


        $http({
            method: 'GET',
            url: '/jobposting/open/'+page
        }).success(function(data){

            console.log("in get open job posting after page change");
            console.log(data);

            if(data != null && data.length > 0) {
                $scope.jobs = data;
            }else{
                $scope.jobs = null;
            }

        }).error(function(error){
            console.log("Error in getting all jobs after page change");
            console.log(error);
        });


    }




    // var skipwords = ['in', 'at', 'to', 'from', 'with', 'like'];
    // $scope.locationSearch = "";
    //
    // $scope.locationFilter = function(job) {
    //     console.log("filter by location");
    //     var words = $scope.locationSearch;
    //
    //     if(words == null || words == "")
    //         return job;
    //     else {
    //         console.log("in else");
    //         for (var i = 0; i < words.length; i++) {
    //             console.log("words found");
    //             console.log(words[i]);
    //             console.log("object values");
    //             console.log(Object.values);
    //             if ( (Object.values(job)).indexOf(words[i]) ) {
    //                 console.log("object value found");
    //                 return job;
    //             }
    //
    //         }
    //         return null;
    //     }
    // }

    $scope.searchJobs = function(){

        var skipwords = ['in', 'at', 'to', 'from', 'with', 'like'];


    }


    $scope.toViewJob = function(requisitionId){

        $state.go("home.viewJob", {jobAndProfile: {profileDet: $state.params.profileDet, requisitionId: requisitionId}} );
    }


})