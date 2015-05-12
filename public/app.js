
var app = angular.module('catboop', ['ngResource']);

app.controller('imgCtrl', function($scope, $http) {
	$scope.idx = 1;
	$scope.increment() = function($scope) {
		$scope.idx = $scope.idx + 1;
	}
	$http.get('/cats/' + $scope.idx).success( function(data) {
		$scope.catURL = data.URL;
	})
})