<table border="1">
	<tr>
		<td>bean名字</td>
		<td>方法</td>
		<td>cron表达式</td>
		<td>描述</td>
		<td>状态</td>
		<td>最后修改时间</td>
		<td>操作</td>
	</tr>
	{{~it.scheduledTaskDefinitions:task}}
		<tr>
			<td>{{=task.beanName}}</td>
			<td>{{=task.methodName}}</td>
			<td>{{=task.cronExpression}}</td>
			<td>{{=task.description}}</td>
			<td>
				{{? task.status == 'RUNNING'}}运行中{{?}}
				{{? task.status == 'STOPPED'}}已停止{{?}}
			</td>
			<td>{{=task.lastModified}}</td>
			<td>
				<button type="button" role="update" data-id="{{=task.id}}">修改</button>
				<button type="button" role="delete" data-id="{{=task.id}}">删除</button>
				<button type="button" role="run" data-id="{{=task.id}}" {{? task.status == 'RUNNING'}}disabled="disabled"{{?}}>启动</button>
				<button type="button" role="stop" data-id="{{=task.id}}" {{? task.status == 'STOPPED'}}disabled="disabled"{{?}}>停止</button>
			</td>
		</tr>
	{{~}}
</table>