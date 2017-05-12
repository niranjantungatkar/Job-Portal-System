/*
 * Main job portal app
 */

var jobPortalApp = angular.module('jobPortalApp',['ui.router']);

jobPortalApp.config(function($stateProvider, $urlRouterProvider){
	
	$urlRouterProvider
		.otherwise('/');
	
	$stateProvider
		.state('home',
			{
				url:'/',
				templateUrl: './templates/homepage.html',
				resolve : {
					userSession:  function($http) {
			            return $http({method: 'GET',
			            			  url: '/session'
			            		}).success(function(data) {
			            				  return data;
			            		});
			            }
				},
				controller: 'controllerHome'
			})
		.state('home.verify', 
				{
					url:'verification',
					templateUrl:'./templates/view.verify.html',
					controller:'controllerVerify',
					params:{profile:null}
				})
		.state('home.signin', 
				{
					url:'signin',
					templateUrl:'./templates/view.signin.html',
					controller:'controllerSignin'
				})
		.state('home.jobseekerprofile',
			{
				url:'profile/jobseeker',
				templateUrl : './templates/view.jobseeker.profile.html',
				params : { profileDet: null},
				controller : 'controllerJobSeekerProfile'
			})
        .state('home.updateJobseekerProfile',
        	{
            	url:'updateJobseekerProfile',
            	templateUrl: './templates/view.jobseeker.updateProfile.html',
            	params: {profileDet: null},
            	controller: 'controllerJobSeekerUpdateProfile'
        	})
        .state('home.viewJobs',
            {
                url:'viewJobs',
                templateUrl: './templates/view.jobseeker.viewJobs.html',
                params: {profileDet: null},
                controller: 'controllerViewJobs'
            })
        .state('home.viewJob',
            {
                url:'viewJob',
                templateUrl: './templates/view.jobseeker.viewJobs.viewJob.html',
                params: {jobAndProfile: null},
                controller: 'controllerViewJob'
            })
        .state('home.interestedjobs',
            {
                url:'interestedjobs',
                templateUrl: './templates/view.jobseeker.interestedjobs.html',
                params: {profileDet: null},
                controller: 'controllerInterestedJobs'
            })
        .state('home.appliedjobs',
            {
                url:'appliedjobs',
                templateUrl: './templates/view.jobseeker.appliedjobs.html',
                params: {profileDet: null},
                controller: 'controllerAppliedJobs'
            })
		.state('home.companyprofile', 
			{
				url:'company',
				templateUrl: './templates/view.company.profile.html',
				params: {companyDet: null},
				controller: 'controllerCompanyProfile' 
			})
		.state('home.update',
			{
				url:'update',
				templateUrl: './templates/view.company.update.html',
				controller: 'controllerCompanyUpdate',
				params: {companyDet: null}
			})
		.state('home.postjob',
			{
				url:'job',
				templateUrl: './templates/view.company.postjob.html',
				controller: 'controllerCompanyPostJob',
				params: {companyDet: null}
			})
		.state('home.updateJobPosting',
			{
					url:'update/jobposting',
					templateUrl: './templates/view.update.jobposting.html',
					controller: 'controllerUpdateJobPosting',
					params: { companyDet: null}
			})
		.state('home.viewjobpostings', 
			{
				url:'jobpostings',
				templateUrl: './templates/view.company.postings.html',
				controller: 'controllerCompanyPostings',
				params: {companyDet: null}
			})
	
}).controller('mainController', function(){
});