jobPortalApp.controller('controllerJobSeekerUpdateProfile', function($scope, $state, $stateParams, $http) {



    if($state.params.profileDet !== null && $state.params.profileDet !== "" && $state.params.profileDet !== undefined) {

        if (typeof(Storage) !== "undefined") {
            if($state.params.profileDet.id != null && $state.params.profileDet.id != "" && $state.params.profileDet.id != undefined)
            {
                localStorage.setItem('jobseekerid',JSON.stringify($state.params.profileDet.id));
            }
        }
    }


    $http({
        method: "GET",
        url: '/jobseeker/' + JSON.parse(localStorage.getItem('jobseekerid'))
    }).success(function (data) {

        $scope.jobseeker = data;

        //also store in localstorage
        localStorage.setItem("jobseeker", $scope.jobseeker);

        if (data.selfIntroduction == null)
            $scope.noselfIntroduction = true;



        //keeping workexperience, education, skills seperate
        $scope.workexperience = data.workExp;
        $scope.education = data.education;
        $scope.skills = data.skills;


        //format workexperience date
        for(var i=0; i<data.workExp.length; i++) {
            $scope.workexperience[i].startDate = timeConverter($scope.workexperience[i].startDate);
            $scope.workexperience[i].endDate = timeConverter($scope.workexperience[i].endDate);
        }


        if ($scope.workexperience == null || $scope.workexperience.length == 0)
            $scope.noworkexperience = true;

        if ($scope.education == null || $scope.education.length == 0)
            $scope.noeducation = true;

        //check condition if education is null
        if ($scope.education == null)
            $scope.education = [];

        if ($scope.skills == null || $scope.skills.length == 0)
            $scope.noskills = true;


    }).error(function (error) {
        console.log("Error in get jobseeker " + error);
    });





    $scope.updateJobseekerProfile = function() {


        if($scope.positionHeld != undefined && $scope.positionHeld != null && $scope.positionHeld != "" && $scope.companyname != undefined && $scope.companyname != null && $scope.companyname != "" )
            $scope.workexperience.push({
                positionHeld: $scope.positionHeld,
                company : $scope.companyname,
                startDate : $scope.workexperiencestartdate,
                endDate : $scope.workexperienceenddate
            });
        if($scope.institutename != undefined && $scope.institutename != null && $scope.institutename != "" && $scope.educationdegree != undefined && $scope.educationdegree != null && $scope.educationdegree != "" )
            $scope.education.push({
                institute: 	$scope.institutename,
                degree : $scope.educationdegree,
                startDate : $scope.educationstartdate,
                endDate : $scope.educationenddate
            });
        if($scope.jobseekerskill != undefined && $scope.jobseekerskill != null && $scope.jobseekerskill != "")
            $scope.skills.push({
                skill : $scope.jobseekerskill
            });





        var dataSend = {
            "id" : $scope.jobseeker.jobseekerid,
            "firstname" : $scope.jobseeker.firstname,
            "lastname" : $scope.jobseeker.lastname,
            "email" : $scope.jobseeker.email,
            "password" : $scope.jobseeker.password,
            "selfIntroduction" : $scope.selfIntroduction,
            "workExperience" : $scope.workexperience,
            "education" : $scope.education,
            "skills" : $scope.skills
        }
        console.log(dataSend);

        $http({
            method : "PUT",
            url : '/jobseeker',
            data : dataSend
        }).success(function(data) {


            //if success
            $state.go("home.jobseekerprofile", {profileDet: {id: JSON.parse(localStorage.getItem('jobseekerid'))} });


        }).error(function(error) {
            console.log("Error in update jobseeker "+error);
            console.log(error);

        });



    }






    $scope.getImages = function(){
        images = $window.images;

    }



    function timeConverter(date){
        var a = new Date(date * 1000);
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var time = month + ' ' + year;
        return time;
    }


})

