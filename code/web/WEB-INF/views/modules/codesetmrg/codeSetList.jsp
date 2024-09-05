<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").click(function(){
				$("#codeSetNo").val("");
				$("#codeSetName").val("");
				$(".select2-container").find(".select2-chosen").text($("#codeGroupId").find("option:first").text());
				$("#codeGroupId").find("option:first").attr("selected","selected");
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

		//编辑代码集目录
		function editCodeSet(id,operation,processStatus){
		    var text = $(this).text();
//			if(processStatus==2){
			if(processStatus==2 && text == "修改"){
				jBox.tip('数据正在审核中，无法修改！', 'warning');
			}else{
				window.location="${ctx}/codesetmrg/codeSet/form?id="+id+"&operation="+operation+"&processStatus="+processStatus+""; 
			}
		};
		//删除代码集目录及代码集数据
		function deleteCodeSet(id,operation,processStatus, areaType, coreFlag){
		    // alert("777");
            //地市及县区不能对核心基础数据进行修改
            if (coreFlag == 0) {
		        if (areaType == 3 || areaType == 4) {
                    layer.alert("抱歉，地市及县区不能对核心基础数据进行删除操作");
                    return;
                }
            }
			top.$.jBox.confirm("确认要删除代码集吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					if(processStatus==2){
						jBox.tip('数据正在审核中，无法删除！', 'warning');
					}else{
                        layer.msg("已提交删除操作，请勿重复点击，耐心等待...", {time: 8000});
						$.ajax({
							url: '${ctx}/codesetmrg/pendingCodeSet/delete',
							type: "POST",
							data: {'id':id,'operation':operation,'processStatus':processStatus},
							dataType: "json",
							cache: false,
							success: function(data) {
//							    alertx("data = " + data);
//								if(data || data == "true"){
								if(data){
//									jBox.tip('删除成功！', 'success');
									alertx("删除成功！");
									location.reload();
								} else {
                                    jBox.tip('抱歉，删除失败，请联系管理员！', 'error');
                                }
//								if (!data) {
//                                    jBox.tip('抱歉，删除失败，请联系管理员！', 'error');
//                                }
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
								jBox.tip('删除失败，请联系管理员', 'error');
							}
						});
					}
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		};
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
	<style type="text/css">
		.form-search .ul-form li label {
			width: 100px;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/codesetmrg/codeSet/">代码集目录列表</a></li>
		<shiro:hasPermission name="codesetmrg:codeSet:edit"><li><a href="${ctx}/codesetmrg/codeSet/form">代码集目录添加</a></li></shiro:hasPermission>
	</ul>
	
	<!-- 设置业务系统HTML S -->
	<!--设置分发关系 -->
	<%--<div class="modal fade hide" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">--%>
    <%--<div class="modal-dialog">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header">--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                <%--<h4 class="modal-title" id="myModalLabel">选择业务系统</h4>--%>
            <%--</div>--%>
            <%--<form id="setSystemForm" action="${ctx}/codesetmrg/pedningCodeItem/setSystem" method="post" class="form-horizontal">--%>
            <%--<div class="modal-body">--%>
            	<%--<div class="control-group">--%>
					<%--<label class="control-label">业务系统：</label>--%>
						<%--<div class="controls">--%>
							<%--<c:forEach items="${systems}" var="system">--%>
								<%--<input type="checkbox" name="systemIds" value="${system.id}"/>${system.systemShort}--%>
							<%--</c:forEach>--%>
							<%--<input type="hidden" name="codeSetId" id="setSystem_codeSetId"/>--%>
							<%--<input type="hidden" name="codeSetNo" id="setSystem_codeSetNo"/>--%>
							<%--<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->--%>
							<%--<input type="hidden" name="codeSetOperation" id="setSystem_operation"/>--%>
							<%--<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->--%>
							<%--<input type="hidden" name="codeSetProcessStatus" id="setSystem_processStatus"/>--%>
						<%--</div>--%>
				<%--</div>--%>
            <%--</div>--%>
            <%--</form>--%>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
                <%--<button type="button" class="btn btn-primary" onClick="doSetSystem()">确定</button>--%>
            <%--</div>--%>
        <%--</div><!-- /.modal-content -->--%>
    <%--</div><!-- /.modal -->--%>
	<%--</div>--%>
	<!-- 下发代码集 -->
	<%--<div class="modal fade hide" id="sendSystem" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">--%>
    <%--<div class="modal-dialog">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-header">--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                <%--<h4 class="modal-title" id="myModalLabel">选择业务系统</h4>--%>
            <%--</div>--%>
            <%--<form id="sendSystemForm" action="${ctx}/codesetmrg/codeSetData/sendingSystemCodeSet" method="post" class="form-horizontal">--%>
            <%--<div class="modal-body">--%>
            	<%--<div class="control-group">--%>
					<%--<label class="control-label">业务系统：</label>--%>
						<%--<div class="controls">--%>
							<%--<c:forEach items="${systems}" var="system">--%>
								<%--<input type="checkbox" name="systemIds" value="${system.id}"/>${system.systemShort}--%>
							<%--</c:forEach>--%>
							<%--<input type="hidden" name="codeSetNo" id="setSendSystem_codeSetNo"/>--%>
						<%--</div>--%>
				<%--</div>--%>
            <%--</div>--%>
            <%--</form>--%>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
                <%--<button type="button" class="btn btn-primary" onClick="doSendSystem()">确定</button>--%>
            <%--</div>--%>
        <%--</div><!-- /.modal-content -->--%>
    <%--</div><!-- /.modal -->--%>
	<%--</div>--%>
	<!-- 设置业务系统HTML E -->
	
	<form:form id="searchForm" modelAttribute="codeSet" action="${ctx}/codesetmrg/codeSet" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div style="text-align:right">
			<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
		</div>
		<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>目录编码：</label><form:input path="codeSetNo" id="codeSetNo" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>目录名称：</label><form:input path="codeSetName" id="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li>
				<label>代码集分组：</label>
				<form:select path="codeGroupId" class="input-xlarge required" id="codeGroupId">  
			            <form:option value="" label="--请选择--"/>  
			            <form:options items="${categorys}" itemValue="id" itemLabel="codeGroupName" />  
		        </form:select>
	        </li>
	        
			<%--<li class="clearfix"></li>
			<li><label>代码集目录编码：</label><form:input path="" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>代码集目录名称：</label><form:input path="" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			 <li class="clearfix"></li>
			<li><label>代码集分组：</label><form:input path="" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>数据状态：</label><form:input path="" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
			<li><label>数据状态：</label><form:input path="" htmlEscape="false" maxlength="50" class="input-medium"/></li> --%>
			
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
				<th>代码集目录编码</th>
				<th>代码集目录名称</th>
				<th>代码集分组</th>
				<th>归属区域</th>
				<th>核心代码集</th>
				<th>数据状态</th>
				<th>操作类型</th>
				<th>创建时间</th>
				<%--<th>创建人</th>--%>
				<th>排序</th>
				<th>操作</th>
				<%--<th>operation,processStatus</th>--%>
				<%--<th>id</th>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="codeSet">
			
			<tr>
				<td>${codeSet.codeSetNo}</td>
				<td>${codeSet.codeSetName}</td>
				<td>${codeSet.dataSetCategory.codeGroupName}</td>
				<td>
					${fn:length(codeSet.areaName) == 0 ? '云南省' :  codeSet.areaName}
				</td>
				<td>
                    <c:choose>
                        <c:when test="${codeSet.coreFlag == 0}">是</c:when>
                        <c:when test="${codeSet.coreFlag == 1}">否</c:when>
                        <c:otherwise>未设置</c:otherwise>
                    </c:choose>
                </td>
				<td>
						<%--${codeSet.processStatus},${codeSet.operation}--%>
				   <c:if test="${codeSet.processStatus==0 || codeSet.processStatus==1}"><!-- //处理状态。0：已保存，1，初审、2：复审、3：审核通过、4、审核不通过 -->  
				        <c:if test="${codeSet.operation==0}">  <!-- 操作类型0：新增;1：修改 ;2：撤消 -->
				         	新增
				   		</c:if>
				   		<c:if test="${codeSet.operation==1}">  
				         	修改
				   		</c:if>
				   </c:if>
				   <c:if test="${codeSet.processStatus==2}">  
				         	送审
				   </c:if>
				   <c:if test="${codeSet.processStatus==10}">  
				    	正常
				   </c:if>
				
				</td>
				<td>
					<c:choose>
						<c:when test="${codeSet.operation==0}">新增</c:when>
						<c:when test="${codeSet.operation==1}">修改</c:when>
						<c:when test="${codeSet.operation==2}">撤消</c:when>
						<c:when test="${codeSet.operation==10}">正式</c:when>
						<c:otherwise>未知</c:otherwise>
					</c:choose>
				</td>
				<td><fmt:formatDate value="${codeSet.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<%--<td>${codeSet.createBy.id}</td>--%>
				<td>${codeSet.codeSetSort}</td>

				<td>
					<shiro:hasPermission name="codesetmrg:codeSet:edit">
						<%--<a href="javascript:void(0);" onclick="editCodeSet('${codeSet.id}',${codeSet.operation },${codeSet.processStatus})">修改</a>--%>
						<a href="javascript:void(0);" onclick="editCodeSet('${codeSet.id}',${codeSet.operation },${codeSet.processStatus})">
							${codeSet.processStatus == 2 ? '查看' : '修改'}
						</a>
						<a href="javascript:void(0);" onclick="deleteCodeSet('${codeSet.id}',${codeSet.operation },${codeSet.processStatus},${codeSet.areaType == null ? -1 : codeSet.areaType},${codeSet.coreFlag == null ? -1 : codeSet.coreFlag})">删除</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="codesetmrg:codeSet:view">
						<shiro:lacksPermission name="codesetmrg:codeSet:edit">
							<a href="javascript:void(0);" onclick="editCodeSet('${codeSet.id}',${codeSet.operation },${codeSet.processStatus})">查看</a>
						</shiro:lacksPermission> 
					</shiro:hasPermission>
					<%--<c:if test="${codeSet.procInstId != null}">--%>
						<%--<a href="${pageContext.request.contextPath}/workflow/wfcomponent/web/showIframe.jsp?processInstID=${codeSet.procInstId}">流程图</a>--%>
					<%--</c:if>--%>
				</td>
				<%--<td>${codeSet.operation },${codeSet.processStatus}</td>--%>
				<%--<td>${codeSet.id}</td>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>