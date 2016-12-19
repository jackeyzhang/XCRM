$(function() {
	$('.sc').on('click', function() {
		location.href = "/cartlist";
	});
	$('#sc').on('click', function() {
		$('#modal-form').submit();
	});
	$('#payment').on('click', function() {
		if ($('.itemid').length > 0) {
			var ids = '';
			for (var i = 0; i < $('.itemid').length; i++) {
				ids += $($('.itemid')[i]).val() + (($('.itemid').length-1==i)?'':',');
			}
//			location.href = "/cartlist/save?ids=" + ids;
			
			var row = {};
			row["comments"] = "";
			if( $("textarea[name^='comments-']").length > 0){
				for (var i = 0; i < $("textarea[name^='comments-']").length; i++) {
					row["comments"] += $("textarea[name^='comments-']")[i].name + "=" + $($("textarea[name^='comments-']")[i]).val() + (($("textarea[name^='comments-']").length-1==i)?'':',');
				}
			}
			
			if( $(".amount").length > 0 ){
				row["amount"] = $($(".amount")[0]).html();
			}
			
			if( $("#price").length > 0 ){
				row["price"] = $("#price").val();
			}
			
			// submit row
			$.ajax({
				url : "/cartlist/save?ids=" + ids,
				type : 'post',
				data : row,
				success :function(indata, status) {
					if (status == "success") {
						location.href = "/order";
					}
				}
			});
			
			
		}
	});
});
