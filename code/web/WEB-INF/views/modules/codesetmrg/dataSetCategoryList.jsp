<%--
 * 代码集分组管理-列表
 * 
 * @author Xwt
 * @date 2017-06-06
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集分组管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").click(function(){
				$("#codeGroupName").val("");
				$("#codeStartWith").val("");
				$("#codeEndWith").val("");
				return false;	//
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

            // 引用layui模块
            layui.use('element', function(){
                var element = layui.element;

                // 绑定按钮 触发layui添加tabs事件
                $("#searchDiv").on('click','.amore',function(){
                    var t=$(this);
                    var jurl =t.attr("jurl"); // 获取跳转路径
                    var id=t.attr("id"); // 获取id
                    var tabName=t.attr("tabName"); // 获取选项卡名称

                    // 判断是否已经添加过该选项卡
                    var layId_listLen=parent.$("#n1127_demo li[lay-id="+id+"]").length;
                    if(layId_listLen>0){// 若已经添加过该选项卡 则不再添加 根据id判断
                        return false;
                    }

                    parent.n1127_addIframe({
                        jurl:jurl,
                        id:id,
                        tabName:tabName
                    })


                })
            });
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }

        // function add() {
			// return;
        // }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/codesetmrg/dataSetCategory/">代码集分组列表</a></li>
		<shiro:hasPermission name="codesetmrg:dataSetCategory:edit"><li><a href="${ctx}/codesetmrg/dataSetCategory/form">代码集分组添加</a></li></shiro:hasPermission>
	</ul>

	<%--<div class="form-group">--%>
		<%--<button type="button" class="btn btn-success btn-sm click-adv fr"><i class="glyphicon glyphicon-wrench" > </i>高级查询</button>--%>
	<%--</div>--%>
	<%--<div class="adv-search clearfix dn">--%>
		<%--<div class="arrow-up"><span><em></em></span></div>--%>

	<form:form id="searchForm" modelAttribute="dataSetCategory" action="${ctx}/codesetmrg/dataSetCategory/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<%--<div style="text-align:right">--%>
		<%--<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>--%>
	<%--</div>--%>
	<div id="searchDiv" class="searchDiv2" style="display: block">
		<ul class="ul-form">
			<li><label>分组名称：</label><form:input path="codeGroupName" id="codeGroupName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<%--<li><label>开始范围：</label><form:input path="codeStartWith" id="codeStartWith" htmlEscape="false" maxlength="50" class="input-medium"/></li>--%>
			<%--<li><label>结束范围：</label><form:input path="codeEndWith" id="codeEndWith" htmlEscape="false" maxlength="50" class="input-medium"/></li>--%>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			<%--<shiro:hasPermission name="codesetmrg:dataSetCategory:edit">--%>
				<%--<a class="amore btn btn-primary" jurl="${ctx}/codesetmrg/dataSetCategory/form" href="javascript:void(0)"--%>
				   <%--id="amoreDscAdd" tabName="代码集分组添加">添加</a>--%>
			<%--</shiro:hasPermission>--%>
			</li>
			<li class="clearfix"></li>
		</ul>
	</div>
	</form:form>

	<%--</div>--%>

	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>分组名称</th>
				<th>开始范围</th>
				<th>结束范围</th>
				<th>备注</th>
				<th width="5%">排序</th>
				<shiro:hasPermission name="codesetmrg:dataSetCategory:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dataSetCategory">
			<tr>
				<td>
					<a href="${ctx}/codesetmrg/dataSetCategory/form?id=${dataSetCategory.id}"></a>
					${dataSetCategory.codeGroupName}
				</td>
				<td>
					${dataSetCategory.codeStartWith}
				</td>
				<td>
					${dataSetCategory.codeEndWith}
				</td>
				<td>
					${dataSetCategory.remarks}
				</td>
				<td>
					${dataSetCategory.codeGroupSort}
				</td>
				<shiro:hasPermission name="codesetmrg:dataSetCategory:edit"><td>
    				<a href="${ctx}/codesetmrg/dataSetCategory/form?id=${dataSetCategory.id}">修改</a>
					<a href="${ctx}/codesetmrg/dataSetCategory/delete?id=${dataSetCategory.id}" onclick="return confirmx('确认要删除该代码集分组吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>