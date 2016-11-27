$(function() {
	$('.create.btn.btn-primary').click(function() {
		$.ajax({
			type : "get",
			url : "/contract/preadd",
			success : function(data, status) {
				if (status == "success") {
					$('.bootstrap-table').html(data);
				}
			}
		});
	});

	xcpage.tableInitHandler = function(data) {
		$('.remove').click(function() {
			var index = $(this).parents('tr').attr('data-index');
			$.ajax({
				type : "delete",
				url : "/contract/remove/"+$('#table').data()['bootstrap.table'].data[index].id
			});
			
		});
		
		$('.update').click(function() {
			var index = $(this).parents('tr').attr('data-index');
			$.ajax({
				type : "get",
				url : "/contract/preadd?id="+$('#table').data()['bootstrap.table'].data[index].id,
				success : function(data, status) {
					if (status == "success") {
						$('.bootstrap-table').html(data);
					}
				}
			});
		});
	}

});
