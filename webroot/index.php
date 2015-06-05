<? 
	function getEventList(){
		$eventList = json_decode(file_get_contents("http://local.lets-do-lunch.com:8080/events"));	
		echo ($eventList);
	}
?>
<!doctype html>
<head>
<meta charset="utf-8">
<title>Lets-do-Lunch</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script>
function createNewEvent(){
	$.ajax( {
		type: 'POST',
		async: false,
	    url: "http://local.lets-do-lunch.com:8080/events",
	    data: {  name:  $("#name").val() ,
	    	location: $("#location").val(),
	    	time: $("#time") };,
	    success: function(data) {
		    addNewEvent(JSON.parse(data));
	    }
	});
}

function addPersonToExistingEvent(){
	$.ajax( {
		type: 'POST',
		async: false,
	    url: "http://local.lets-do-lunch.com:8080/events/" + $("#divId"),
	    data: $("#name").val(),
	    success: function(data) {
		    updateLikeCounter(JSON.parse(data));
	    }
	});
}
</script>
</head>
	<body>
		<?getEventList(); ?>
		<? include("index.html") ?>
	</body>
</html>