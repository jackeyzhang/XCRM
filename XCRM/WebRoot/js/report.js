$(function() {
	$('#productsalereportbutton').click(function() {
		window.location.href = "/report/productsalereport";
	});

	$('#productsalereportbutton').click(function() {
		window.location.href = "/report/productsalereport";
	});

	$('#orderreportbutton').click(function() {
		window.location.href = "/report/orderreport";
	});

	$('#salesreportbutton').click(function() {
		window.location.href = "/report/salesreport";
	});

	$('#customerreportbutton').click(function() {
		window.location.href = "/report/customerreport";
	});

	$('#queryingbutton-productsales').click(function() {
		$.ajax({
			type : "post",
			url : "/report/querying",
			data : {
				startdate: $("#startdate").val(),
				enddate: $("#endate").val(),
				orderstatus: $("#orderstatusselect").val()
			},
			success : function(data, status) {
				if (status == "success") {
					$('#reporttable').bootstrapTable('load', data);
				}
			},
			error : function() {
				showAlert("查询失败");
			}
		});
	});

	$('#startdate').datetimepicker({
		format : 'yyyy-mm-dd',
		autoclose : true,
		todayBtn : true,
		startView : 2,
		minView : 2
	});

	$('#endate').datetimepicker({
		format : 'yyyy-mm-dd',
		autoclose : true,
		todayBtn : true,
		startView : 2,
		minView : 2
	});

});