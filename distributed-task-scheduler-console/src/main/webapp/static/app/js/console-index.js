$(function() {
	
	$.get($$ctx + "/static/app/tpl/console-table.tpl", function(tpl) {
		var tplFn = doT.template(tpl);
		$.post($$ctx + "/all", function(scheduledTaskDefinitions) {
			var r = tplFn({scheduledTaskDefinitions: scheduledTaskDefinitions});
			$("#content").html(r);
		});
	});
	
	$("#add").click(function() {
		window.location.href = $$ctx + "/add";
	});
	
	$('body').on("click", "button[role='update']", function(e) { // 修改
		var $btn = $(e.currentTarget);
		var id = $btn.data("id");
		window.location.href = $$ctx + "/update/" + id;
	});
	
	$('body').on("click", "button[role='delete']", function(e) { // 删除
		var $btn = $(e.currentTarget);
		var id = $btn.data("id");
		if (confirm("您确定要删除此定时任务吗？")) {
			$.post($$ctx + "/delete/" + id, function(r) {
				if (r.success) {
					alert('删除成功');
					window.location.href = $$ctx;
				} else {
					alert(r.msg);
				}
			});
		}
	});
	
	$('body').on("click", "button[role='run']", function(e) { // 启动
		var $btn = $(e.currentTarget);
		var id = $btn.data("id");
		$.post($$ctx + "/run/" + id, function(r) {
			if (r.success) {
				alert('启动成功');
				window.location.href = $$ctx;
			} else {
				alert(r.msg);
			}
		});
	});
	
	$('body').on("click", "button[role='stop']", function(e) { // 停止
		var $btn = $(e.currentTarget);
		var id = $btn.data("id");
		$.post($$ctx + "/stop/" + id, function(r) {
			if (r.success) {
				alert('停止成功');
				window.location.href = $$ctx;
			} else {
				alert(r.msg);
			}
		});
	});
	
	$.get($$ctx + "/nodes", function(r) {
		var html = "";
		if (r) {
			html = "当前定时任务所在主节点为：" + r[0] + "。所有节点为：" + r.join(", ");
		} else {
			html = "当前无运行的定时任务节点";
		}
		$("#nodes").html(html);
	});
	
});