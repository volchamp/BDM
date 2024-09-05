<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口收发日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            //重置
            $("#btnReset").click(function() {
                // layer.msg("777");
                $("#codeSetName").val("");
                $("#startDateStart").val("");
                $("#startDateEnd").val("");
                var srcSysNameFirstOpt = $("#sourceSysName").find("option:first");
                $(".select2-container").find(".select2-chosen").text(srcSysNameFirstOpt.text());	//所有下拉框的文本内容置为业务系统下拉框第一个选项的文本内容
                srcSysNameFirstOpt.attr("selected","selected");	//选中下拉框的第一个选项
                $("#destSysName").find("option:first").attr("selected","selected");
                $("#recordType").find("option:first").attr("selected","selected");
                $("#status").find("option:first").attr("selected","selected");
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
	</script>
	<style type="text/css">
	.form-search .ul-form li label{ width:120px;}
	</style>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li class="active"><a href="${ctx}/inoutlog/logInout/">接口收发日志列表</a></li>--%>
		<%--&lt;%&ndash; <shiro:hasPermission name="inoutlog:logInout:edit"><li><a href="${ctx}/inoutlog/logInout/form">接口收发日志添加</a></li></shiro:hasPermission> &ndash;%&gt;--%>
	<%--</ul>--%>
	<form:form id="searchForm" modelAttribute="logInout" action="${ctx}/inoutlog/logInout/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<div style="text-align:right">
		<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
	</div>
	<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<%--<li><label>源业务系统名称：</label>--%>
				<%--<form:input path="sourceSysName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>源业务系统：</label>--%>
				<%--<select id="sourceSysName" name="sourceSysName" class="input-medium">--%>
					<%--<option value="">--请选择--</option>--%>
					<%--<option value="MDM主数据平台" ${sourceSysName == 'MDM主数据平台' ? 'selected' : ''}>MDM主数据平台</option>--%>
					<%--<c:forEach items="${fns:getAllSystem()}" var="system">--%>
						<%--<option value="${system.systemName }" ${sourceSysName == system.systemName ? 'selected' : ''}>${system.systemName }</option>--%>
					<%--</c:forEach>--%>
				<%--</select>--%>
			<%--</li>--%>
			<li><label>源业务系统：</label>
				<form:select path="sourceSysName" class="input-medium">
					<form:option value="">--请选择--</form:option>
					<form:option value="基础数据管控平台">基础数据管控平台</form:option>
					<form:options items="${fns:getAllSystem()}" itemLabel="systemName" itemValue="systemName" htmlEscape="false"/>
				</form:select>
			</li>
			<%--<li><label>目的业务系统名称：</label>--%>
				<%--<form:input path="destSysName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>目的业务系统：</label>--%>
				<%--<select id="destSysName" name="destSysName" class="input-medium">--%>
					<%--<option value="">--请选择--</option>--%>
					<%--<option value="MDM主数据平台" ${destSysName == 'MDM主数据平台' ? 'selected' : ''}>MDM主数据平台</option>--%>
					<%--<c:forEach items="${fns:getAllSystem()}" var="system">--%>
						<%--<option value="${system.systemName }" ${destSysName == system.systemName ? 'selected' : ''}>${system.systemName }</option>--%>
					<%--</c:forEach>--%>
				<%--</select>--%>
			<%--</li>--%>
			<li><label>目的业务系统：</label>
				<form:select path="destSysName" class="input-medium">
					<form:option value="">--请选择--</form:option>
					<form:option value="基础数据管控平台">基础数据管控平台</form:option>
					<form:options items="${fns:getAllSystem()}" itemLabel="systemName" itemValue="systemName" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>开始操作时间：</label>
				<input id="startDateStart" name="startDateStart" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${logInout.startDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>
				至
				<input id="startDateEnd" name="startDateEnd" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${logInout.startDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
			</li>
			<li><label>记录类型：</label>
				<form:select path="recordType" class="input-medium">
					<form:option value="">--请选择--</form:option>
					<form:options items="${fns:getDictList('receive_send')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<%--<li><label>&emsp;&emsp;&emsp;&emsp;&emsp;状态：</label>--%>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="">--请选择--</form:option>
					<form:options items="${fns:getDictList('success_fail')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>代码集名称：</label>
				<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>
			<li class="clearfix"></li>
		</ul>
	</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>代码集名称</th>
				<th>源业务系统名称</th>
				<th>目的业务系统名称</th>
				<th>所属区域</th>
				<th>记录数量</th>
				<th>记录类型</th>
				<th>状态</th>
				<th>开始操作时间</th>
				<th>结束操作时间</th>
				<th>失败原因</th>
				<%--<shiro:hasPermission name="inoutlog:logInout:edit">--%>
					<th>操作</th>
				<%--</shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="logInout">
			<tr>
				<td>
					<a href="${ctx}/inoutlog/logInout/form?id=${logInout.id}" title="查看本日志">
						${logInout.codeSetName}
					</a>
				</td>
				<td>
					${logInout.sourceSysName}
				</td>
				<td>
					${logInout.destSysName}
				</td>
				<td>
					${logInout.areaName}
				</td>
				<td>
					${logInout.recordAmount}
				</td>
				<td>
					${logInout.recordType == 0 ? '数据核查' : '数据分发'}
				</td>
				<td>
						${logInout.status == 1 ? '成功' : '失败'}
				</td>
				<td>
					<fmt:formatDate value="${logInout.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${logInout.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${logInout.failReason}
				</td>
				<%--<shiro:hasPermission name="inoutlog:logInout:edit">--%>
					<td>
						<a href="${ctx}/inoutlog/logInout/form?id=${logInout.id}">查看</a>
						<%--<a href="${ctx}/inoutlog/logInout/delete?id=${logInout.id}" onclick="return confirmx('确认要删除该接口收发日志吗？', this.href)">删除</a>--%>
					</td>
				<%--</shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>