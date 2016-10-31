$(function() {
	isvalid = true;
	xcpage = {
		$modal : $('#modal').modal({
			show : false
		}),
		$alert : $('.alert').hide(),
		$table : $('#table').bootstrapTable({
			url : API_URL
		}),
		
		$form : $('#modal-form')
	}
	

	$('.create').click(function() {
		showModal($(this).text());
	});
	xcpage.$modal.find('.submit').click(																									
			function() {
				var row = {};
				xcpage.$form.validator('validate');//fire validate
				if(xcpage.$form.validator().data('bs.validator').hasErrors()){
					return;
				}
				xcpage.$modal.find('input[name]').each(function() {
					row[$(this).attr('name')] = $(this).val();
				});
				
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
			'<a class="remove" href="javascript:" title="删除">删除</a>', ]
			.join('   ');
}
// update and delete events
window.actionEvents = {
	'click .update' : function(e, value, row) {
		showModal($(this).attr('title'), row);
	},
	'click .remove' : function(e, value, row) {
		if (confirm('Are you sure to delete this item?')) {
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
		nickname : '',
		department : 'yes',
		title : 'CEO',
		description : ''
	}; // default row value
	xcpage.$modal.data('id', row.id);
	xcpage.$modal.find('.modal-title').text(title);
	for ( var name in row) {
		xcpage.$modal.find('input[name="' + name + '"]').val(row[name]);
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