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
				// go to payment
				var ids = '';
				for (var i = 0; i < $('.itemid').length; i++) {
					ids += $($('.itemid')[i]).val()
							+ (($('.itemid').length - 1 == i) ? '' : ',');
				}
				$.ajax({
					url : "/cartlist/gotoPayment",
					type : 'post',
					data : {
						ids : ids,
						amount : $(".amount").html(),
						price : $("#price").val(),
						ordercomments : $("#ordercomments").val()
					},
					success : function(indata, status) {
						if (status == "success") {
							location.href = "/payment";
						}
					}
				});
			});
	
	$('#changeorder').on(
			'click',
			function() {
				var r = window.location.search;
				var orderno = r.substring(r.indexOf("=")+1,r.length);
				// go to payment
				var ids = '';
				for (var i = 0; i < $('.itemid').length; i++) {
					ids += $($('.itemid')[i]).val()
							+ (($('.itemid').length - 1 == i) ? '' : ',');
				}
				$.ajax({
					url : "/cartlist/gotoPayment?orderno=" + orderno,
					type : 'post',
					data : {
						ids : ids,
						amount : $(".amount").html(),
						price : $("#price").val(),
						ordercomments : $("#ordercomments").val()
					},
					success : function(indata, status) {
						if (status == "success") {
							location.href = "/spayment?orderno=" + orderno;
						}
					}
				});
			});
});
