<%--
 * 数据分发管理-列表
 * 
 * @author Xwt
 * @date 2017-06-14
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据分发管理-列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			// 重置
			$("#btnReset").click(function(){
				$("#codeSetName").val("");
				$("#sendDateStart").val("");
				$("#sendDateEnd").val("");
				$("#taskCompleteDateStart").val("");
				$("#taskCompleteDateEnd").val("");
				$(".select2-container").find(".select2-chosen").text($("#destSysId").find("option:first").text());
				$("#destSysId").find("option:first").attr("selected","selected");
				$("#sendStatus").find("option:first").attr("selected","selected");
				$("#taskStatus").find("option:first").attr("selected","selected");
				return false;
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
            var mes = "${message}";
            if (mes.length > 0) {
                layer.msg(mes);
            }
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
		<%--<li class="active"><a href="${ctx}/businessdatamrg/sendingAudit/">数据分发列表</a></li>--%>
	<%--</ul>--%>
	<form:form id="searchForm" modelAttribute="sendingCodeSet" action="${ctx}/businessdatamrg/sendingAudit/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<div style="text-align:right">
		<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
	</div>
	<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>发送时间：</label>
				<input id="sendDateStart" name="sendDateStart" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${sendingCodeSet.sendDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>至
				<input id="sendDateEnd" name="sendDateEnd" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${sendingCodeSet.sendDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
			</li>
			<li><label>发送状态：</label>
				<form:select path="sendStatus" class="input-small required">
					<form:option value="" label="--请选择--"/>
					<form:option value="0" label="未发送"/>
					<form:option value="1" label="已发送"/>
					<form:option value="2" label="发送失败"/>
				</form:select>
			</li>
			<%--<li><label>目标业务系统：</label>--%>
				<%--<select id="destSysId" name="destSysId" class="input-medium">--%>
					<%--<option value="">--请选择--</option>--%>
					<%--<c:forEach items="${fns:getAllSystem()}" var="system">--%>
						<%--<option value="${system.id }" ${destSysId == system.id ? 'selected' : ''}>${system.systemName }</option>--%>
					<%--</c:forEach>--%>
				<%--</select>--%>
			<%--</li>--%>
			<li><label>代码集目录：</label><form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<%--<li class="clearfix"></li>--%>
			<li><label>任务处理时间：</label>
				<input id="taskCompleteDateStart" name="taskCompleteDateStart" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						value="<fmt:formatDate value="${sendingCodeSet.taskCompleteDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>至
				<input id="taskCompleteDateEnd" name="taskCompleteDateEnd" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${sendingCodeSet.taskCompleteDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
			</li>
			<%--<li><label>任务状态：</label>--%>
				<%--<form:select path="taskStatus" class="input-small">--%>
					<%--<form:option value="" label="--请选择--"/>--%>
					<%--<form:option value="0" label="待处理"/>--%>
					<%--<form:option value="1" label="已处理"/>--%>
				<%--</form:select>--%>
			<%--</li>--%>
			<li><label>目标业务系统：</label>
				<form:select path="destSysId" class="input-medium">
					<form:option value="">--请选择--</form:option>
					<form:options items="${fns:getAllSystem()}" itemLabel="systemName" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>
			<li class="clearfix"></li>
		</ul>
	</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>代码集目录</th>
				<th>目标业务系统</th>
				<th>所属区域</th>
				<th>发送状态</th>
				<th>发送时间</th>
				<%--<th>操作类型</th>--%>
				<%--<th>任务处理人id</th>--%>
				<%--<th>任务状态</th>--%>
				<th>任务开始时间</th>
				<th>任务处理时间</th>
				<%--<shiro:hasPermission name="businessdatamrg:sendingAudit:edit">--%>
					<th>操作</th>
				<%--</shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sendingCodeSet">
			<tr>
				<td>${sendingCodeSet.codeSetName}</td>
				<td>${sendingCodeSet.destSysName}</td>
				<td>${sendingCodeSet.areaName}</td>
				<td>
					<%--${sendingCodeSet.sendStatus eq 2 ? '发送失败' : '发送成功'}--%>
					<c:choose>
						<c:when test="${sendingCodeSet.sendStatus == 0}">未发送</c:when>
						<c:when test="${sendingCodeSet.sendStatus == 1}">已发送</c:when>
						<c:when test="${sendingCodeSet.sendStatus == 2}">发送失败</c:when>
					</c:choose>
				</td>
				<td><fmt:formatDate value="${sendingCodeSet.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<%--<td>--%>
					<%--<c:choose>--%>
						<%--<c:when test="${sendingCodeSet.operation == 0}">新增</c:when>--%>
						<%--<c:when test="${sendingCodeSet.operation == 1}">修改</c:when>--%>
						<%--<c:when test="${sendingCodeSet.operation == 2}">撤销</c:when>--%>
					<%--</c:choose>--%>
				<%--</td>--%>
				<%--<td>${sendingCodeSet.taskHandlerId}</td>--%>
				<%--<td>--%>
					<%--&lt;%&ndash;${sendingCodeSet.taskStatus eq 0 ? '待处理':'已处理'}&ndash;%&gt;--%>
					<%--<c:choose>--%>
						<%--<c:when test="${sendingCodeSet.taskStatus == 0}">待处理</c:when>--%>
						<%--<c:when test="${sendingCodeSet.taskStatus == 1}">已处理</c:when>--%>
						<%--<c:otherwise></c:otherwise>--%>
					<%--</c:choose>--%>
				<%--</td>--%>
				<td><fmt:formatDate value="${sendingCodeSet.taskCompleteDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${sendingCodeSet.taskCompleteDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<c:if test="${empty sendingCodeSet.taskHandlerId}">
						<c:if test="${sendingCodeSet.sendStatus == 1}">
							<shiro:hasPermission name="businessdatamrg:sendingAudit:view">
								<a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}&taskHandlerId=${sendingCodeSet.taskHandlerId }&taskStatus=${sendingCodeSet.taskStatus }">
									查看
								</a>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${sendingCodeSet.sendStatus != 1}">
							<shiro:hasPermission name="businessdatamrg:sendingAudit:issued">
								<a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}&taskHandlerId=${sendingCodeSet.taskHandlerId }&taskStatus=${sendingCodeSet.taskStatus }">
									处理
								</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="businessdatamrg:sendingAudit:view">
								<shiro:lacksPermission name="businessdatamrg:sendingAudit:issued">
									<a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}&taskHandlerId=${sendingCodeSet.taskHandlerId }&taskStatus=${sendingCodeSet.taskStatus }">
										查看
									</a>
								</shiro:lacksPermission>
							</shiro:hasPermission>
						</c:if>
					</c:if>
					<c:if test="${!empty sendingCodeSet.taskHandlerId}">
						<c:if test="${sendingCodeSet.taskHandlerId == fns:getUser().id}">
							<a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}&taskHandlerId=${sendingCodeSet.taskHandlerId }&taskStatus=${sendingCodeSet.taskStatus }">
								${sendingCodeSet.sendStatus == 1 ? '查看' : '处理'}
							</a>
						</c:if>
						<c:if test="${sendingCodeSet.taskHandlerId != fns:getUser().id}">
							<shiro:hasPermission name="businessdatamrg:sendingAudit:view">
								<a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}&taskHandlerId=${sendingCodeSet.taskHandlerId }&taskStatus=${sendingCodeSet.taskStatus }">
									查看
								</a>
							</shiro:hasPermission>
						</c:if>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>