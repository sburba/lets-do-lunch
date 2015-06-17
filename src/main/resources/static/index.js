var apiUrl = "/api/v1";
var autoCompletedPlaceName = null;

$(document).ready(function () {
    initializeMap();
    registerClickHandlers();
    refreshEvents();
    setInterval(refreshEvents, 1000);
});

function registerClickHandlers() {
    $("#new-event-form").submit(createNewEvent);
    $("#location").on("change", onLocationInputChange);
    $(document).on("click", function (event) {
        var isInSignupContainer = $(event.target).closest('.signup').length;
        var isInLikeButton = $(event.target).closest('.numLikes').length;
        if (!isInSignupContainer && !isInLikeButton) {
            var signupContainer = $(".signup");
            if (signupContainer.is(":visible")) {
                signupContainer.fadeOut("fast")
            }
        }
    });
}

function onLocationInputChange(event) {
    autoCompletedPlaceName = null;
}

function registerEventClickHandlers() {
    var likeButtons = $(".like");
    likeButtons.unbind("click");
    likeButtons.click(function (event) {
        var lunchItem = $(event.target).closest('.lunchItem');
        var signupContainer = lunchItem.find('.signup').first();
        signupContainer.fadeToggle("fast");
    });

    var addPersonForms = $(".addPerson");
    addPersonForms.unbind("submit");
    addPersonForms.submit(function (event) {
        var id = $(event.target).closest('.lunchitem').attr('data-id');
        var name = $(event.target).find('#name').first().val();
        event.target.reset();
        addPersonToEvent(id, name);
        $(".signup").fadeOut("fast");
    });

    var commentButtons = $(".leaveComment");
    commentButtons.unbind("click");
    commentButtons.click(function (event) {
        var lunchItem = $(event.target).closest('.lunchItem');
        var toggleArea = lunchItem.find('.toggleArea').first();
        toggleArea.slideToggle();
    });

    var commentSubmitForms = $(".addComment");
    commentSubmitForms.unbind("submit");
    commentSubmitForms.submit(function (event) {
        var id = $(event.target).closest('.lunchitem').attr('data-id');
        var comment = $(event.target).find("#submit").val();
        var toggleArea = $(event.target).find(".toggleArea").first();
        toggleArea.slideToggle(
            {
                complete: function () {
                    addCommentToEvent(id, comment);
                }
            }
        );
        event.preventDefault();
    });
}

function refreshEvents() {
    $.get(apiUrl + "/events", updateEventElements);
}

function addCommentToEvent(eventId, comment) {
    $.ajax({
        url: apiUrl + "/events/" + eventId.toString() + "/comments?comment=" + comment,
        method: "PUT",
        success: replaceEventData
    });
}

function addPersonToEvent(eventId, name) {
    $.ajax({
        url: apiUrl + "/events/" + eventId.toString() + "/people?name=" + name,
        method: "PUT",
        success: replaceEventData
    })
}

function updateEventElements(eventDatas) {
    for (var i = 0; i < eventDatas.length; i++) {
        addOrReplaceEventData(eventDatas[i]);
    }
}

function addOrReplaceEventData(eventData) {
    var eventElem = findEventForId(eventData.id);
    if (eventElem.length == 0) {
        addEvent(eventData);
    }
}

function replaceEventData(eventData) {
    $.get('event.mst', function (template) {
        var eventElem = findEventForId(eventData.id);
        var rendered = renderEvent(template, eventData);
        eventElem.html(rendered);
        registerEventClickHandlers();
    });
}

function findEventForId(eventId) {
    var filter = "li[data-id='" + eventId.toString() + "']";
    return $(filter).first();
}

function createNewEvent(event) {
    hideError();
    var location = autoCompletedPlaceName;
    // If there's no autocompleted value, just use the raw data from the input field
    if (!location) {
        location = $("#location").val();
    }
    var time = parseTime($("#time").val());
    var name = $("#organizer").val();
    event.target.reset();
    $.ajax({
        url: apiUrl + "/events",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            location: location,
            time: time.toJSON(),
            creator: name
        }),
        success: function (data, status) {
            addEvent(data);
        },
        error: function (_, textStatus, errMessage) {
            console.log(errMessage);
            showError();
        }
    });

    // We handle the form insert ourselves, don't actually try to post to a server
    return event.preventDefault();
}

function hideError() {
    var alert = $(".alert-fail").slideUp();
}

function showError() {
    $(".alert-fail").slideDown();
}

function renderEvent(template, event) {
    event.prettyTime = moment(event.time, moment.ISO_8601).format("h:mma zz");
    return Mustache.render(template, event);
}

function addEvent(event) {
    $.get('event.mst', function (template) {
            var eventItem = renderEvent(template, event);
            $(".items").prepend(eventItem);
            var firstEvent = $(".items li").eq(0);
            firstEvent.hide();
            firstEvent.slideDown("slow");
            registerEventClickHandlers();
        }
    );
}

function parseTime(timeString) {
    timeString = timeString.replace(/\s+/g, '');
    var time = moment(timeString, "h:mma");
    var amPmSpecified = /am|pm/i.test(timeString);
    var now = moment();
    if (amPmSpecified) {
        return time;
    } else if (time.isBefore(now)) {
        //TODO: Be more intelligent about assuming which time they meant.
        return time.add(12, 'hours');
    }
}

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

        autoCompletedPlaceName = place.name;
        infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
        infowindow.open(map, marker);
    });
}