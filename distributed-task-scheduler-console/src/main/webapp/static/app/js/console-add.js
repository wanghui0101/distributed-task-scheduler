$(function() {
	
	$.get($$ctx + "/static/app/tpl/console-form-add.tpl", function(tpl) {
		var tplFn = doT.template(tpl);
		var r = tplFn();
		$("#content").html(r);
	});
	
	$('body').on("click", "#save", function() {
		$("form").attr("action", $$ctx + "/add").ajaxSubmit(function(r) {
			if (r.success) {
				alert('新增成功');
				window.location.href = $$ctx;
			} else {
				alert(r.msg);
			}
		});
	});
	
});