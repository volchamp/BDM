<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集拆分合并关系管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            combineCell("contentTable");
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }

        function combineCell(tableId) {
            // var html = $("#contentTable").html();
            // alert("html = " + html);
            var table = document.getElementById(tableId);
            for (var i = 0; i < table.rows.length; i++) {
                for (var c = 0; c < table.rows[i].cells.length; c++) {
                    if (c <= 5) {	//选择要合并的列序数，将操作列排除
                        for (var j = i + 1; j < table.rows.length; j++) {
                            var cell1 = table.rows[i].cells[c].innerHTML;
                            var cell2 = table.rows[j].cells[c].innerHTML;
                            if (cell1 == cell2) {
                                table.rows[j].cells[c].style.display = 'none';
                                table.rows[j].cells[c].style.verticalAlign = 'middle';
                                table.rows[i].cells[c].rowSpan++;
                            } else {
                                table.rows[j].cells[c].style.verticalAlign = 'middle'; //合并后剩余项内容自动居中
                                break;
                            }
                        }
                    }
                }
            }
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li class="active"><a href="${ctx}/codesetmrg/splitMerge/">代码集拆分合并关系列表</a></li>--%>
		<%--<shiro:hasPermission name="codesetmrg:splitMerge:edit"><li><a href="${ctx}/codesetmrg/splitMerge/form">代码集拆分合并关系添加</a></li></shiro:hasPermission>--%>
	<%--</ul>--%>
	<form:form id="searchForm" modelAttribute="splitMerge" action="${ctx}/codesetmrg/splitMerge/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<%--<ul class="ul-form">--%>
			<%--<li><label>old_item_code：</label>--%>
				<%--<form:input path="oldItemCode" htmlEscape="false" maxlength="100" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>old_item_name：</label>--%>
				<%--<form:input path="oldItemName" htmlEscape="false" maxlength="256" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>old_kind_code：</label>--%>
				<%--<form:input path="oldKindCode" htmlEscape="false" maxlength="100" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>new_item_code：</label>--%>
				<%--<form:input path="newItemCode" htmlEscape="false" maxlength="100" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>new_item_name：</label>--%>
				<%--<form:input path="newItemName" htmlEscape="false" maxlength="256" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>new_kind_code：</label>--%>
				<%--<form:input path="newKindCode" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		<%--</ul>--%>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<%--<th>old_item_id</th>--%>
				<th>旧代码集编码</th>
				<th>旧代码集名称</th>
				<th>旧代码集内编码</th>
				<%--<th>new_item_id</th>--%>
				<th>新代码集编码</th>
				<th>新代码集名称</th>
				<th>新代码集内编码</th>
				<%--<shiro:hasPermission name="codesetmrg:splitMerge:edit">--%>
					<th>操作</th>
				<%--</shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="splitMerge">
			<tr>
				<%--<td><a href="${ctx}/codesetmrg/splitMerge/form?id=${splitMerge.id}">--%>
					<%--${splitMerge.oldItemId}--%>
				<%--</a></td>--%>
				<td>
					${splitMerge.oldItemCode}
				</td>
				<td>
					${splitMerge.oldItemName}
				</td>
				<td>
					${splitMerge.oldKindCode}
				</td>
				<%--<td>--%>
					<%--${splitMerge.newItemId}--%>
				<%--</td>--%>
				<td>
					${splitMerge.newItemCode}
				</td>
				<td>
					${splitMerge.newItemName}
				</td>
				<td>
					${splitMerge.newKindCode}
				</td>
				<%--<shiro:hasPermission name="codesetmrg:splitMerge:edit">--%>
					<td>
    				<%--<a href="${ctx}/codesetmrg/splitMerge/form?id=${splitMerge.id}">修改</a>--%>
					<a href="${ctx}/codesetmrg/splitMerge/delete?id=${splitMerge.id}" onclick="return confirmx('确认要删除该代码集拆分合并关系吗？', this.href)">删除</a>
				</td>
				<%--</shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>