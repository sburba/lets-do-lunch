<? 
	function getEventList(){
		return json_decode(file_get_contents("http://local.lets-do-lunch.com:8080/events"));	
	}
?>
<!doctype html>
<head>
<meta charset="utf-8">
<title>Lets-do-Lunch</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="styles.css">
<script>
function createNewEvent(){
	nameE = $("#organizer").val();
	locationE = $("#restaurant").val();
	timeE = $("#time").val();
	
	$.ajax( {
		type: 'POST',
		async: false,
	    url: "http://localhost:8080/events",
	    data: { name:  nameE ,
	    	location: locationE,
	    	time: timeE },
	    success: function() {
		    addNewEvent(nameE, locationE, timeE);
	    },
		error: function() {
	    }
	});
}

function addPersonToExistingEvent(evt){
	number = $(".signup" + evt.target.id).val();
	alert(evt.target.id);
	var nameE = $("#name" + number).val();
	$.ajax( {
		type: 'POST',
		async: false,
	    url: "http://local.lets-do-lunch.com:8080/events/" + number,
	    data: "name=" + nameE,
	    success: function(data) {
		    //updateLikeCounter();
	    }
	});
}

function addNewEvent(){
	$(".container").append('<li> <span class="details"> Lunch at <strong>' + locationE + '</strong> at <strong>' + timeE + '</strong> </span> <div class="numLikes"> <span class="number">1</span> <a class="like" href="#"><span class="icon icon-like2"></span></a> </div> <div class="signup"> <span class="arrow"></span> <form onsubmit="addPersonToExistingEvent()"> <label for="name">Your Name</label> <input id="name1" type="text" /> <br /> <button class="floatR" type="submit">Sign Up</button> </form> </div> </li>');
}
</script>
</head>
	<body>
		<? $eventList = getEventList(); ?>
		<? include("index.html") ?>
	</body>
</html>