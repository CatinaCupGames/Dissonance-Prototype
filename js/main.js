$(document).ready(function() { 
	$(document).foundation();
	
	$.get("http://hypereddie.com/dissonance/commits.php", function(html) {
		$("#commits").html(html).delay(1000).fadeIn();
	});

    $.get('http://hypereddie.com/dissonance/percent.php', function(data) {
        $('#p').css('width', data + '%');
        $('#t').text(Math.round(data) + '%');
    });
});
