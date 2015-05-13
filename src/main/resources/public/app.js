
function clickFn() {
	alert('hello function');
}

var app = angular.module('catboop', ['ngResource']);

app.controller('imgCtrl', function($scope, $http) {

	var catFrame = document.getElementById('catFrame');
	catFrame.onclick = function(e) {
		var posX = 0;
		var posY = 0;
		if( e.offsetX || e.offsetY ) {
			posX = e.offsetX;
			posY = e.offsetY;
		}
		else if( e.layerX || e.layerY ) {
			posX = e.layerX;
			posY = e.layerY;
		}
		//alert('X,Y: ' + posX + ', ' + posY);

		var catImg = document.getElementById('catImg');
		$scope.findClosestCat(posX, posY, catImg);
	};

	$scope.findClosestCat = function(noseX, noseY, catImg) {
		//var cat = {_id:1234, width:550, height:486, noseX:0, noseY:0, xShift:50, yShift:0, URL:"http://tonsofcats.com/wp-content/uploads/2013/10/l-Derpy-aww-550x486.jpg"};
		//alert('Finding: ' + noseX + ', ' + noseY);
		$http.get('/cat/' + noseX + ',' + noseY).success(function(data) { $scope.displayCatPhoto(catImg, data); });
	};

	$scope.displayCatPhoto = function(catImg, cat) {
		catImg.style.display = "block";
		catImg.src = cat.URL;
		if( cat.width > cat.height ) {
			catImg.style.height = "100%";
			catImg.style.width = "";
			catImg.style.margin = "0px " + (-cat.xShift) + "px";
		}
		else {
			catImg.style.height = "";
			catImg.style.width = "100%";
			catImg.style.margin = (-cat.yShift) + "px 0px";
		}
	};

});