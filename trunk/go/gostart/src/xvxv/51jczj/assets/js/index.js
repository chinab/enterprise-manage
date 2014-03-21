$(function(){
	$(".content-item div").each(function(index,element){
		$(element).css("background-color",page.bgColors[index]);
		
	});
});

var page={
	bgColors:["#b012b4","#1abb9b","#aa0029","#b15d00","#282c7c",
			"#139838","#118b73","#b50000","#e2d500","#e200bf"]
}