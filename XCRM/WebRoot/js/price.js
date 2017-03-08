$(function() {
	$('.create.btn.btn-primary').click(function() {
		var index = $(this).parents('tr').attr('data-index');
		$.ajax({
			type : "get",
			url : "/price/preadd?pid=0",
			success : function(data, status) {
				if (status == "success") {
					$('.bootstrap-table').html(data);
				}
			}
		});
	});

	xcpage.tableInitHandler = function(data) {
		$('.update').click(function() {
			var index = $(this).parents('tr').attr('data-index');
			var pid = $('#table').data()['bootstrap.table'].data[index].product;
			var id = $('#table').data()['bootstrap.table'].data[index].id;
			$.ajax({
				type : "get",
				url : "/price/preadd?pid="+pid + "&id=" + id,
				success : function(data, status) {
					if (status == "success") {
						$('.bootstrap-table').html(data);
					}
				}
			});
			
			var urlhere = "/price/loadingAddData?id="+ id;
			setTimeout(function() {
				$.ajax({
					type : "get",
					url : urlhere,
					success : function(indata, status) {
						if (status == "success") {
							loadData('test', indata);
						}
					}
				})
			}, 1000);
		});
	};
	
	
	$('#closebutton').click(function() {
		window.location.href = '/price/';
	});

	$('#submitprice').click(function(event) {
		form = $('#modal-form');
		var row = {};
		//fire validate
		form.validator('validate');
		if(form.validator().data('bs.validator').hasErrors()){
			return;
		}
		if($('#productvalue').val() == ''){
			event.preventDefault();
			alert("请选择一个已存在的产品!");
		}else{
			// get input value to row
			form.find('input[name]').each(function() {
				if($(this).attr('type') == "radio"){
					if($(this).is(":checked")){
						row[$(this).attr('name')] = $(this).val();
					}
				}else{
					row[$(this).attr('name')] = $(this).val();
				}
			});
			// get select value to row
			form.find('select[name]').each(function() {
				if($(this).val() instanceof Array){
					row[$(this).attr('name')] = $(this).val().join(',');
				}else{
					row[$(this).attr('name')] = $(this).val();
				}
			});
			// submit row
			$.ajax({
				url : $('#id') == "" ? "/price/add"
						: "/price/update",
				type : 'post',
				data : row,
				success :function(indata, status) {
					if (status == "success") {
					}
				},
				error: function () {
					alert("编辑失败");
	            }
			});
		}
	});
	
	
	function loadData(title, row) {
		form = $('#modal-form');
		row = row || {
			id : '',
		}; // default row value
		form.data('id', row.id);
		form.find('.modal-title').text(title);
		for ( var name in row) {
			//set value to dialog for radio input
			if( form.find('input[name="' + name + '"]').attr('type') == "radio"){
				form.find('input[name="' + name + '"]').each(function() {
					if( $(this).val() == row[name]){
						$(this).attr("checked", true);
					}else{
						$(this).removeAttr("checked");
					}
				});
			}else{
				//set value to dialog for input
				form.find('input[name="' + name + '"]').val(row[name]);
				//set value to dialog for select
				form.find('select[name="' + name + '"]').val(row[name]);
				// set value for dialog for multiple select
				if(form.find('select[name="' + name + '"]').attr('multiple') == "multiple"){
					if(row[name]){
						form.find('select[name="' + name + '"]').multiselect('select', row[name].split(","));
					}else{
						form.find('select[name="' + name + '"]').multiselect('deselectAll',false);
						form.find('select[name="' + name + '"]').multiselect('updateButtonText');
					}
				}
			}
		}
	};

});
