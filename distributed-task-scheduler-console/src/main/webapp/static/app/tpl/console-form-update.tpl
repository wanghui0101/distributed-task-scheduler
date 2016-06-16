<form method="POST">
	<table>
		<input type="hidden" name="id" value="{{=it.id}}">
		<tr>
			<td>bean名字</td>
			<td>
				<input type="text" name="beanName" value="{{=it.beanName}}">
			</td>
		</tr>
		<tr>
			<td>方法</td>
			<td>
				<input type="text" name="methodName" value="{{=it.methodName}}">
			</td>
		</tr>
		<tr>
			<td>cron表达式</td>
			<td>
				<input type="text" name="cronExpression" value="{{=it.cronExpression}}">
			</td>
		</tr>
		<tr>
			<td>描述</td>
			<td>
				<input type="text" name="description" value="{{=it.description}}">
			</td>
		</tr>
	</table>
	<button type="button" id="save">保存</button>
</form>