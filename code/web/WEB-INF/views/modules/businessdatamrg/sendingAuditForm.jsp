<%--
 * 数据分发审核处理页
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
	<title>数据分发处理页</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			// 审核操作
			$(".audit").click(function(){
				var status = $(this).attr("status");
				var form = $("#inputForm");
				var processStatus = $("#processStatus");
				processStatus.val(status); // 设置审核状态
				if (processStatus.val() != '') {
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
			//重新发送操作
            $("#resend").click(function () {
                top.$.jBox.confirm("确认要进行发送操作吗？","系统提示",function(v,h,f){
                    if(v =="ok") {
                        var form = $("#inputForm");
                        form.attr("action","${ctx}/businessdatamrg/sendingAudit/resend");
                        form.submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });

		});
		
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/businessdatamrg/sendingAudit/">数据分发列表</a></li>--%>
		<%--&lt;%&ndash;<li class="active"><a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}">数据分发${sendingCodeSet.sendStatus == 1 ? '查看' : '处理'}</a></li>&ndash;%&gt;--%>
		<%--<li class="active"><a href="${ctx}/businessdatamrg/sendingAudit/form?id=${sendingCodeSet.id}">数据分发详情</a></li>--%>
	<%--</ul>--%>
	<form id="inputForm" action="${ctx}/businessdatamrg/pendingAudit/review" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${sendingCodeSet.id }"/>
		<input type="hidden" id="processStatus" name="processStatus" />
		<input type="hidden" id="changeForm" name="changeForm"/>
		<input type="hidden" name="origin" value="${origin}"/>
		<ul class="breadcrumb ul-form">
			<c:if test="${empty sendingCodeSet.taskHandlerId}">
				<c:if test="${sendingCodeSet.sendStatus != 1}">
					<shiro:hasPermission name="businessdatamrg:sendingAudit:issued">
						<li class="btns">
							<input id="resend" class="btn btn-primary audit" type="button" value="${sendingCodeSet.sendStatus == 0 ? '' : '重新'}发送"/>
						</li>
					</shiro:hasPermission>
				</c:if>
			</c:if>
			<c:if test="${!empty sendingCodeSet.taskHandlerId}">
				<c:if test="${sendingCodeSet.taskHandlerId == fns:getUser().id}">
					<c:if test="${sendingCodeSet.sendStatus != 1}">
						<li class="btns">
							<input id="resend" class="btn btn-primary audit" type="button" value="${sendingCodeSet.sendStatus == 0 ? '' : '重新'}发送"/>
						</li>
					</c:if>
				</c:if>
				<%--<c:if test="${sendingCodeSet.taskHandlerId != fns:getUser().id}">--%>
				<%--</c:if>--%>
			</c:if>
			<li class="btns">
				<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
                <%--<a href="${ctx}/businessdatamrg/sendingAudit/" class="btn">返回</a>--%>
			</li>
		</ul>
		<legend>代码集目录</legend>
		<div class="container-fluid breadcrumb">
			<div class="row-fluid span12">
				<span class="span4">代码集目录名称: ${sendingCodeSet.codeSetName}</span>
				<span class="span4">目标业务系统: ${sendingCodeSet.destSysName}</span>
				<span class="span4">下发状态:
					<c:choose>
						<c:when test="${sendingCodeSet.sendStatus == 0}">未发送</c:when>
						<c:when test="${sendingCodeSet.sendStatus == 1}">已发送</c:when>
						<c:when test="${sendingCodeSet.sendStatus == 2}">发送失败</c:when>
					</c:choose>
				</span>
			</div>
			<div class="row-fluid span8">
				<span class="span4">下发时间: <fmt:formatDate value="${sendingCodeSet.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</span>
			</div>
		</div>
		<legend>代码集数据</legend>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>代码</th>
					<th>名称</th>
					<th>年度</th>
					<th>操作类型</th>
					<th>有效开始时间</th>
					<th>有效结束时间</th>
					<th>发送状态</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sendingCodeItem">
				<tr>
					<td>
						${sendingCodeItem.itemCode}
					</td>
					<td>
						${sendingCodeItem.itemName}
					</td>
					<td>
						${sendingCodeItem.year}
					</td>
					<td>
						<%--${sendingCodeItem.operation eq 0 ? '新增':sendingCodeItem.operation eq 5 ? '新增':sendingCodeItem.operation eq 1 ? '修改':'撤消'}--%>
						<c:choose>
							<c:when test="${sendingCodeItem.operation == 0}">新增</c:when>
							<c:when test="${sendingCodeItem.operation == 1}">修改</c:when>
							<c:when test="${sendingCodeItem.operation == 2}">撤消</c:when>
							<c:when test="${sendingCodeItem.operation == 4}">只修改分发关系</c:when>
						</c:choose>
					</td>
					<td>
						<fmt:formatDate value="${sendingCodeItem.validStartDate}" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						<fmt:formatDate value="${sendingCodeItem.validEndDate}" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						<%--发送失败--%>
						<%--${sendingCodeItem.sendStatus == 0 ? '未发送' : '发送失败'}--%>
						<c:choose>
							<c:when test="${sendingCodeItem.sendStatus == 0}">未发送</c:when>
							<c:when test="${sendingCodeItem.sendStatus == 1}">已发送</c:when>
							<c:when test="${sendingCodeItem.sendStatus == 2}">发送失败</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</form>
</body>
</html>