$(function() {
	var r = window.location.search;
	var orderno = r.substring(r.indexOf("=")+1,r.length);
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
			})
	};
	
	
	$('#backtoordercart').on('click', function() {
		location.href = "/cartlist";
	});
	
	$('#deliverytime').datetimepicker({
	    format: 'yyyy-mm-dd',
	    autoclose: true,
	    todayBtn: true,
	    startView:2,
	    minView:2
	});
	
	$('#submitorder').on('click', function() {
		// submit row
		$.ajax({
			url : "/payment/submitorder",
			type : 'post',
			data : {
				customer: $("#customerselect").val(),
				contract: $("#contractselect").val(),
				paymenttype: $("#paymentwayselect").val(),
				deliverytime: $("#deliverytime").val(),
				paid: $("#paid").val(),
				paymentcomments: $("#paymentcomments").val(),
				status: 1
			},
			success :function(indata, status) {
				if (status == "success") {
					location.href = "/order";
				}
			}
		});
	});	
	
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
					location.href = "/order";
				}
			}
		});
	});	
	
	
	//get customer list
	var customerlist;
	$.get('/customer/list/', function(data){
		customerlist = data;
	    for( d in data){
	      $('#customerselect').append("<option value='" + data[d].id + "'>" + data[d].name + "</option>");
	    }
	});
	
	//get contract list
	$.get('/contract/list/', function(data){
	    for( dd in data){
	      $('#contractselect').append("<option value='" + data[dd].id + "'>" + data[dd].name + "</option>");
	    }
	});
	
	  //set attribute value from attributeidList
	  $('#customerselect').change(function(){ 
		  $('#customerinfo').html("");
		   for(d in customerlist){
			   if( customerlist[d].id == $(this).children('option:selected').val()){
				   $('#customerinfo').html( "<h4><b>公司名称:</b>" + customerlist[d].company +"   <b>邮寄地址:</b> " + customerlist[d].shiptoAddr + "</h4>");
			   }
		   }
	  });
	  
	  
});
