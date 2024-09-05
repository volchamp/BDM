<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>核查代码集目录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            //重置
            $("#btnReset").click(function() {
                $("#codeSetName").val("");
                $("#beginCheckDate").val("");
                $("#endCheckDate").val("");
                var sysCodeFirstOpt = $("#systemCode").find("option:first");
                $(".select2-container").find(".select2-chosen").text(sysCodeFirstOpt.text());	//所有下拉框的文本内容置为业务系统下拉框第一个选项的文本内容
                sysCodeFirstOpt.attr("selected","selected");	//选中下拉框的第一个选项
                $("#handleStatus").find("option:first").attr("selected","selected");
                $("#checkResult").find("option:first").attr("selected","selected");
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
            //即时核查
            $("#btnInstantVal").click(function() {
                layer.confirm('请问您确定要执行即时核查操作吗？', {
                    btn: ['确定','关闭'] //按钮
                }, function(){
                    var searchFormElem = $("#searchForm");
                    searchFormElem.attr("action", "${ctx}/datacheckingmrg/validationCodeSet/instantVal");
                    layer.msg("正在核查，请耐心等待（提示：您还可以稍后手动刷新核查结果列表）...", {time: 8000});
                    searchFormElem.submit();
                });
            });
            <%--var mes = "${message}";--%>
            <%--if (mes.length > 0) {--%>
                <%--layer.msg(mes);--%>
            <%--}--%>
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li class="active"><a href="${ctx}/datacheckingmrg/validationCodeSet/">核查代码集目录列表</a></li>--%>
		<%--&lt;%&ndash;<shiro:hasPermission name="datacheckingmrg:validationCodeSet:edit">&ndash;%&gt;--%>
			<%--&lt;%&ndash;<li><a href="${ctx}/datacheckingmrg/validationCodeSet/form">核查代码集目录添加</a></li>&ndash;%&gt;--%>
		<%--&lt;%&ndash;</shiro:hasPermission>&ndash;%&gt;--%>
	<%--</ul>--%>
	<form:form id="searchForm" modelAttribute="validationCodeSet" action="${ctx}/datacheckingmrg/validationCodeSet/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<div style="text-align:right">
		<input id="btnInstantVal" class="btn btn-primary" type="button" value="即时核查"/>
		<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
	</div>
	<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<%--<li><label>业务系统编码：</label>--%>
				<%--<form:input path="systemCode" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<li><label>业务系统：</label>
				<%--<form:input path="systemName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
				<form:select path="systemCode" class="input-medium required">
					<form:option value="" label="--请选择--"/>
					<%--<form:options items="${systemList}" itemValue="id" itemLabel="systemName" />--%>
					<form:options items="${systemList}" itemValue="systemCode" itemLabel="systemName" />
				</form:select>
			</li>
				<li><label>处理状态：</label>
						<%--<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
					<form:select path="handleStatus" class="input-small required">
						<form:option value="" label="--请选择--"/>
						<form:option value="0" label="未处理"/>
						<form:option value="1" label="已处理"/>
					</form:select>
				</li>
			<%--<li><label>代码集目录编码：</label>--%>
				<%--<form:input path="codeSetNo" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
				<li><label>核查时间：</label>
					<input id="beginCheckDate" name="beginCheckDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						   value="<fmt:formatDate value="${validationCodeSet.beginCheckDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:false});"/>至
					<input id="endCheckDate" name="endCheckDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						   value="<fmt:formatDate value="${validationCodeSet.endCheckDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 23:59:59',isShowClear:false});"/>
				</li>
				<%--<li class="clearfix"></li>--%>
				<li><label>核查结果：</label>
						<%--<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
					<form:select path="checkResult" class="input-small required">
						<form:option value="" label="--请选择--"/>
						<form:option value="0" label="匹配"/>
						<form:option value="1" label="不匹配"/>
					</form:select>
				</li>
				<li><label>目录名称：</label><form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<%--<li><label>--%>
					<%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
				<%--</label></li>--%>
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
                <th width="7%">代码集目录编码</th>
                <th>代码集目录名称</th>
				<th>业务系统</th>
				<th>所属区域</th>
				<th width="12%">核查时间</th>
				<th width="5%">核查结果</th>
				<th width="5%">处理状态</th>
				<th width="30%">描述</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="validationCodeSet">
			<tr>
				<%--<td>--%>
					<%--<a href="${ctx}/datacheckingmrg/validationCodeSet/form?id=${validationCodeSet.id}">--%>
						<%--${validationCodeSet.systemCode}--%>
					<%--</a>--%>
				<%--</td>--%>
				<td>
					${validationCodeSet.codeSetNo}
				</td>
				<td>
					${validationCodeSet.codeSetName}
				</td>
                <td>
                        ${validationCodeSet.systemName}
                        <%--${validationCodeSet.id}--%>
                </td>
                <td>
								${fn:length(validationCodeSet.areaName) == 0 ? '云南省' : validationCodeSet.areaName}
                </td>
				<td>
					<fmt:formatDate value="${validationCodeSet.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<%--<c:if test="${validationCodeSet.checkResult == 0}">匹配</c:if>--%>
					<%--<c:if test="${validationCodeSet.checkResult == 1}">不匹配</c:if>--%>
					${validationCodeSet.checkResult == 0 ? '匹配' : '不匹配'}
				</td>
				<td>
					<%--<c:if test="${validationCodeSet.handleStatus == 0}">未处理</c:if>--%>
					<%--<c:if test="${validationCodeSet.handleStatus == 1}">已处理</c:if>--%>
					${validationCodeSet.handleStatus == 0 ? '未处理' : '已处理'}
				</td>
				<td>
					${validationCodeSet.exceptionDesc}
				</td>
				<td>
					<shiro:hasPermission name="datacheckingmrg:validationCodeSet:view">
						<shiro:lacksPermission name="datacheckingmrg:validationCodeSet:edit">
							<a href="${ctx}/datacheckingmrg/validationCodeSet/form?id=${validationCodeSet.id}&taskHandlerId=${validationCodeSet.taskHandlerId}">
							<c:if test="${validationCodeSet.handleStatus==0}">
    							${validationCodeSet.taskHandlerId eq fns:getUser() ? '处理':'查看'}
    						</c:if>
    						<c:if test="${validationCodeSet.handleStatus==1}">查看</c:if>  
							</a>
						</shiro:lacksPermission>
					</shiro:hasPermission>
					<shiro:hasPermission name="datacheckingmrg:validationCodeSet:edit">
							<a href="${ctx}/datacheckingmrg/validationCodeSet/form?id=${validationCodeSet.id}&taskHandlerId=${validationCodeSet.taskHandlerId}">
							${validationCodeSet.handleStatus eq 0 ? '处理':'查看'}
							</a>
					</shiro:hasPermission>
					<%--<a href="${ctx}/datacheckingmrg/validationCodeSet/delete?id=${validationCodeSet.id}" --%>
					   <%--onclick="return confirmx('确认要删除该核查代码集目录吗？', this.href)">删除</a>--%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>