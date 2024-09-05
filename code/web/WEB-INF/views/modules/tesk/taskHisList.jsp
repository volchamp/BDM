<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程跟踪查看</title>
	<meta name="decorator" content="default"/>
	<%--<jsp:include page="../../../../securityJsp/nfrrinc.jsp" />--%>
	<%--<%@include file="/securityJsp/nfrrinc.jsp"%>--%>
	<script type="text/javascript">
		$(document).ready(function() {
            //重置
            $("#btnReset").click(function() {
                $("#codeSetName").val("");
                $("#taskStartDateStart").val("");
                $("#taskStartDateEnd").val("");
                $("#taskCompleteDateStart").val("");
                $("#taskCompleteDateEnd").val("");
                var sysNameFirstOpt = $("#systemName").find("option:first");
                $(".select2-container").find(".select2-chosen").text(sysNameFirstOpt.text());	//所有下拉框的文本内容置为业务系统下拉框第一个选项的文本内容
                sysNameFirstOpt.attr("selected","selected");	//选中下拉框的第一个选项
                $("#taskTypeId").find("option:first").attr("selected","selected");
            });

            //高级查询按钮
            $("#btnAdvanced").click(function() {
                var seaDivElem = $("#searchDiv");
                if (seaDivElem.is(":hidden")) {
                    seaDivElem.show();    //如果元素为隐藏,则将它显现
                } else {
                    seaDivElem.hide();     //如果元素为显现,则将其隐藏
                }
            });
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function refreshUnhandledList() {
            <%--var seaFormElem = $("#searchForm");--%>
            <%--seaFormElem.attr("action", "${ctx}/tesk/task/?taskStatus=0");--%>
            <%--seaFormElem.submit();--%>
			window.location.reload();
        }
		
	</script>
	<style type="text/css">
		.form-search .ul-form li label{ width:120px;}
	</style>
</head>
<body>
	<!-- 首页2 -->

	<form:form id="searchForm" modelAttribute="task" action="${ctx}/tesk/task/taskHisList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="codeSetName" name="codeSetName" type="hidden" value="${task.codeSetName}"/>
		<input id="systemName" name="systemName" type="hidden" value="${task.systemName}"/>
		<div style="text-align:right">
			<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
		</div>
		<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>环节开始时间：</label>
				<input id="taskStartDateStart" name="taskStartDateStart" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${task.taskStartDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>
				至
				<input id="taskStartDateEnd" name="taskStartDateEnd" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${task.taskStartDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
			</li>
			<%--<li><label>业务系统名称：</label>--%>
				<%--<form:input path="systemName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<li><label>环节处理时间：</label>
				<input id="taskCompleteDateStart" name="taskCompleteDateStart" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${task.taskCompleteDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>
				至
				<input id="taskCompleteDateEnd" name="taskCompleteDateEnd" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${task.taskCompleteDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
			</li>
			<%--<li><label>业务系统：</label>--%>
				<%--<form:select path="systemName" class="input-medium">--%>
					<%--<form:option value="">--请选择--</form:option>--%>
					<%--<form:option value="MDM主数据管理平台">MDM主数据管理平台</form:option>--%>
					<%--<form:options items="${fns:getAllSystem()}" itemLabel="systemName" itemValue="systemName" htmlEscape="false"/>--%>
				<%--</form:select>--%>
			<%--</li>--%>
			<li><label>环节：</label>
				<%--<form:input path="taskTypeId" htmlEscape="false" maxlength="18" class="input-medium"/>--%>
				<form:select path="taskTypeId" class="input-small">
					<form:option value="">--请选择--</form:option>
						<form:options items="${fns:getDictList('task_type_id')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>
			<sys:treeselect id="taskHandler" name="taskHandlerId" value="" labelName="taskHandlerName" labelValue=""
					title="用户" url="/sys/office/treeData?type=3" cssStyle="display:none;" notAllowSelectParent="true" hideBtn="true"
					 allowClear="true" />
			<li class="clearfix"></li>
		</ul>
		</div>
	</form:form>

	<%--</div>--%>
	<legend>
		代码集信息
	</legend>
	<ul class="ul-form">
		<li><label>代码集目录名称：</label>
			${task.codeSetName}
		</li>
		<%--<li><label>代码集目录编码：</label>--%>
			<%--${task.codeSetName}--%>
		<%--</li>--%>
		<%--<li class="clearfix"></li>--%>
	</ul>

	<sys:message content="${message}"/>
	<fieldset>
		<legend>环节记录</legend>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>
					环节
				</th>
				<th>
					环节开始时间
				</th>
				<th>
					环节处理时间
				</th>
				<th>
					处理人
				</th>
				<th>
					处理状态
				</th>
				<th>
					业务系统名称
				</th>
				<th>
					归属区域
				</th>
				<%--<th>操作</th>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="task">
			<tr>
				<td>
					${fns:getDictLabel(task.taskTypeId, 'task_type_id', '')}
				</td>
				<td>
					<fmt:formatDate value="${task.taskStartDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${task.taskCompleteDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${task.taskHandlerName }
				</td>
				<td>
					${task.taskStatus == 0 ? '待处理' : '已处理'}
				</td>
				<td>
					${task.systemName }
				</td>
				<td>
					${task.areaName }
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</fieldset>
</body>
</html>