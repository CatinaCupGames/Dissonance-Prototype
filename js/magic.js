var percent = 0;
$.ajax({
  url: "http://hypereddie.com/dissonance/percent.php",
  cache: true
}).done(function(html) {
    percent = html;
    $('#progressbar').progressbar('setMaximum', 100);
    $('#progressbar').progressbar('setPosition', percent);
});

$.ajax({
  url: "http://hypereddie.com/dissonance/commits.php",
  cache: true
}).done(function(html) {
  $("#commit_box_stuff").html(html);
  setTimeout(function() {
	$("#commit1").fadeIn(1300);
	$("#commit2").fadeIn(1600);
	$("#commit3").fadeIn(1900);
  }, 300);
});