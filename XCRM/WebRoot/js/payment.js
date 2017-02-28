$(function() {
	
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
		var customer = $("#customersearch").val();
		var contract = $("#contractselect").val();
		if( contract == '' || customer == '' ||customer == null || contract == null){
			alert("请选择收货人和合同");
			return;
		}
		// submit row
		$.ajax({
			url : "/payment/submitorder",
			type : 'post',
			data : {
				customer: $("#customervalue").val(),
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
	
	//get customer list
	$.get('/customer/list/', function(data){
		var subjects = new Array(); 
	    for( d in data){
	      subjects.push( data[d] );
	    }
	    $('#customersearch').typeahead({
	    	source: subjects,
	    	afterSelect: function (item) {
	    		$('#customervalue').val(item.id);
	    		$('#customerinfo').html( "<h4><b>公司名称:</b>" + item.company +"   <b>邮寄地址:</b> " + item.shiptoAddr + "</h4>");
	    	},
	    	displayText: function (item) {
                return item.name + "-" +item.company;//返回字符串
            }
	    }) 
	});
	
	//get contract list
	$.get('/contract/list/', function(data){
	    for( dd in data){
	      $('#contractselect').append("<option value='" + data[dd].id + "'>" + data[dd].name + "</option>");
	    }
	});
	
	  
	  
});
