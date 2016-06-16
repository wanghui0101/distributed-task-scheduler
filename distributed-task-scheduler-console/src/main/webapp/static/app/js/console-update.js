$(function() {
	
	$.get($$ctx + "/static/app/tpl/console-form-update.tpl", function(tpl) {
		var tplFn = doT.template(tpl);
		$.get($$ctx + "/" + taskId, function(task) {
			var r = tplFn(task);
			$("#content").html(r);
		});
	});
	
	$('body').on("click", "#save", function() {
		$("form").attr("action", $$ctx + "/update").ajaxSubmit(function(r) {
			if (r.success) {
				alert('修改成功');
				window.location.href = $$ctx;
			} else {
				alert(r.msg);
			}
		});
	});
	
});