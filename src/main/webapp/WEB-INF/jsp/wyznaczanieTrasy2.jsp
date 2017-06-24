<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" isELIgnored="false"
	contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Nextbike - wyznaczanie trasy cd.</title>
<link href="/nextbike/static/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="/nextbike/static/css/row.css" rel="stylesheet">
<script src="/nextbike/static/bootstrap/css/bootstrap.min.css"></script>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="row">
		<center>
			Średni czas przejazdu: ${time }
			<div id="map"></div>
		</center>
	</div>
</body>
<script>
	var locationA;
	var locationB;
	locationA = {
		lat : ${pointA.lat},
		lng : ${pointA.lng}};
	locationB = {
		lat : ${pointB.lat},
		lng : ${pointB.lng}}; 
	function initMap() {
		var directionsDisplay = new google.maps.DirectionsRenderer;
		var directionsService = new google.maps.DirectionsService;
		var map = new google.maps.Map(document.getElementById('map'), {
			zoom : 15,
			center : {
				lat : 51.7592485,
				lng : 19.4559833
			}
		});
		directionsDisplay.setMap(map);
		calculateAndDisplayRoute(directionsService, directionsDisplay);
	}
	function calculateAndDisplayRoute(directionsService, directionsDisplay) {
		directionsService.route({
			origin : locationA,
			destination : locationB,
			travelMode : google.maps.TravelMode.BICYCLING
		}, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				directionsDisplay.setDirections(response);
			}
		});
	}
</script>
<script
	src="https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/markerclusterer.js">
	
</script>
<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBQAR809YkzO5lbIQ_dht4OlSFEaznt2T4&callback=initMap">
	
</script>
<style>
#map {
	height: 650px;
	width: 100%;
}
</style>
</html>