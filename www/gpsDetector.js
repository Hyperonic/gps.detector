
var exec = require('cordova/exec');

var gpsDetect = function() {};

gpsDetect.prototype.checkGPS = function(alertShown, successCallback, failureCallback) {
	exec(successCallback, failureCallback, 'GpsDetector', 'gpsDetection', [alertShown]);
};

var gpsDetect = new gpsDetect();
module.exports = gpsDetect;
