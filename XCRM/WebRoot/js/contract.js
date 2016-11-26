$(function() {
	$('.create.btn.btn-primary').click(function(){
        $.ajax({
            type: "get",
            url: "/contract/preadd",
            success: function (data, status) {
                if (status == "success") {
                	$('.bootstrap-table').html(data);
                }
            }
        });
	});
});


