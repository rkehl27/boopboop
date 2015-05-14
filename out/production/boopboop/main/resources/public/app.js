
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

		var catImg = document.getElementById('catImg');
		$scope.findClosestCat(posX, posY, catImg);
	};

	$scope.findClosestCat = function(noseX, noseY, catImg) {
		$http.get('/cat/' + noseX + ',' + noseY).success(function(data) { $scope.displayCatPhoto(catImg, data); });
	};

	$scope.displayCatPhoto = function(catImg, cat) {
		catImg.style.display = "none";
		catImg.onload = function() {
			if( cat.width > cat.height ) {
				this.style.height = "100%";
				this.style.width = "";
				this.style.margin = "0px " + (-cat.xShift) + "px";
			}
			else {
				this.style.height = "";
				this.style.width = "100%";
				this.style.margin = (-cat.yShift) + "px 0px";
			}
			this.style.display = "block";
		}
		catImg.src = cat.URL;
	};

});

app.controller('submitCtrl', function($scope, $http) {

	var submitFrame = document.getElementById('submitFrame');
	var submitURL = document.getElementById('submitURL');
	var submitBtn = document.getElementById('updateURLbtn');
	var submitImg = document.getElementById('submitImg');
	var submitURLdiv = document.getElementById('submitURLdiv');

	submitBtn.onclick = function() {
		submitURLdiv.style.display = "none";

		submitImg.onload = function() {
			submitImg.style.display = "block";
			var w = submitImg.style.width;
			var h = submitImg.style.height;
			submitFrame.style.display = "inline-block";
			if( w > h ) {
				submitImg.style.height = "100%";
				submitFrame.style.width = "auto";
			}
			else {
				submitImg.style.width = "100%";
				submitFrame.style.height = "auto";
			}
		};
		submitImg.src = submitURL.value;
		document.getElementById('h3instr').innerHTML = "Click the cat's nose to submit the photo:";

	}; //End button click event

	submitImg.onclick = function(e) {
		if( submitImg.src == "" )
			return;

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
		posX -= submitFrame.offsetLeft;
		posY -= submitFrame.offsetTop;
		var str = submitImg.clientWidth + "," + submitImg.clientHeight + "," + posX + "," + posY + "," + encodeURIComponent(submitURL.value);

		$http.post('/submit/' + str);
		document.getElementById('h3instr').innerHTML = "Submission successful!";

    };

});