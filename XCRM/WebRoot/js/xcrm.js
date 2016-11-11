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
		if($('#file-Portrait')){
//			createFileinput(defaultFileinput());
		}
		showModal($(this).text());
	});
	xcpage.$modal.find('.submit').click(
			function(){
				var imgs = '';
				$('.file-thumbnail-footer').each(function(){
					if($(this).find('.progress-bar-success').html().trim()=='100%'){
						imgs += $(this).find('.file-footer-caption').attr('title')+',';
					}
				});
				$('#imgs').val(imgs);
			}
	);
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
					if($(this).val() instanceof Array){
						row[$(this).attr('name')] = $(this).val().join(',');
					}else{
						row[$(this).attr('name')] = $(this).val();
					}
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
						if(xcpage.fileinput){
							$(xcpage.fileinput).fileinput('clear');
						}
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
		if($('#changepic')){
			$('#changepic').css('display','block');
			$('#changepic').click(function(){
				createEditfileinput([
				               'http://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/FullMoon2010.jpg/631px-FullMoon2010.jpg',
				               'http://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Earth_Eastern_Hemisphere.jpg/600px-Earth_Eastern_Hemisphere.jpg'
				           ],[
				              {caption: "Moon.jpg", size: 930321, width: "120px", key: 1},
				              {caption: "Earth.jpg", size: 1218822, width: "120px", key: 2}
				          ]);
				$('.file-input').css('display','block');
			});
			$('.file-input').css('display','none');
		}
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
	},
	'click .attributes' : function(e, value, row) {
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
		});
	}
};
function showModal(title, row) {
	row = row || {
		id : '',
	}; // default row value
	if(xcpage.$form[0]){
		xcpage.$form[0].reset();//reset form
	}
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
			// set value for dialog for multiple select
			if(xcpage.$modal.find('select[name="' + name + '"]').attr('multiple') == "multiple"){
				if(row[name]){
					xcpage.$modal.find('select[name="' + name + '"]').multiselect('select', row[name].split(","));
				}else{
					xcpage.$modal.find('select[name="' + name + '"]').multiselect('deselectAll',false);
					xcpage.$modal.find('select[name="' + name + '"]').multiselect('updateButtonText');
				}
			}
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
	if (confirm('确定注销该用户?')) {
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
}

function active(row){
	if (confirm('确定激活该用户?')) {
		$.ajax({
			url : '/user/active/' + row,
			type : 'get',
			data : row,
			success : function() {
				showAlert('激活成功!', 'success');
				xcpage.$table.bootstrapTable('refresh');
			},
			error : function() {
				showAlert('激活失败!', 'danger');
			}
		})
	}
}
