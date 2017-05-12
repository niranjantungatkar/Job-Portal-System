jobPortalApp.controller('controllerAppliedJobs',function($scope, $http, $state, userSession) {


    console.log("in applied jobs");


    //get all applied jobs
    $http({
        method : "GET",
        url : '/jobapplication/jobseeker/'+ $state.params.profileDet.id
    }).success(function(data) {

        console.log("in get jobseeker applied jobs");
        console.log(data);

        $scope.appliedjobs = data;


    }).error(function(error) {
        console.log("Error in get jobseeker applied jobs");
        console.log(error);
    });



})