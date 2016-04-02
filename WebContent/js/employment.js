var app = angular.module('employment', ['firebase', 'ngMaterial']);

//reusable factory that generates $firebaseAuth instance
app.factory("Auth", ["$firebaseAuth",
	function($firebaseAuth){
		
		var ref = new Firebase("https://employement.firebaseio.com/");
		return $firebaseAuth(ref);
		
	
	}
	
]);

//set angular material theme to dark
app.config(function($mdThemingProvider) {
	  $mdThemingProvider.theme('default')
	    .dark();
});



//set routes
/*app.config(function($routeProvider){


      $routeProvider
          .when('/',{
                templateUrl: './views/login.html'
          })
          .when('/request',{
                templateUrl: './views/request.html'
          });


}); */

//login controler using angular fire functions
app.controller('loginCtrl', ["$scope", "Auth", "$http", "$timeout", function($scope, Auth, $http, $timeout){
	
	$scope.auth = Auth;
	$scope.message_completed = "";
	$scope.message_error = "";
	
	//listen for changes in authentication state
	$scope.auth.$onAuth(function(authData){
		
		$scope.authData = authData;
				
	});
	
	
	
	
	$scope.authenticate = function(){
		
		$scope.empID = $scope.eID + "@company.com";
		
		$scope.auth.$authWithPassword({
			email : $scope.empID,
			password : $scope.password
		})
		.then(onLogon)
		.catch(onError);
		
	}
	
	function onLogon(authData){
		console.log("Authenticated payload:", authData);	
	}
	
	function onError(error) {
        console.log("Authentication error:", error);
    }
	
	$scope.request = function(){
		
		$scope.mortID = $scope.mID;
		$scope.url = "http://employment.us-west-2.elasticbeanstalk.com/arnoldcloud/request";
		//$scope.url = "http://localhost:8080/Employment/arnoldcloud/request";
		$scope.method = "POST";
		var param = JSON.stringify({empID: $scope.eID, mortID: $scope.mortID});
		
		
		$http.post($scope.url, param)
			.then(
			
				function(response){
					
					$scope.data = response.data;
					$scope.status = response.status;
					
					console.log($scope.data);
					
					if($scope.status == "200"){
						
						$scope.message_completed = "Completed!";
						console.log($scope.data);
						
					}
					
				},
				function(response){
					
					$scope.data = response.data || "error";
					$scope.status = response.status;
					
					if($scope.status != "200"){
						
						$scope.message_error = "Error! Please try again!";
						
					}
					
					console.log($scope.data);
					
				}
			
			
			);
		
		
		console.log("Request received!");
		
		$timeout(function(){$scope.auth.$unauth();}, 4000);
		
		
		
	}
	
	
}]);

















/*app.controller('createStudent', function($scope, $http){

  $scope.sID = "";
  $scope.nickname= ""

  console.log($scope.sID + " " + $scope.nickname);

  $scope.makePostRequest = function(){

    $scope.student=[{
      studentId: $scope.sID,
      nickname: $scope.nickname
    }];

    var url = "http://104.130.3.60:8080/StudentManagementServices/arnoldcloud/create/";
    var data = angular.toJson($scope.student, true);
    var config = {
      headers : {
                   'Content-Type': 'application/json'
               }
    };

    $http.post(url, data, config)
    .then(
     function(response){
       // success callback
     },
     function(response){
       // failure callback
     }
  );

    console.log($scope.sID + " " + $scope.nickname);
  };


});*/
