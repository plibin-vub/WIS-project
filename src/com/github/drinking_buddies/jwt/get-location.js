getLocation();
function getLocation() {
	if (navigator.geolocation)
	{
		navigator.geolocation.getCurrentPosition(returnPosition,returnError);
	}
	else{returnError();}
}
function returnPosition(position){
	Wt.emit(Wt, {name: '${signal-name}', event: null, eventObject: null}, position.coords.latitude,position.coords.longitude); 
}
function returnError(position)
{
	Wt.emit(Wt, {name: '${signal-name}', event: null, eventObject: null},50.8833 ,4.7000); 
}