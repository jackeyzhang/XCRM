$(function() {
	var r = window.location.search;
	var orderno = r.substring(r.indexOf("=")+1,r.length);
	
	$('#cancelinfolabel').hide();
    $('#cancelinfo').hide();
	
    $(":radio").click(function(){
	   if($(this).attr('id') == 'other'){
		   $('#cancelinfolabel').show();
		   $('#cancelinfo').show();
		   $('#cancelordercomments').val('');
	   }else{
		   $('#cancelinfolabel').hide();
		   $('#cancelinfo').hide();
		   $('#cancelordercomments').val($(this).val());
	   }
	});
	
	$('#cancelorderbutton').on('click', function() {
		// submit row
		$.ajax({
			url : "/cancelorder/cancel",
			type : 'post',
			data : {
				cancelreason: $("#cancelordercomments").val(),
				orderno: orderno,
			},
			success :function(indata, status) {
				if (status == "success") {
					location.href = "/order";
				}
			}
		});
	});	
	
});
