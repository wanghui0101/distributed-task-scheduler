<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="../commons/taglibs.jsp" %>
<html>
	<head>
		<c:import url="../commons/header.jsp" />
		<script type="text/javascript">
			var taskId = "${id}";
		</script>
	</head>
	<body>
		<c:import url="../commons/navigator-bar.jsp" />
		<div id="content"></div>
	</body>
	<script type="text/javascript" src="${ctx}/static/app/js/console-update.js"></script>
</html>
