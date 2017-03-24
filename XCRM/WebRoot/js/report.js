$(function() {
	$('#productsalereportbutton').click(function() {
		window.location.href = "/report/productsalereport";
	});

	$('#orderpaymentreportbutton').click(function() {
		window.location.href = "/report/orderpaymentreport";
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
			url : "/report/queryingproductsales",
			data : {
				startdate: $("#startdate").val(),
				enddate: $("#endate").val(),
				orderstatus: $("#orderstatusselect").val().toString()
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
	
	$('#queryingbutton-orderreport').click(function() {
		$.ajax({
			type : "post",
			url : "/report/queryingorderpaymentreport",
			data : {
				startdate: $("#startdate").val(),
				enddate: $("#endate").val(),
				topay: $("#topay").is(':checked'),
				todue: $("#todue").is(':checked')
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
	
	$('#queryingbutton-customer').click(function() {
		$.ajax({
			type : "post",
			url : "/report/queryingcustomerreport",
			data : {
				startdate: $("#startdate").val(),
				enddate: $("#endate").val()
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
	
	$('#queryingbutton-sales').click(function() {
		$.ajax({
			type : "post",
			url : "/report/queryingsalereport",
			data : {
				startdate: $("#startdate").val(),
				enddate: $("#endate").val()
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
	
	var startdatestr = getFirstDayOfCurrentDay( );
	$('#startdate').val( startdatestr );

	$('#endate').datetimepicker({
		format : 'yyyy-mm-dd',
		autoclose : true,
		todayBtn : true,
		startView : 2,
		minView : 2
	});
	
	var enddatestr = getCurrentDay();
	$('#endate').val( enddatestr );
	
	function getFirstDayOfCurrentDay( ){
		var cdate = new Date();
		return cdate.getFullYear() + "-" + (cdate.getMonth()+1) + "-" + "01";
	}
	
	function getCurrentDay(){
		var cdate = new Date();
		return  cdate.getFullYear() + "-" + (cdate.getMonth()+1) + "-" + cdate.getDate();
	}
	

});