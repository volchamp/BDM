<%--
 * 数据审核处理页
 * 
 * @author Xwt
 * @date 2017-06-13
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据审核处理页</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			// 审核操作
			$(".audit").click(function() {
				
				var status = $(this).attr("status");
				var form = $("#inputForm");
				var processStatus = $("#processStatus");
				processStatus.val(status); // 设置审核状态
				// 审核通过
				if (processStatus.val() == 3) {
					if (processStatus.val() != '') {
						
						// var html = "<div style='padding:10px;'>请输入是否立即下发业务系统(Y/N)：<input type='text' id='changeForm' name='changeForm' /></div>";
						// var submit = function (v, h, f) {
						// 	var r = f.changeForm;
						//     if (r == '') {
						//     	jBox.tip("请输入是否立即下发业务系统。", 'error', { focusId: "changeForm" }); // 关闭设置 changeForm 为焦点
						//         return false;
						//     }
						//     var reg= /^[A-Za-z]+$/;
						// 	if (reg.test(r)) { //判断是否符合正则表达式
						// 		r = r.toUpperCase();
						// 		if (r == "Y" || r == "N") {
						// 			$("#changeForm").val(r);
						// 			loading('正在提交，请稍等...');
						// 			form.submit();
						// 		} else {
						// 			jBox.tip("请输入Y/N");
						// 			return false;
						// 		}
						// 	} else {
						// 		jBox.tip("请输入Y/N");
						// 		return false;
						// 	}
						//     return true;
						// };
						// jBox(html, { title: "提示信息", submit: submit });

                        layer.confirm('是否立即下发业务系统？', {
                                // time: 20000, //20s后自动关闭
                                btn: ['是', '否', '取消'],
                                icon: 3,
                                title:'请选择'
                            }
                            , function(){
                                $("#changeForm").val("Y");
                                loading('正在提交，请稍等...');
                                form.submit();
                            }, function(){
                                $("#changeForm").val("N");
                                loading('正在提交，请稍等...');
                                form.submit();
                            }
                        );
					}
				} else {
					if (processStatus.val() != '') {
                        layer.confirm('确定执行此操作？', {
                                // time: 20000, //20s后自动关闭
                                btn: ['是', '取消'],
                                icon: 3,
                                title:'请选择'
                            }
                            , function(){
                                loading('正在提交，请稍等...');
                                form.submit();
                            }
                        );
					}
				}
			});
			
		});
		
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li>--%>
			<%--<c:choose>--%>
				<%--<c:when test="${origin != null}">--%>
					<%--<a href="${ctx}/tesk/task/?taskStatus=${pendingCodeSet.taskStatus}">代码集任务列表</a>--%>
				<%--</c:when>--%>
				<%--<c:otherwise>--%>
					<%--<a href="${ctx}/businessdatamrg/pendingAudit/">代码集审核列表</a>--%>
				<%--</c:otherwise>--%>
			<%--</c:choose>--%>
		<%--</li>--%>
		<%--<li class="active">--%>
			<%--&lt;%&ndash;<a href="${ctx}/businessdatamrg/pendingAudit/form?id=${pendingCodeSet.id}">数据审核处理</a>&ndash;%&gt;--%>
			<%--<a href="">数据审核处理</a>--%>
		<%--</li>--%>
	<%--</ul>--%>
	<form id="inputForm" action="${ctx}/businessdatamrg/pendingAudit/review" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${pendingCodeSet.id }"/>
		<input type="hidden" name="systemId" value="${pendingCodeSet.systemId }"/>
		<input type="hidden" name="workItemId" value="${pendingCodeSet.workItemId }"/>
		<input type="hidden" name="procInstId" value="${pendingCodeSet.procInstId }"/>
		<input type="hidden" id="processStatus" name="processStatus" />
		<input type="hidden" id="changeForm" name="changeForm"/>
		<input type="hidden" name="origin" value="${origin}"/>
		<ul class="breadcrumb ul-form">
			<%--<shiro:hasPermission name="businessdatamrg:pendingAudit:review">--%>
				<c:if test="${pendingCodeSet.taskStatus==0  && pendingCodeSet.taskHandlerId!=fns:getUser()}">
					<li class="btns"><input id="agree" class="btn btn-primary audit" status="3" type="button" value="审核通过"/></li>
					<li class="btns"><input id="refuse" class="btn btn-primary audit" status="4" type="button" value="审核不通过"/></li>
					<li class="btns"><input id="back" class="btn btn-primary audit" status="1" type="button" value="退回"/></li>
				</c:if>
			<%--</shiro:hasPermission>--%>
			<shiro:hasPermission name="businessdatamrg:pendingAudit:view">
				<c:if test="${pendingCodeSet.taskStatus==0}">
					<c:if test="${pendingCodeSet.taskHandlerId==fns:getUser()}">
						<li class="btns"><input id="agree" class="btn btn-primary audit" status="3" type="button" value="审核通过"/></li>
						<li class="btns"><input id="refuse" class="btn btn-primary audit" status="4" type="button" value="审核不通过"/></li>
						<li class="btns"><input id="back" class="btn btn-primary audit" status="1" type="button" value="退回"/></li>
					</c:if>
				</c:if>
			</shiro:hasPermission>
			<li class="btns">
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<%--<a href="${ctx}/tesk/task/?taskStatus=${pendingCodeSet.taskStatus}" class="btn">返 回</a>--%>
			</li>
		</ul>
		<fieldset>
        <legend>代码集目录</legend>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码集分组名称：</label>--%>
			<%--<div class="controls">--%>
				<%--${codeGroupName }--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码集目录编码：</label>--%>
			<%--<div class="controls">--%>
				<%--${pendingCodeSet.codeSetNo }--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码集目录名称：</label>--%>
			<%--<div class="controls">--%>
				<%--${pendingCodeSet.codeSetName }--%>
			<%--</div>--%>
		<%--</div>--%>
		<%-- <div class="control-group">
			<label class="control-label">表示格式说明：</label>
			<div class="controls">
				<textarea id="formatDesc" class="input-xxlarge" readonly="readonly" rows="3">${pendingCodeSet.formatDesc }</textarea>
			</div>
		</div> --%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码规则说明：</label>--%>
			<%--<div class="controls">--%>
				<%--&lt;%&ndash;<pre>${pendingCodeSet.ruleDesc }</pre>&ndash;%&gt;--%>
				<%--${pendingCodeSet.ruleDesc }--%>
			<%--</div>--%>
		<%--</div>--%>
		<%-- <div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<textarea id="remarks" class="input-xxlarge" readonly="readonly" rows="3">${pendingCodeSet.remarks }</textarea>
			</div>
		</div> --%>
			<div class="container-fluid breadcrumb">
				<div class="row-fluid span12">
					<span class="span4">代码集分组名称: ${codeGroupName}</span>
					<span class="span4">代码集目录编码: ${pendingCodeSet.codeSetNo}</span>
					<span class="span4">代码集目录名称: ${pendingCodeSet.codeSetName}</span>
				</div>
				<div class="row-fluid span8">
					<span class="span4">代码规则说明: ${pendingCodeSet.ruleDesc}</span>
				</div>
			</div>
		</fieldset>
		<%--<br/>--%>
		<fieldset>
        <legend>代码集数据</legend>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>代码集编码</th>
					<th>上级代码集编码</th>
					<th>代码集名称</th>
					<th>年度</th>
					<th>版本</th>
					<th>分发关系</th>
					<th>有效时间</th>
					<th>操作</th>
					<th>原名称</th>
					<th>原版本</th>
					<th>原分发关系</th>
					<th>原有效时间</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="pendingCodeItem">
				<tr>
					<td>
						${pendingCodeItem.itemCode}
					</td>
					<td>
						${pendingCodeItem.parentItemCode}
					</td>
					<td>
						${pendingCodeItem.itemName}
					</td>
					<td>
						${pendingCodeItem.year}
					</td>
					<td>
						${pendingCodeItem.version}
					</td>
					<td>
						${pendingCodeItem.distribution}
					</td>
					<td>
						<fmt:formatDate value="${pendingCodeItem.validStartDate}" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						${pendingCodeItem.operation eq 0 ? '新增': pendingCodeItem.operation eq 2 ? '撤消' : '修改'}
					</td>
					<td>
						${pendingCodeItem.codeSetData.itemName}
					</td>
					<td>
						${pendingCodeItem.codeSetData.version}
					</td>
					<td>
						${pendingCodeItem.codeSetData.distribution}
					</td>
					<td>
						<fmt:formatDate value="${pendingCodeItem.codeSetData.validStartDate}" pattern="yyyy-MM-dd"/>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
		</fieldset>
		<br/>
		<%--<br/>--%>
		<%--<ul class="breadcrumb ul-form">--%>
			<%--<shiro:hasPermission name="businessdatamrg:pendingAudit:review">--%>
				<%--<c:if test="${pendingCodeSet.taskStatus==0}">--%>
					<%--<li class="btns"><input id="agree" class="btn btn-primary audit" status="3" type="button" value="审核通过"/></li>--%>
					<%--<li class="btns"><input id="refuse" class="btn btn-primary audit" status="4" type="button" value="审核不通过"/></li>--%>
					<%--<li class="btns"><input id="back" class="btn btn-primary audit" status="1" type="button" value="退回"/></li>--%>
				<%--</c:if>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="businessdatamrg:pendingAudit:view">--%>
				<%--<c:if test="${pendingCodeSet.taskStatus==0}">--%>
					<%--<c:if test="${pendingCodeSet.taskHandlerId==fns:getUser()}">--%>
						<%--<li class="btns"><input id="agree" class="btn btn-primary audit" status="3" type="button" value="审核通过"/></li>--%>
						<%--<li class="btns"><input id="refuse" class="btn btn-primary audit" status="4" type="button" value="审核不通过"/></li>--%>
						<%--<li class="btns"><input id="back" class="btn btn-primary audit" status="1" type="button" value="退回"/></li>--%>
					<%--</c:if>--%>
				<%--</c:if>--%>
			<%--</shiro:hasPermission>--%>
			<%--<li class="btns"><input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/></li>--%>
		<%--</ul>--%>
	</form>
	
</body>
</html>