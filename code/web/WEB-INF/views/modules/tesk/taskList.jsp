<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集任务管理</title>
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

		/**
		 * 任务处理
		 * @param {String} 任务链接
		 */
		function handle(taskLink){

		}

		// 指派任务
		function assign(id,taskHandlerId,taskHandlerName){
//		    alertx("11");
			$("#assignUserId").val(id);
			$("#taskHandlerId").val(taskHandlerId);
			$("#taskHandlerName").val(taskHandlerName);
			$("#taskHandlerName").click();
		}

		// 选择用户回调函数
		function taskHandlerTreeselectCallBack(v, h, f){
			// 指派用户ID
			var id = $("#assignUserId").val();
			var taskHandlerId = $("#taskHandlerId").val();
			var url = ctx + "/tesk/task/assign";
			var obj = {"id": id,"taskHandlerId": taskHandlerId};
			// if (taskHandlerId.length == 0) {
             //    top.$.jBox.tip("本次没有指派");
             //    return;
			// }
			$.post(url, obj,function(data) {
				if (data) {
                    var admin = "${fns:getUser().admin}";
					top.$.jBox.tip("操作成功");
					//当前不是超级管理员，指派成功要刷新
					// if (admin == "false") {
                        setTimeout("refreshUnhandledList()", 500);
                    // }
				} else {
					top.$.jBox.tip("指派失败,请联系管理员！");
				}
			});
		}

        /**
		 * 刷新任务列表
         */
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
	<%--<ul class="nav nav-tabs">--%>
		<%--&lt;%&ndash;<li ><a href="${ctx}/tesk/task/test">我的首页</a></li>&ndash;%&gt;--%>
	<%--&lt;%&ndash;<li class="active"><a href="${ctx}/tesk/task/">代码集任务列表</a></li>&ndash;%&gt;--%>
		<%--&lt;%&ndash;<li class="active"><a href="${ctx}/tesk/task/?taskStatus=${page.list[0].taskStatus}">代码集任务列表</a></li>&ndash;%&gt;--%>
		<%--<li class="active"><a href="${ctx}/tesk/task/?taskStatus=${task.taskStatus}">代码集任务列表</a></li>--%>
		<%--&lt;%&ndash;<c:if test="${task.taskStatus == 0}">&ndash;%&gt;--%>
		<%--&lt;%&ndash;<li ><a href="${ctx}/bps/workFlow/toDoList?processType=basicDataAudit">数据审核任务列表</a></li>&ndash;%&gt;--%>
		<%--&lt;%&ndash;</c:if>&ndash;%&gt;--%>
	<%--</ul>--%>

	<%--<div class="form-group">--%>
		<%--<button type="button" class="btn btn-success btn-sm click-adv fr"><i class="glyphicon glyphicon-wrench" > </i>高级查询</button>--%>
	<%--</div>--%>
	<%--<div class="adv-search clearfix dn">--%>
		<%--<div class="arrow-up"><span><em></em></span></div>--%>

	<form:form id="searchForm" modelAttribute="task" action="${ctx}/tesk/task/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="assignUserId" type="hidden" value=""/>
		<form:hidden path="taskStatus" id="taskStatus"/>
		<%--<shiro:hasPermission name="tesk:task:assign">--%>
			<%-- <sys:treeselect id="taskHandler" name="taskHandlerId" value="" labelName="taskHandlerName" labelValue=""
					title="用户" url="/sys/office/treeData?type=3" cssStyle="" allowClear="true" notAllowSelectParent="true" hideBtn="true"/> --%>
		<%--</shiro:hasPermission>--%>
		<%-- <div class="control-group" id="taskUser" style="display:none;">
			<label class="control-label">处理人:</label>
			<div class="controls">
				 <sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div> --%>
		<div style="text-align:right">
			<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
		</div>
		<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>任务送达时间：</label>
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
			<li><label>业务系统：</label>
				<form:select path="systemName" class="input-medium">
					<form:option value="">--请选择--</form:option>
					<form:option value="MDM主数据管理平台">MDM主数据管理平台</form:option>
					<form:options items="${fns:getAllSystem()}" itemLabel="systemName" itemValue="systemName" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>代码集目录名称：</label>
				<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<c:if test="${task.taskStatus == 1}">
				<li><label>任务处理时间：</label>
					<input id="taskCompleteDateStart" name="taskCompleteDateStart" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						   value="<fmt:formatDate value="${task.taskCompleteDateStart}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>
					至
					<input id="taskCompleteDateEnd" name="taskCompleteDateEnd" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						   value="<fmt:formatDate value="${task.taskCompleteDateEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
				</li>
			</c:if>
			<li><label>任务类型：</label>
				<%--<form:input path="taskTypeId" htmlEscape="false" maxlength="18" class="input-medium"/>--%>
				<form:select path="taskTypeId" class="input-small">
					<form:option value="">--请选择--</form:option>
					<c:if test="${task.taskStatus == 0}">
						<form:option value="1">数据初审</form:option>
						<form:option value="2">数据复审</form:option>
						<form:option value="3">下发处理</form:option>
						<form:option value="4">核查处理</form:option>
					</c:if>
					<c:if test="${task.taskStatus == 1}">
						<form:options items="${fns:getDictList('task_type_id')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</c:if>
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

	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>
					任务送达时间
				</th>
				<c:if test="${task.taskStatus == 1}">
					<th>
						任务处理时间
					</th>
				</c:if>
				<th>
					业务系统名称
				</th>
				<th>
					归属区域
				</th>
				<th>
					代码集目录名称
				</th>
				<th>
					任务类型
				</th>
				<%--<th>--%>
					<%--taskHandlerId--%>
				<%--</th>--%>
				<%--<c:if test="${task.taskStatus == 0}">--%>
					<%--<shiro:hasPermission name="tesk:task:edit">--%>
						<th>操作</th>
					<%--</shiro:hasPermission>--%>
				<%--</c:if>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="task">
			<tr>
				<td>
					<fmt:formatDate value="${task.taskStartDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<c:if test="${task.taskStatus == 1}">
					<td>
						<fmt:formatDate value="${task.taskCompleteDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</c:if>
				<td>
					${task.systemName }
					<%--${task.id}--%>
				</td>
				<td>
					${fn:length(task.areaName) == 0 ? '云南省' :  task.areaName}
				</td>
				<td>
					${task.codeSetName }
				</td>
				<td>
					${task.taskType }
				</td>
				<%--<td>--%>
					<%--${task.taskHandlerId }--%>
				<%--</td>--%>
				<td>
					<c:if test="${task.taskStatus == 0}">
						<shiro:hasPermission name="tesk:task:edit">
							<a href="${ctx }${task.taskLink }&taskHandlerId=${task.taskHandlerId}&taskStatus=${task.taskStatus}&systemId=${task.systemId}&origin=taskList&handleStatus=${task.taskStatus}">处理</a>
							<%--<shiro:hasPermission name="tesk:task:assign">--%>
								<a href="javascript:;" onclick="assign('${task.id}','${task.taskHandlerId }','${task.taskHandlerName }');">指派</a>
							<%--</shiro:hasPermission>--%>
						</shiro:hasPermission>
						<shiro:hasPermission name="tesk:task:view">
							<shiro:lacksPermission name="tesk:task:edit">
								<%--<c:if test="${task.taskLink != null}">--%>
								<a href="${ctx }${task.taskLink }&taskHandlerId=${task.taskHandlerId}&taskStatus=${task.taskStatus}&origin=taskList" >查看</a>
								<%--</c:if>--%>
							</shiro:lacksPermission>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${task.taskStatus == 1}">
						<shiro:hasPermission name="tesk:task:view">
							<%--<c:if test="${task.taskLink != null}">--%>
							<a href="${ctx }${task.taskLink }&taskHandlerId=${task.taskHandlerId}&taskStatus=${task.taskStatus}&origin=taskList" >查看</a>
							<%--</c:if>--%>
						</shiro:hasPermission>
					</c:if>
					<%--<c:if test="${task.procInstId != null && task.procInstId != ''}">--%>
						<a href="javascript:;" onclick="layerPage2('${ctx}/tesk/task/taskHisList?codeSetName=${task.codeSetName}&systemName=${task.systemName}',
							'“${task.codeSetName }”流程跟踪', '1000px', '400px');" style="cursor:pointer;">流程跟踪</a>
					<%--<button onclick="layerPage2('${ctx}/tesk/task/taskHisList?codeSetName=${task.codeSetName}',--%>
							<%--'“${task.codeSetName }”流程跟踪', '1000px', '400px');">流程跟踪2</button>--%>
					<%--</c:if>--%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>