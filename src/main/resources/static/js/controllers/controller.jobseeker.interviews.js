jobPortalApp.controller('controllerJobseekerInterviews',function($scope, $http, $state, userSession) {

    //get all applied jobs and get interviews from them
    $http({
        method : "GET",
        url : '/jobapplication/jobseeker/'+ $state.params.profileDet.id
    }).success(function(data) {

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

    }).error(function(error) {
        alert("There was an error in retrieving your interviews. Please refresh and try again");
    });

    $scope.acceptInterview = function(applicationId, interviewId) {

        $http({
            method : "POST",
            url : '/jobapplication/interview/update',
            data : {
                jobApplicationId : applicationId,
                interviewNo : (interviewId).toString(),
                status : "1"
            }
        }).success(function(data) {

            alert("Interview accepted");

        }).error(function(err){
            alert("There was a problem performing your request. Please refresh and try again");
        });

    }

    $scope.rejectInterview = function(applicationId, interviewId) {

        $http({
            method : "POST",
            url : '/jobapplication/interview/update',
            data : {
                jobApplicationId : applicationId,
                interviewNo : (interviewId).toString(),
                status : "2"
            }
        }).success(function(data) {

            alert("Interview rejected");

        }).error(function(err){
            alert("There was a problem performing your request. Please refresh and try again");
        });

    }

})