<form method="POST">
	<table>
		<input type="hidden" name="id" />
		<tr>
			<td>bean名字</td>
			<td>
				<input type="text" name="beanName" />
			</td>
		</tr>
		<tr>
			<td>方法</td>
			<td>
				<input type="text" name="methodName" />
			</td>
		</tr>
		<tr>
			<td>cron表达式</td>
			<td>
				<input type="text" name="cronExpression" />
			</td>
		</tr>
		<tr>
			<td>描述</td>
			<td>
				<input type="text" name="description" />
			</td>
		</tr>
	</table>
	<button type="button" id="save">保存</button>
</form>