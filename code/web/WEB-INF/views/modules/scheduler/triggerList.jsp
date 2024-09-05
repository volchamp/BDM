<%--
 * 触发器-列表
 * 
 * @author Xwt
 * @date 2017-06-27
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计划列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
	<style type="text/css">
		.form-search .ul-form li label{ width:100px;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/scheduler/getTriggersByJob?jobName=${jobName}">计划列表</a></li>
		<shiro:hasPermission name="sys:scheduler:edit"><li><a href="${ctx}/sys/scheduler/addTrigger1?jobName=${jobName}">添加定时计划:${jobName}</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
			    <th>任务名称</th>
			    <th>计划名称</th>
			    <th>计划描述</th>
			    <th>状态</th>
				<shiro:hasPermission name="sys:scheduler:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="trig" varStatus="status">
			<tr>
				<td>
					${status.index + 1}
				</td>
				<td>
					${trig.jobKey.name }
				</td>
				<td>
					${trig.key.name }
				</td>
				<td>
					${trig.description }
				</td>
				<td>
					<c:choose>
						<c:when test="${mapState[trig.key.name] eq STATE_NORMAL}">
							<font color='green'><b>启用</b></font>
						</c:when>
						<c:when test="${mapState[trig.key.name] eq STATE_PAUSED}">
							<font color='red'><b>禁用</b></font>
						</c:when>
						<c:when test="${mapState[trig.key.name] eq STATE_ERROR}">
							执行出错
						</c:when>
						<c:when test="${mapState[trig.key.name] eq STATE_COMPLETE}">
							完成
						</c:when>
						<c:when test="${mapState[trig.key.name] eq STATE_BLOCKED}">
							正在执行
						</c:when>
						<c:when test="${mapState[trig.key.name] eq STATE_NONE}">
							未启动
						</c:when>
					</c:choose>
				</td>
				<shiro:hasPermission name="sys:scheduler:edit"><td>
					<c:if test="${mapState[trig.key.name] eq STATE_NORMAL || mapState[trig.key.name] eq STATE_PAUSED}">
    					<a href="${ctx}/sys/scheduler/toggleTriggerRun?trigName=${trig.key.name }&jobName=${trig.jobKey.name }" onclick="loading('正在操作，请稍等...');">${mapState[trig.key.name] eq STATE_NORMAL ? '禁用':'启用' }</a>
    					<a href="${ctx}/sys/scheduler/addTrigger3?trigName=${trig.key.name }&jobName=${trig.jobKey.name }">修改</a>
    					<a href="${ctx}/sys/scheduler/delTrigger?trigName=${trig.key.name }&jobName=${trig.jobKey.name }" onclick="return confirmx('确认要删除该计划吗？', this.href)">删除</a> 
    				</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>