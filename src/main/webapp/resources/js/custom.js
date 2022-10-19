(function ($) {

	new WOW().init();

	jQuery(window).load(function() { 
		jQuery("#preloader").delay(100).fadeOut("slow");
		jQuery("#load").delay(100).fadeOut("slow");
	});


	//jQuery to collapse the navbar on scroll
	$(window).scroll(function() {
		if ($(".navbar").offset().top > 50) {
			$(".navbar-fixed-top").addClass("top-nav-collapse");
            $("#logo").addClass("logoLarge");
            $("#logo").removeClass("logo");
		} else {
			$(".navbar-fixed-top").removeClass("top-nav-collapse");
            $("#logo").removeClass("logoLarge");
            $("#logo").addClass("logo");
		}
	});

	//jQuery for page scrolling feature - requires jQuery Easing plugin
	$(function() {
		$('.navbar-nav li a').bind('click', function(event) {
			var $anchor = $(this);
			$('html, body').stop().animate({
				scrollTop: $($anchor.attr('href')).offset().top
			}, 1500, 'easeInOutExpo');
			event.preventDefault();
		});
		$('.page-scroll a').bind('click', function(event) {
			var $anchor = $(this);
			$('html, body').stop().animate({
				scrollTop: $($anchor.attr('href')).offset().top
			}, 1500, 'easeInOutExpo');
			event.preventDefault();
		});
	});
    var url = window.location.href; /* 获取完整URL */

//display career  product  support problem   aboutus display
    for(var i =1;i<=5;i++){
    	var li = $("#l"+i);
    	if(li.hasClass("active")){
    		li.removeClass("active");
    		break;
		}
	}
    if(url.indexOf("display")>-1){
        $("#l4").addClass("active");
	}else if(url.indexOf("career")>-1){
        $("#l5").addClass("active");

    }else if(url.indexOf("product")>-1){
        $("#l2").addClass("active");

    }else if(url.indexOf("support")>-1){
        $("#l3").addClass("active");

    }else if(url.indexOf("problem")>-1){
        $("#l3").addClass("active");

    }else if(url.indexOf("aboutus")>-1){
        $("#l5").addClass("active");

    }else {
        $("#l1").addClass("active");
    }






})(jQuery);
