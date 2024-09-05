<%--
 * 数据审核管理-列表
 * 
 * @author Xwt
 * @date 2017-06-07
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据审核管理-列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").click(function(){
				$("#codeSetName").val("");
				$("#taskCompleteDateStart").val("");
				$("#taskCompleteDateEnd").val("");
				$(".select2-container").find(".select2-chosen").text($("#taskStatus").find("option:first").text());
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
		<%--<li class="active"><a href="${ctx}/businessdatamrg/pendingAudit/">代码集审核列表</a></li>--%>
	<%--</ul>--%>
	<form:form id="searchForm" modelAttribute="pendingCodeSet" action="${ctx}/businessdatamrg/pendingAudit/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<div style="text-align:right">
		<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
	</div>
	<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>代码集目录：</label><form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>任务状态：</label>
				<form:select path="taskStatus" id="taskStatus" class="input-medium">  
			            <form:option value="" label="--请选择--"/>
			            <form:option value="0" label="待处理"/> 
			            <form:option value="1" label="已处理"/>       
		        </form:select>
			</li>
			<li><label>任务处理时间：</label><input id="taskCompleteDateStart" name="taskCompleteDateStart" type="text" readonly="readonly" maxlength="20" class="input-mini Wdate"
				value="<fmt:formatDate value="${pendingCodeSet.taskCompleteDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>
				-
				<input id="taskCompleteDateEnd" name="taskCompleteDateEnd" type="text" readonly="readonly" maxlength="20" class="input-mini Wdate"
				value="<fmt:formatDate value="${pendingCodeSet.taskCompleteDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重 置"/></li>
			<li class="clearfix"></li>
		</ul>
	</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>代码集目录</th>
				<th>操作类型</th>
				<th>任务状态</th>
				<th>任务开始时间</th>
				<th>任务处理时间</th>
				<th>业务系统名称</th>
				<th>所属区域</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="pendingCodeSet">
			<tr>
				<td>
					${pendingCodeSet.codeSetName}
				</td>
				<td>
					${pendingCodeSet.operation eq 0 ? '新增':pendingCodeSet.operation eq 1 ? '修改':'撤消'}
				</td>
				<td>
					${pendingCodeSet.taskStatus eq 0 ? '待处理':'已处理'}
				</td>
				<td>
					<fmt:formatDate value="${pendingCodeSet.taskCompleteDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${pendingCodeSet.taskCompleteDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
						${pendingCodeSet.systemName}
				</td>
				<td>
					${fn:length(pendingCodeSet.areaName) == 0 ? '云南省' :  pendingCodeSet.areaName}
				</td>
				<td>
					<shiro:hasPermission name="businessdatamrg:pendingAudit:review">
    					<a href="${ctx}/businessdatamrg/pendingAudit/form?id=${pendingCodeSet.id}&taskHandlerId=${pendingCodeSet.taskHandlerId }&taskStatus=${pendingCodeSet.taskStatus }">${pendingCodeSet.taskStatus eq 0 ? '处理':'查看'}</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="businessdatamrg:pendingAudit:view">
    					<shiro:lacksPermission name="businessdatamrg:pendingAudit:review">
    					<a href="${ctx}/businessdatamrg/pendingAudit/form?id=${pendingCodeSet.id}&taskHandlerId=${pendingCodeSet.taskHandlerId }&taskStatus=${pendingCodeSet.taskStatus }">
    					<c:if test="${pendingCodeSet.taskStatus==0}">
    						${pendingCodeSet.taskHandlerId eq fns:getUser().id ? '处理':'查看'}
    					</c:if><c:if test="${pendingCodeSet.taskStatus==1}">查看</c:if>
    					</a>
    					</shiro:lacksPermission>
    				</shiro:hasPermission>
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>