<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="../commons/taglibs.jsp" %>
<html>
	<head>
		<c:import url="../commons/header.jsp" />
	</head>
	<body>
		<c:import url="../commons/navigator-bar.jsp" />
		<div id="content"></div>
		<div>
			<button type="button" id="add">新增</button>
		</div>
		<div id="leader"></div>
	</body>
	<script type="text/javascript" src="${ctx}/static/app/js/console-index.js"></script>
</html>
