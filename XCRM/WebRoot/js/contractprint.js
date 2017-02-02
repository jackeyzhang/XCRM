$(function() {
	$("#print").click(function() {
		$('.printarea').printArea({
			mode : 'popup',
			popClose : 'false',
			extraCss : '',
			retainAttr : '',
			extraHead : ''
		});
	});
});