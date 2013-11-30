function getGeoLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
				function(position){
					var lat = position.coords.latitude;
					var lon = position.coords.longitude;
					${wt-call};
				}
		);
	}
}