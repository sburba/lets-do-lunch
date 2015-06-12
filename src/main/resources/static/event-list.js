angular.module('letsdolunch', [])
    .controller('EventListController', function () {
        var eventList = this;
        eventList.events = [{
            "id": 0,
            "location": "Moe's Southwest Grill",
            "time": "2015-06-12T04:00Z",
            "people": ["Sam"],
            "comments": []
        }, {
            "id": 1,
            "location": "Texas",
            "time": "2015-06-12T04:00Z",
            "people": ["Sam"],
            "comments": ["this is a test"]
        }];

        eventList.addEvent = function () {
            eventList.events.push({text: eventList.eventText, done: false});
            eventList.eventText = '';
        };

        eventList.remaining = function () {
            var count = 0;
            angular.forEach(eventList.events, function (event) {
                count += event.done ? 0 : 1;
            });
            return count;
        };

        eventList.archive = function () {
            var oldTodos = eventList.events;
            eventList.events = [];
            angular.forEach(oldTodos, function (event) {
                if (!event.done) eventList.events.push(event);
            });
        };
    });