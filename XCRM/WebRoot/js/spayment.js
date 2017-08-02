$(function() {
	var r = window.location.search;
	var orderno = r.substring(r.indexOf("=")+1,r.length);
	var customerid = $('#customerid').html();
	init = {
			$paymenttable : $('#paymenttable').bootstrapTable({
				url : "/orderview/loadingPayment?orderno=" + orderno,
				queryParamsType: "limit",
				pageNumber: 1,
				pageList: [10, 25, 50, 100],
				clickToSelect:true,
				exportDataType: "basic",
				sidePagination: "server",//表格分页的位置  
				onLoadSuccess:function(data){
					if(xcpage.tableInitHandler)
						xcpage.tableInitHandler(data);
				}
			}),
	
			$allpaymenttable : $('#allpaymenttable').bootstrapTable({
				url : "/orderview/loadingAllPayments?customerid=" + customerid,
				queryParamsType: "limit",
				pageNumber: 1,
				pageList: [10, 25, 50, 100],
				clickToSelect:true,
				exportDataType: "basic",
				sidePagination: "server",//表格分页的位置  
				onLoadSuccess:function(data){
					if(xcpage.tableInitHandler)
						xcpage.tableInitHandler(data);
				}
			})
	};
	
	
	$('#spaymentsubmitorder').on('click', function() {
		// submit row
		$.ajax({
			url : "/spayment/submitorder",
			type : 'post',
			data : {
				paymenttype: $("#paymentwayselect").val(),
				paid: $("#paid").val(),
				paymentcomments: $("#paymentcomments").val(),
				status: 1
			},
			success :function(indata, status) {
				if (status == "success") {
					location.href = "orderview?orderno=" + orderno;
				}
			}
		});
	});	
	
	$('#showallpayment').click(function () {
		var checked = $('#showallpayment').is(':checked');
		var customerid = $('#customerid').html();
		$('#allpaymenttable').bootstrapTable('refresh',{url:"/orderview/loadingAllPayments?customerid=" + customerid + "&showall=" + checked });
	});
	
});
