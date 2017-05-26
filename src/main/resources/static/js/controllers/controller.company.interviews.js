jobPortalApp.controller('controllerCompanyInterviews',function($scope, $http, $state, userSession) {

    if($state.params.companyDet !== null && $state.params.companyDet !== "" && $state.params.companyDet !== undefined) {

        if (typeof(Storage) !== "undefined") {
            if($state.params.companyDet.id !== null && $state.params.companyDet.id !== "" && $state.params.companyDet.id !== undefined)
            {
                localStorage.setItem('jobpostingcid',JSON.stringify($state.params.companyDet.id));
            }
        }
    }

    $scope.jobpostingcid = JSON.parse(localStorage.getItem('jobpostingcid'));

    //get all jobseeker interviews
    $http({
        method:'POST',
        url: "jobapplication/interview/companysearch",
        data : {
            companyName : $scope.jobpostingcid
        }
    }).success(function(data){

        var interviews = [];
        for(var i=0; i<data.length; i++) {
            for(var j=0; j<data[i].interviews.length; j++) {
                interviews.push({
                    applicationId : data[i].applicationId,
                    companyName : data[i].jobPosting.company.companyName,
                    companyLogo : data[i].jobPosting.company.logoURL,
                    jobTitle : data[i].jobPosting.title,
                    interviewNo : data[i].interviews[j].interviewNo,
                    startTime : data[i].interviews[j].startTime,
                    endTime : data[i].interviews[j].endTime,
                    status : data[i].interviews[j].status
                });
            }
        }
        $scope.interviews = interviews;

    }).error(function(err){
        alert("There was a problem retrieving interviews. Please refresh and try again");
    });


    $scope.cancelInterview = function(applicationId, interviewId) {
        console.log(applicationId);
        console.log(interviewId);
        $http({
            method : "POST",
            url : '/jobapplication/interview/update',
            data : {
                jobApplicationId : applicationId,
                interviewNo : (interviewId).toString(),
                status : "3"
            }
        }).success(function(data){
            alert("Interview cancel successful");
        }).error(function(err){
            alert("There was a problem performing your request. Please refresh and try again");
        });
    }


})