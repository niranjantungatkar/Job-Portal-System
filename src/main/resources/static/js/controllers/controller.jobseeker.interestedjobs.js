jobPortalApp.controller('controllerInterestedJobs',function($scope, $http, $state, userSession) {


    console.log("in interested jobs");


    //get all interested jobs
    $http({
        method : "GET",
        url : '/jobseeker/'+ $state.params.profileDet.id
    }).success(function(data) {

        console.log("in get jobseeker from interested jobs");
        console.log(data);

        $scope.interestedJobs = data;


    }).error(function(error) {
        console.log("Error in get jobseeker from view jobs");
        console.log(error);
    });




})