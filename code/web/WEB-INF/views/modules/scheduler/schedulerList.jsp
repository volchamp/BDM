<%--
 * 定时任务管理-列表
 * 
 * @author Xwt
 * @date 2017-06-22
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定时任务列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").click(function(){
				$("#systemName").val("");
				$("#serviceAddr").val("");
				return false;
			});
		});
		
		/**
		 * 功能：启动定时器
		 */
		function changeStart(){
			loading('正在操作，请稍等...');
			$("#searchForm").submit();
		}

	</script>
	<style type="text/css">
		.form-search .ul-form li label{ width:100px;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/scheduler/">定时任务列表</a></li>
		<shiro:hasPermission name="sys:scheduler:edit"><li><a href="${ctx}/sys/scheduler/addJob1">定时任务添加</a></li></shiro:hasPermission>
	</ul>
	<form id="searchForm" action="${ctx}/sys/scheduler/changeStart" method="post" class="breadcrumb form-search">
		<input type="hidden" value="${isStandby}" id="isStandby" name="isStandby"/>
		<ul class="ul-form">
			<c:choose>
				<c:when test="${isStandby==true}">
					<li class="btns"><input id="btnStart" class="btn btn-primary" type="button" value="启动定时器" onclick="javascript:changeStart()"/>
				</c:when>
				<c:otherwise>
					<li class="btns"><input id="btnStop" class="btn btn-primary" type="button" value="停止定时器" onclick="javascript:changeStart()"/>
				</c:otherwise>
			</c:choose>
			<li class="clearfix"></li>
		</ul>
	</form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
			    <th>任务名称</th>
			    <th>任务类</th>
			    <th>任务描述</th>
				<shiro:hasPermission name="sys:scheduler:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="scheduler" varStatus="status">
			<tr>
				<td>
					${status.index + 1}
				</td>
				<td>
					${scheduler.key.name }
				</td>
				<td>
					${scheduler.jobClass.name }
				</td>
				<td>
					${scheduler.description }
				</td>
				<shiro:hasPermission name="sys:scheduler:edit"><td>
					<a href="${ctx}/sys/scheduler/addJob3?jobName=${scheduler.key.name }">修改</a>
					<a href="${ctx}/sys/scheduler/delJob?jobName=${scheduler.key.name }" onclick="return confirmx('确认要删除该任务吗？', this.href)">删除</a>
    				<a href="${ctx}/sys/scheduler/addTrigger1?jobName=${scheduler.key.name }">添加计划</a>
    				<a href="${ctx}/sys/scheduler/executeJob?jobName=${scheduler.key.name }" onclick="loading('正在执行，请稍等...');">执行</a> 
    				<a href="${ctx}/sys/scheduler/getTriggersByJob?jobName=${scheduler.key.name }">计划列表</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>