angular.module('letsdolunch', [])
    .controller('EventCreateController', function() {
        angular.element(document).ready( function() {
            initializeMap();
        });
    });

function initializeMap() {
    var mapOptions = {
        center: new google.maps.LatLng(42.2340218, -83.72218649999),
        zoom: 13,
        disableDefaultUI: true
    };
    var map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);

    var location = /** @type {HTMLInputElement} */(
        document.getElementById('location'));
    var name = document.getElementById('organizer');
    var time = document.getElementById('time');
    var submit = document.getElementById('submit-event');

    navigator.geolocation.getCurrentPosition(function (position) {
        map.setCenter({lat: position.coords.latitude, lng: position.coords.longitude});
    });

    map.controls[google.maps.ControlPosition.TOP_LEFT].push(name);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(time);
    map.controls[google.maps.ControlPosition.LEFT_TOP].push(location);
    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(submit);

    var autocomplete = new google.maps.places.Autocomplete(location);
    autocomplete.bindTo('bounds', map);

    var infowindow = new google.maps.InfoWindow();
    var marker = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });

    google.maps.event.addListener(autocomplete, 'place_changed', function () {
        infowindow.close();
        marker.setVisible(false);
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            //If we don't have any geometry, we can't really do anything else
            return;
        }

        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);  // Why 17? Because it looks good.
        }
        marker.setIcon(/** @type {google.maps.Icon} */({
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(35, 35)
        }));
        marker.setPosition(place.geometry.location);
        marker.setVisible(true);

        var address = '';
        if (place.address_components) {
            address = [
                (place.address_components[0] && place.address_components[0].short_name || ''),
                (place.address_components[1] && place.address_components[1].short_name || ''),
                (place.address_components[2] && place.address_components[2].short_name || '')
            ].join(' ');
        }

        infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
        infowindow.open(map, marker);
    });
}