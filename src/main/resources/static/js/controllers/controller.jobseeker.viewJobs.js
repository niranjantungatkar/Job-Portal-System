jobPortalApp.controller('controllerViewJobs', function($scope, $state, $stateParams, $http, $log) {


	$scope.jobSearch = {
    		freeText:"",
    		companies:"",
    		location:"",
    		minSalary:"",
    		maxSalary:"",
    		singleValue:""
    } 
    
    /*
     * low salary != 0 && max salary != 0 -> close range
     * low salary = -1 && max salary = target
     * open range low range = value && max salary - high value - open range.
     */
    $scope.sendSearchData = {
    		freeText:"",
    		companies:"",
    		location:"",
    		minSalary:"",
    		maxSalary:"",
    }
    $scope.nojobsfound = false;
   
    //get all jobs
    $http({
        method: 'POST',
        url: '/jobposting/search?page='+0+'&size=5',
        data: $scope.sendSearchData
    }).success(function(data){
    	console.log(data);
        $scope.jobs = data.content;

    }).error(function(error){
        console.log("Error in getting all jobs");
        console.log(error);
    });

    $scope.pageChanged = function(page) {


        $http({
            method: 'GET',
            url: '/jobposting/search?page='+page+'&size=2'
        }).success(function(data){
        	console.log(data)
            if(data.content !== null && data.content.length > 0) {
                $scope.jobs = data.content;
            }else{
                $scope.jobs = null;
            }

        }).error(function(error){
            console.log("Error in getting all jobs after page change");
            console.log(error);
        });


    }

    
    
    $scope.search = function(page) {
    	console.log($scope.salaryType)
    	if($scope.salaryType == "singlevalue") {
    		
    		$scope.sendSearchData.minSalary = -1;
    		
    		$scope.sendSearchData.maxSalary = $scope.jobSearch.singleValue;
    		
    	} else if ($scope.salaryType == "salaryrange") {
    		
    		if(($scope.jobSearch.minSalary !== null && $scope.jobSearch.minSalary !== undefined && $scope.jobSearch.minSalary !== ""  ) && 
    		   ($scope.jobSearch.maxSalary === null || $scope.jobSearch.maxSalary === undefined || $scope.jobSearch.maxSalary === "" ) ) {
    			
    				$scope.sendSearchData.minSalary = $scope.jobSearch.minSalary;
    			
    				$scope.sendSearchData.maxSalary = 9999999;
    		}
    		
    		if(($scope.jobSearch.minSalary !== null && $scope.jobSearch.minSalary !== undefined && $scope.jobSearch.minSalary !== ""  ) && 
    	       ($scope.jobSearch.maxSalary !== null && $scope.jobSearch.maxSalary !== undefined && $scope.jobSearch.maxSalary !== "" ) ) {
    	    			
    	    		$scope.sendSearchData.minSalary = $scope.jobSearch.minSalary;
    	    			
    	    		$scope.sendSearchData.maxSalary = $scope.jobSearch.maxSalary;
    	    }
    		
    		if(($scope.jobSearch.minSalary === null || $scope.jobSearch.minSalary === undefined || $scope.jobSearch.minSalary === ""  ) && 
    	       ($scope.jobSearch.maxSalary !== null && $scope.jobSearch.maxSalary !== undefined && $scope.jobSearch.maxSalary !== "" ) ) {
    	    	    			
    	    	    $scope.sendSearchData.minSalary = 0;
    	    	    			
    	    	    $scope.sendSearchData.maxSalary = $scope.jobSearch.maxSalary;
    	    }
    		
    		if(($scope.jobSearch.minSalary === null || $scope.jobSearch.minSalary === undefined || $scope.jobSearch.minSalary === ""  ) && 
    	       ($scope.jobSearch.maxSalary === null || $scope.jobSearch.maxSalary === undefined ||$scope.jobSearch.maxSalary === "" ) ) {
    	    	    			
    	    	    $scope.sendSearchData.minSalary = $scope.jobSearch.minSalary;
    	    	    			
    	    	    $scope.sendSearchData.maxSalary = $scope.jobSearch.maxSalary;
    	    }
    	}
    	
    	$scope.sendSearchData.companies = $scope.jobSearch.companies;
    	$scope.sendSearchData.location = $scope.jobSearch.location;
    	$scope.sendSearchData.freeText = $scope.jobSearch.freeText;
    	
    	
    	$http({
    		method:'POST',
    		url:'/jobposting/search?page='+page+'&size=2',
    		data : $scope.sendSearchData
    	}).success(function(data){
    		if(data.content !== null && data.content.length > 0)
    		{
    			$scope.jobs= data.content;
    		}
     	})
    	console.log($scope.sendSearchData);
    	
    }
    
    /*$scope.mainSearch = "";
    
    function findString(obj, regexp) {
    	  let found = false;

    	  JSON.stringify(obj, (k, v) => {
    	    if (typeof v === 'string' && v.includes(regexp)) found = true;
    	    else return v;
    	  });

    	  return found;
    	}*/
    
    /*$scope.mainFilter = function(job) {
    	if($scope.mainSearch === null || $scope.mainSearch === "" || $scope.mainSearch === undefined)
    		return true;
    	var matchString = JSON.stringify(job);
    
    	var words = $scope.mainSearch.split(" ");
    	
    	for(var i=0; i<words.length; i++) {
    		return findString(job, words[i]);		
    	}
    	return false;
    }*/


    $scope.toViewJob = function(requisitionId){

        $state.go("home.viewJob", {jobAndProfile: {profileDet: $state.params.profileDet, requisitionId: requisitionId}} );
    }


})