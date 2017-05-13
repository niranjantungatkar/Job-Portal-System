jobPortalApp.controller('controllerViewJobs', function($scope, $state, $stateParams, $http, $log) {


    $scope.nojobsfound = false;
   
    //get all jobs
    $http({
        method: 'GET',
        url: '/jobposting/open'
    }).success(function(data){

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

    $scope.mainSearch = "";
    
    function findString(obj, regexp) {
    	  let found = false;

    	  JSON.stringify(obj, (k, v) => {
    	    if (typeof v === 'string' && v.includes(regexp)) found = true;
    	    else return v;
    	  });

    	  return found;
    	}
    
    $scope.mainFilter = function(job) {
    	if($scope.mainSearch === null || $scope.mainSearch === "" || $scope.mainSearch === undefined)
    		return true;
    	var matchString = JSON.stringify(job);
    
    	var words = $scope.mainSearch.split(" ");
    	
    	for(var i=0; i<words.length; i++) {
    		return findString(job, words[i]);		
    	}
    	return false;
    }


    $scope.toViewJob = function(requisitionId){

        $state.go("home.viewJob", {jobAndProfile: {profileDet: $state.params.profileDet, requisitionId: requisitionId}} );
    }


})