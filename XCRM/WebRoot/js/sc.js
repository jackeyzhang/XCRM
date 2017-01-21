$(function() {
	$('.sc').on('click', function() {
		location.href = "/cartlist";
	});
	$('#sc').on('click', function() {
		$('#modal-form').submit();
	});
	$('#payment').on(
			'click',
			function() {
				if ($('.itemid').length > 0) {
					var ids = '';
					for (var i = 0; i < $('.itemid').length; i++) {
						ids += $($('.itemid')[i]).val()
								+ (($('.itemid').length - 1 == i) ? '' : ',');
					}
					location.href = "/payment";
					
					/*$.ajax({
						url : "/cartlist/save",
						type : 'post',
						data : {
							ids : ids,
							amount : $(".amount").html(),
							price : $("#price").val(),
							ordercomments : $("#ordercomments").val()
						},
						success : function(indata, status) {
							if (status == "success") {
								location.href = "/order";
							}
						}
					});*/

				}
			});
});
