var apiUrl = "http://localhost:8080/api/v1";
var first = true;

$(document).ready(function () {
    registerClickHandlers();
    refreshEvents();
});

function registerClickHandlers() {

    $(".createItem").submit(createNewEvent);
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
        success: addOrReplaceEventData
    });
}

function addPersonToEvent(eventId, name) {
    $.ajax({
        url: apiUrl + "/events/" + eventId.toString() + "/people?name=" + name,
        method: "PUT",
        success: addOrReplaceEventData
    })
}

function updateEventElements(eventDatas) {
    if (eventDatas.length === 0) {
        first = false;
        initialize();
    }
    for (var i = 0; i < eventDatas.length; i++) {
        addOrReplaceEventData(eventDatas[i]);
    }
}

function addOrReplaceEventData(eventData) {
    var eventElem = findEventForId(eventData.id);
    if (eventElem.length != 0) {
        replaceEventData(eventElem, eventData);
    } else {
        addEvent(eventData);
    }
}

function replaceEventData(eventElem, eventData) {
    $.get('event.mst', function (template) {
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
    var rawLocation = $("#pac-input").val();
    var location = rawLocation.substr(0, rawLocation.indexOf(','));
    var time = $("#time").val();
    var name = $("#organizer").val();
    var comment = $("#message").val();
    event.target.reset();
    $.ajax({
        url: apiUrl + "/events",
        method: "POST",
        data: {
            location: location,
            time: time,
            name: name,
            comment: comment
        },
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
    return Mustache.render(template, event);
}

function addEvent(event) {
    $.get('event.mst', function (template) {
            var eventItem = renderEvent(template, event);
            $(".items").prepend(eventItem);
            var firstEvent = $(".items li").eq(0);
            firstEvent.hide();
            firstEvent.slideDown("slow", function() {
                if (first) {
                    first = false;
                    initialize();
                }
            });
            registerEventClickHandlers();
        }

    );
}