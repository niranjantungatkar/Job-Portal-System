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
            	templateUrl: './templates/view.company.updateJobseekerProfile.html',
            	params: {profileDet: null},
            	controller: 'controllerJobSeekerProfile'
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