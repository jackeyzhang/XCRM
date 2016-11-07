$(function() {
	isvalid = true;
	xcpage = {
		$modal : $('#modal').modal({
			show : false
		}),
		$alert : $('.alert').hide(),
		$table : $('#table').bootstrapTable({
			url : API_URL,
			pageNumber: 1,
			pageList: [10, 25, 50, 100],
			clickToSelect:true,
			showExport: true,
			exportDataType: "basic",
            onEditableSave: function (field, row, oldValue, $el) {
                $.ajax({
                    type: "post",
                    url: UPDATE_API_URL,
                    data: row,
                    success: function (data, status) {
                        if (status == "success") {
                        	showAlert("编辑成功");
                        }
                    },
                    error: function () {
                    	showAlert("编辑失败");
                    },
                    complete: function () {

                    }

                });
             }
		}),
		
		$form : $('#modal-form'),
		
		$calendar : $('#calendar').fullCalendar({
			locale: 'zh-cn',
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,basicWeek,basicDay'
			},
			defaultDate: '2016-11-11',
			navLinks: true, // can click day/week names to navigate views
			editable: true,
			eventLimit: true // allow "more" link when too many events
		})
	}
	
	$("[data-toggle='tooltip']").tooltip();

	$('.create').click(function() {
		xcpage.$form[0].reset();//reset form
		showModal($(this).text());
	});
	xcpage.$modal.find('.submit').click(																									
			function() {
				var row = {};
				//fire validate
				xcpage.$form.validator('validate');
				if(xcpage.$form.validator().data('bs.validator').hasErrors()){
					return;
				}
				// get input value to row
				xcpage.$modal.find('input[name]').each(function() {
					if($(this).attr('type') == "radio"){
						if($(this).is(":checked")){
							row[$(this).attr('name')] = $(this).val();
						}
					}else{
						row[$(this).attr('name')] = $(this).val();
					}
				});
				// get select value to row
				xcpage.$modal.find('select[name]').each(function() {
						row[$(this).attr('name')] = $(this).val();
				});
				// submit row
				$.ajax({
					url : xcpage.$modal.data('id') == "" ? ADD_API_URL
							: UPDATE_API_URL,
					type : 'post',
					data : row,
					success : function() {
						xcpage.$modal.modal('hide');
						xcpage.$table.bootstrapTable('refresh');
						showAlert((xcpage.$modal.data('id') ? '更新'
								: '创建')
								+ '成功', 'success');
					},
					error : function() {
//						xcpage.$modal.modal('hide');
						showAlert((xcpage.$modal.data('id') ? '更新'
								: '创建')
								+ '失败!', 'danger');
					}
				});
			});
	
});
function queryParams(params) {
	return {};
}
function actionFormatter(value) {
	return [ '<a class="update" href="javascript:" title="编辑">编辑</a>',
			'<a class="remove" href="javascript:" title="删除">删除</a>']
			.join('   ');
}

// update and delete events
window.actionEvents = {
	'click .update' : function(e, value, row) {
		showModal($(this).attr('title'), row);
	},
	'click .remove' : function(e, value, row) {
		if (confirm('确定删除?')) {
			$.ajax({
				url : REMOVE_API_URL + row.id,
				type : 'delete',
				success : function() {
					xcpage.$table.bootstrapTable('refresh');
					showAlert('删除成功!', 'success');
				},
				error : function() {
					showAlert('删除失败!', 'danger');
				}
			})
		}
	}
};
function showModal(title, row) {
	row = row || {
		id : '',
	}; // default row value
	xcpage.$form[0].reset();//reset form
	xcpage.$modal.data('id', row.id);
	xcpage.$modal.find('.modal-title').text(title);
	for ( var name in row) {
		//set value to dialog for radio input
		if( xcpage.$modal.find('input[name="' + name + '"]').attr('type') == "radio"){
			xcpage.$modal.find('input[name="' + name + '"]').each(function() {
				if( $(this).val() == row[name]){
					$(this).attr("checked", true);
				}else{
					$(this).removeAttr("checked");
				}
			});
		}else{
			//set value to dialog for input
			xcpage.$modal.find('input[name="' + name + '"]').val(row[name]);
			//set value to dialog for select
			xcpage.$modal.find('select[name="' + name + '"]').val(row[name]);
		}
	}
	xcpage.$modal.modal('show');
}
function showAlert(title, type) {
	xcpage.$alert.attr('class', 'alert alert-' + type || 'success').html(
			'<i class="glyphicon glyphicon-check"></i> ' + title).show();
	setTimeout(function() {
		xcpage.$alert.hide();
	}, 3000);
}

function logout(row){
	$.ajax({
		url : '/user/logoff/' + row,
		type : 'get',
		data : row,
		success : function() {
			showAlert('注销成功!', 'success');
			xcpage.$table.bootstrapTable('refresh');
		},
		error : function() {
			showAlert('注销失败!', 'danger');
		}
	})
}

function custTypeformatter( value, row ){
	if(value == "1"){
		return "先生";
	}else if(value == "2"){
		return "女士";
	}else if(value == "3"){
		return "长子";
	}else if(value == "4"){
		return "次子";
	}else if(value == "5"){
		return "长女";
	}else if(value == "6"){
		return "次女";
	}else if(value == "7"){
		return "儿子";
	}else if(value == "8"){
		return "女儿";
	}else if(value == "9"){
		return "供应商";
	}
	return "未指定";
}


function customernameformatter(value, row){
	if( row['heartinfo'] == null){
		hearinfo = '未设定';
	}else{
		hearinfo = row['heartinfo'];
	}
	
	if(row['heartlevel'] == '5'){
		return '<i class="fa fa-heart" aria-hidden="true" style="color:green" data-toggle="tooltip" title="'+ hearinfo +'">&nbsp;&nbsp;</i>' + value;
	} else if(row['heartlevel'] == '4'){
		return '<i class="fa fa-heart" aria-hidden="true" style="color:orange" data-toggle="tooltip" title="'+ hearinfo +'">&nbsp;&nbsp;</i>' + value;
	}
	return '<i class="fa fa-heart" aria-hidden="true" style="color:red" data-toggle="tooltip" title="'+ hearinfo +'">&nbsp;&nbsp;</i>' + value;
}
