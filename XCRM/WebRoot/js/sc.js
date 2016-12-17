$(function() {
	$('.sc').on('click',function(){
		location.href="/cartlist";
	});
	$('.sc-btn').on('click',function(){
		$('#modal-form').submit();
	});
});
