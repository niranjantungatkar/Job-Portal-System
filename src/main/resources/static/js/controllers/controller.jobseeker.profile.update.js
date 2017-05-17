jobPortalApp.controller('controllerJobSeekerUpdateProfile', function($scope, $state, $stateParams, Upload, $timeout, $http) {



    if($state.params.profileDet !== null && $state.params.profileDet !== "" && $state.params.profileDet !== undefined) {

        if (typeof(Storage) !== "undefined") {
            if($state.params.profileDet.id !== null && $state.params.profileDet.id !== "" && $state.params.profileDet.id !== undefined)
            {
                localStorage.setItem('jobseekerid',JSON.stringify($state.params.profileDet.id));
            }
        }
    }


    $http({
        method: "GET",
        url: '/jobseeker/' + JSON.parse(localStorage.getItem('jobseekerid'))
    }).success(function (data) {

        console.log("in get jobseeker update profile");
        console.log(data);

        $scope.jobseeker = data;

        //also store in localstorage
        localStorage.setItem("jobseeker", $scope.jobseeker);

        if (data.selfIntroduction === null)
            $scope.noselfIntroduction = true;

        //keeping workexperience, education, skills seperate
        $scope.workexperience = data.workExp;
        $scope.education = data.education;
        $scope.skills = data.skills;


        //format workexperience date

        if(data.workExp)
            for(var i=0; i<data.workExp.length; i++) {
                $scope.workexperience[i].startDate = $scope.workexperience[i].startDate;
                $scope.workexperience[i].endDate = $scope.workexperience[i].endDate;
            }

        if(data.education)
            for(var i=0; i<data.education.length; i++){
                $scope.education[i].startDate = $scope.education[i].startDate;
                $scope.education[i].endDate = $scope.education[i].endDate;
            }

        if ($scope.workexperience === null || $scope.workexperience.length === 0)
        console.log($scope.education);

            $scope.noworkexperience = true;

        if ($scope.education === null || $scope.education.length === 0)
            $scope.noeducation = true;

        //check condition if education is null
        if ($scope.education === null)
            $scope.education = [];

        if ($scope.skills === null || $scope.skills.length == 0)
            $scope.noskills = true;


    }).error(function (error) {
        console.log("Error in get jobseeker " + error);
    });



    //for image upload file
    $http({
        method:'POST',
        url:'/signature'
    }).success(function(data){
        $scope.policy = data.policy;
        $scope.signature = data.signature;
    })


    //for upload photo
    $scope.uploadFiles = function(file, errFiles) {
        $scope.f = file;
        var newFileName = file.name+'-'+$state.params.profileDet.id;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            file.upload = Upload.upload({
                url: 'https://angular-file-upload.s3-us-west-2.amazonaws.com/',
                //data: {file: file},
                data: {
                    key: newFileName, // the key to store the file on S3, could be file name or customized
                    AWSAccessKeyId: "AKIAJPWE3LFVDSTG5IUQ",
                    acl: 'public-read-write', // sets the access to the uploaded file in the bucket: private, public-read, ...
                    policy: $scope.policy, // base64-encoded json policy (see article below)
                    signature: $scope.signature, // base64-encoded signature based on policy string (see article below)
                    "Content-Type": file.type != '' ? file.type : 'application/octet-stream', // content type of the file (NotEmpty)
                    filename: file.name, // this is needed for Flash polyfill IE8-9
                    file: file
                }
            });

            //set resume url
            $scope.pictureUrl = 'https://angular-file-upload.s3-us-west-2.amazonaws.com/'+newFileName;

            file.upload.then(function (response) {
                console.log(response)
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                console.log(response)
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
            });
        }
    }



    $scope.updateJobseekerProfile = function() {


        if($scope.positionHeld !== undefined && $scope.positionHeld !== null && $scope.positionHeld !== "" && $scope.companyname !== undefined && $scope.companyname !== null && $scope.companyname !== "" )
            $scope.workexperience.push({
                positionHeld: $scope.positionHeld,
                company : $scope.companyname,
                startDate : moment($scope.workexperiencestartdate).format('YYYY-MM'),
                endDate : moment($scope.workexperienceenddate).format('YYYY-MM')
            });
        if($scope.institutename !== undefined && $scope.institutename !== null && $scope.institutename !== "" && $scope.educationdegree !== undefined && $scope.educationdegree !== null && $scope.educationdegree !== "" )
            $scope.education.push({
                institute: 	$scope.institutename,
                degree : $scope.educationdegree,
                startDate : moment($scope.educationstartdate).format('YYYY-MM'),
                endDate : moment($scope.educationenddate).format('YYYY-MM')
            });
        if($scope.jobseekerskill !== undefined && $scope.jobseekerskill !== null && $scope.jobseekerskill !== "")
            $scope.skills.push({
                skill : $scope.jobseekerskill
            });


        var dataSend = {
            "id" : $scope.jobseeker.jobseekerid,
            "firstname" : $scope.jobseeker.firstname,
            "lastname" : $scope.jobseeker.lastname,
            "picture" : $scope.pictureUrl,
            "email" : $scope.jobseeker.email,
            "password" : $scope.jobseeker.password,
            "selfIntroduction" : $scope.jobseeker.selfIntroduction,
            "workExperience" : $scope.workexperience,
            "education" : $scope.education,
            "skills" : $scope.skills
        }

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




})

