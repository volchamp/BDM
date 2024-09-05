<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	Object size = request.getAttribute("size");
%>
<html>
<head>
	<title>代码集管理</title>
	<meta name="decorator" content="default"/>
	<%--<meta name="renderer" content="webkit">--%>
	<%--<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">--%>
	<%--<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">--%>
	<%--<link rel="stylesheet" href="${ctxStatic}/layui-v2.4.5/layui/css/layui.css"  media="all">--%>
	<script type="text/javascript" src="${ctxStatic}/modules/codesetmrg/codeSetDataList.js"></script>
	<script type="text/javascript">
		var size ='<%=size%>';
		$(document).ready(function() {
			parent.refreshTree();
			//高级查询按钮
            $("#btnAdvanced").click(function() {
                var seaDivElem = $("#searchDiv");
                if (seaDivElem.is(":hidden")) {
                    seaDivElem.show();    //如果元素为隐藏,则将它显现
                } else {
                    seaDivElem.hide();     //如果元素为显现,则将其隐藏
                }
            });

            $("#btnReset").click(function(){
				$("#itemName").val("");
				$("#itemCode").val("");
				$("#year").val("");
				return false;
			});

            //如果是从任务列表的查看操作跳转过来，则隐藏相关按钮
			var origin = "${origin}";
			if ("taskList" == origin) {
                $("#btnReview").hide();
                $("#btnAudit").hide();
                $("#btmCodesetMapping").hide();
                $("#btnSysSending").hide();
                $("#btnImports").hide();
                $("#btnBatchDel").hide();
                $("#carryOver").hide();
                $(".editBtn").hide();
                $(".delBtn").hide();
                $("#addLi").hide();
                $("#retBtn").show();
            }
		});
		//全选、全反选
		function doSelectAll(){
			$("input[name=selectedRow]").prop("checked", $("#selAll").is(":checked"));		
		}
		
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}

        //结转
        function doCarryOver(){
		    // layer.confirm("hello layui");
		    // return;
            top.$.jBox.confirm("确认要进行年度结转吗？","系统提示",function(v,h,f){
                if(v=="ok"){
                    var $inputFrom = $("#inputForm");
                    var isSuccess = "unknown";
                    var codeSetId = $.trim($("#codeSetId").val());
                    var codeSetNo = $.trim($("#codeSetNo2").val());
                    var operation = $.trim($("#codeSetOperation").val());
                    $.ajax({
                        url : '${ctx}/codesetmrg/codeSetData/carryOver',
                        type : "POST",
                        // data : $inputFrom.serialize(),
                        data : {'codeSetId':codeSetId,'codeSetNo':codeSetNo,'operation':operation},
                        dataType : "json",
                        cache : false,
                        success : function(data) {
                            console.log(data);
                            if (data) {
                                isSuccess = "yes";
                                alertx("已成功结转下一年度");
                                window.location.reload();
                            } else {
                                isSuccess = "no";
                                alertx("抱歉，结转失败，请稍后再试");
                            }
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown) {
                            // alertx('结转失败，请联系管理员');
                            alertx('结转成功');
                            // alert("抱歉，异步请求出错，原因：" + textStatus + ", " + errorThrown);
                            window.location.reload();
                        }
                    });
                    return isSuccess;
                }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
        }

        //通用分发关系打开弹出层
		function openSetLayer() {
            var content = $("#modal-content").prop("outerHTML");
            // layer.open({
            //     type: 1,
            //     title: '选择业务系统',
            //     skin: 'layui-layer-rim', //加上边框
            //     area: ['500px', '250px'], //宽高
            //     content: content
            // });
            layerPage(content, '选择业务系统', '500px', '250px', true, true, 0.6, true, '');
        }

        function isCheckAll(thisElem) {
            var isCheckAll = $(thisElem).prop("checked");
            $("input[name='systemIds']").prop("checked", isCheckAll);
        }

        //查看代码集数据
        function viewCodeItem(id,operation){
            var codeSetProcessStatus = $("#processStatus").val();//2送审
            var codeSetOperation = $("#operation").val();
            var codeSetNo = $("#codeSetNo").val();
            var codeSetId = $("#id").val();
            window.location=ctx+"/codesetmrg/codeSetData/form?id="+id+"&operation="+operation+"&codeSetOperation="+codeSetOperation+"&codeSetProcessStatus="+codeSetProcessStatus+"&codeSetNo="+codeSetNo+"&codeSetId="+codeSetId;
        }
	</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/codesetmrg/pedningCodeItem/list?id=${codeSet.id }&operation=${codeSet.operation}&processStatus=${codeSet.processStatus}&codeSetNo=${codeSet.codeSetNo}">代码集数据列表</a></li>
		<shiro:hasPermission name="codesetmrg:codesetdata:edit">
			<c:if test="${codeSet.processStatus!=null && codeSet.processStatus !=2 }">
				<li id="addLi">
					<a id="addA" href="${ctx}/codesetmrg/codeSetData/form?codeSetId=${codeSet.id }&codeSetOperation=${codeSet.operation}&codeSetProcessStatus=${codeSet.processStatus}&codeSetNo=${codeSet.codeSetNo}">代码集数据添加</a>
				</li>
			</c:if>
		</shiro:hasPermission>
	</ul>
	
	<%-- <ul class="breadcrumb ul-form">
		<li class="btns">
		<shiro:hasPermission name="codesetmrg:codeSet:edit">
		<li class="btns"><input id="btnReview" class="btn btn-primary" type="button" value="送审" disabled="disabled"/></li>
		<li class="btns"><input id="btnAudit" class="btn btn-primary" type="button" value="审核通过" disabled="disabled"/></li>
		<li class="btns"><input id="btmCodesetMapping" class="btn btn-primary" type="button" value="通用分发关系"  data-toggle="modal" data-target="#myModal" disabled="disabled"/></li>
		<li class="btns"><input id="btnSysSending" class="btn btn-primary" type="button" value="下发业务系统" disabled="disabled" data-toggle="modal" data-target="#sendSystem"/></li>
		<li class="btns">
			<!-- <input id="btnImport" class="btn btn-primary" type="button" value="导入" disabled="disabled"/> -->
			<div class="btn-group">
				<button type="button" id="btnImports" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" disabled="disabled">
					导入<span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a id="btnImport" href="#">导入EXCEL</a>
					<li><a id="btnImportHistory" href="#">导入历史数据</a>
				</ul>
			</div>
		</li>
		<!-- <li class="btns"><input id="btnImportHistory" class="btn btn-primary" type="button" value="历史版本导入" disabled="disabled"/></li> -->
		<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出" disabled="disabled"/></li>
		<li class="btns"><input id="btnPreview" class="btn btn-primary" type="button" value="预览" disabled="disabled"/></li>
		<li class="btns"><input id="btnBatchDel" class="btn btn-primary" type="button" value="批量删除" disabled="disabled"/></li>
		</shiro:hasPermission>
		</li>
		<li class="btns pull-right"><input id="btnAdvanced"  class="btn btn-primary" type="button" value="高级查询"/></li> --%>
		<!-- <li class="clearfix"></li>
	</ul>-->

	<shiro:hasPermission name="codesetmrg:codesetdata:edit">
		<input id="btnReview" class="btn btn-primary" type="button" value="送审" disabled="disabled"/>
		<input id="btnAudit" class="btn btn-primary" type="button" value="审核通过" disabled="disabled"/>
		<%--<button data-method="confirmTrans" class="layui-btn">配置一个透明的询问框</button>--%>
		<%--<input id="btmCodesetMapping" class="btn btn-primary" type="button" value="通用分发关系"  data-toggle="modal" data-target="#myModal" disabled="disabled"/></li>--%>
		<input id="btmCodesetMapping" class="btn btn-primary" type="button" value="通用分发关系" disabled="disabled" onclick="openSetLayer();"/></li>
		<input id="btnSysSending" class="btn btn-primary" type="button" value="下发业务系统" disabled="disabled" data-toggle="modal" data-target="#sendSystem"/>

		<!-- <input id="btnImport" class="btn btn-primary" type="button" value="导入" disabled="disabled"/> -->
		<div class="btn-group">
			<button type="button" id="btnImports" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" disabled="disabled">
				导入<span class="caret"></span>
			</button>
			<ul class="dropdown-menu" >
				<li ><a id="btnImport" href="#">导入EXCEL</a>
				<li ><a id="btnImportHistory" href="#">导入历史数据</a>
			</ul>
		</div>

		<!-- <li class="btns"><input id="btnImportHistory" class="btn btn-primary" type="button" value="历史版本导入" disabled="disabled"/></li> -->
		<input id="btnExport" class="btn btn-primary" type="button" value="导出" disabled="disabled"/>
		<input id="btnPreview" class="btn btn-primary" type="button" value="预览" disabled="disabled"/>
		<input id="btnBatchDel" class="btn btn-primary" type="button" value="批量删除" disabled="disabled"/>
        <button id="carryOver" class="btn btn-primary btn-lg" onClick="doCarryOver()" disabled="disabled">年度结转</button>
    </shiro:hasPermission>
	<input id="retBtn" class="btn" type="button" value="返回" onclick="history.go(-1)" style="display: none;"/>
	<input id="btnAdvanced" class="btn btn-primary pull-right" type="button" value="高级查询" disabled="disabled"/>
	
	<form:form id="searchForm" action="${ctx}/codesetmrg/pedningCodeItem/list" method="post" class="breadcrumb form-search" style="overflow: hidden;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="operation" name="operation" type="hidden" value="${codeSet.operation}"/>
		<input id="processStatus" name="processStatus" type="hidden" value="${codeSet.processStatus}"/>
		<input id="id" name="id" type="hidden" value="${codeSet.id}"/>
		<input id="codeSetNo" name="codeSetNo" type="hidden" value="${codeSet.codeSetNo}"/>
		<input type="hidden" id="changeForm" name="changeForm"/>
		<input type="hidden" id="origin" name="origin" value="${origin}"/>

		<div id="searchDiv" class="searchDiv2" style="display: none;margin:20px auto auto auto;">
			<label>代码集编码：</label>
				<input id="itemCode" name="itemCode" type="text" class="input-medium" value="${itemCode}"/>

			<label>代码集名称：</label>
				<input id="itemName" name="itemName" type="text" class="input-medium" value="${itemName}"/>

			<label>年度：</label>
				<input id="year" name="year" type="text" value="${year}" class="input-medium"/>

			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
	</form:form>

	<sys:message content="${message}"/>
	<div class="container-fluid breadcrumb" >
		<div class="row-fluid span12">
			<span class="span4" style="width: 40%;">代码集目录编码: ${codeSet.codeSetNo}</span>
			<span class="span4" style="width: 40%;">代码集目录名称: ${codeSet.codeSetName}</span>
			<span class="span4" style="width: 40%;">区域: ${codeSet.areaName}</span>
			<span class="span4" style="width: 40%;">数据状态:
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
			</span>
		</div>
	</div>

	<%--<sys:message content="${message}"/>--%>
	<form:form id="inputForm" action="${ctx}/codesetmrg/pedningCodeItem/batchDelete" method="post" class="breadcrumb form-search">
		<input id="codeSetOperation" name="codeSetOperation" type="hidden" value="${codeSet.operation}"/>
		<input id="codeSetProcessStatus" name="codeSetProcessStatus" type="hidden" value="${codeSet.processStatus}"/>
		<input id="codeSetId" name="codeSetId" type="hidden" value="${codeSet.id}"/>
		<input id="codeSetNo" name="codeSetNo" type="hidden" value="${codeSet.codeSetNo}"/>
		<input id="codeSetNo2" name="codeSetNo2" type="hidden" value="${codeSet.codeSetNo}"/>
		<input id="systemIds" name="systemIds" type="hidden"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th align="center"><input type="checkbox" id="selAll" onclick="doSelectAll()" /></th>
				<th>代码集编码</th>
				<th>上级代码集编码</th>
				<th>代码集名称</th>
				<th>年度</th>
				<th>是否启用</th>
				<th>版本</th>
				<th>有效开始日期</th>
				<th>有效结束日期</th>
				<th style="min-width: 180px">分发关系</th>
				<th>排序</th>
				<th>备注</th>
				<shiro:hasPermission name="codesetmrg:codesetdata:edit"> 
				<th style="min-width: 60px">操作</th>
				</shiro:hasPermission> 
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="pendingCodeItem">
			<tr>
				<td align="center"><input type="checkbox" name="selectedRow" value="${pendingCodeItem.id}#${pendingCodeItem.operation}" /></td>
				<td>${pendingCodeItem.itemCode }</td>
				<td>${pendingCodeItem.parentItemCode }</td>
				<td>${pendingCodeItem.itemName}</td>
				<td>${pendingCodeItem.year }</td>
				<td>
					<!-- 是否可用0：不可用 1：未启用；2：已启用 -->
					<c:if test="${pendingCodeItem.status==0 }">
						不可用
					</c:if>
					<c:if test="${pendingCodeItem.status==1 }">
						未启用
					</c:if>
					<c:if test="${pendingCodeItem.status==2 }">
						已启用
					</c:if>
				</td>
				<td>${pendingCodeItem.version }</td>
				<td>
					<fmt:formatDate value="${pendingCodeItem.validStartDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${pendingCodeItem.validEndDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>${pendingCodeItem.distribution }</td>
				<td>${pendingCodeItem.itemCodeSort }</td>
				<td>${pendingCodeItem.remarks }</td>
				<%-- <td>${pendingCodeItem.processStatus },${pendingCodeItem.codeSetId },${pendingCodeItem.opertion }</td> --%>
				<shiro:hasPermission name="codesetmrg:codesetdata:edit">
				<td>
                    <c:if test="${pendingCodeItem.status!=0}">
    				<%-- <a href="${ctx}/codesetmrg/codeSetData/form?id=${pendingCodeItem.id}&operation=${pendingCodeItem.operation }&codeSetOperation=${codeSet.operation }&codeSetProcessStatus=${codeSet.processStatus}">修改</a> --%>
                        <a class="editBtn" href="javascript:void(0);" onclick="editCodeItem('${pendingCodeItem.id}',${pendingCodeItem.operation })">修改</a>
                        <a class="delBtn" href="javascript:void(0);" onclick="deleteById('${pendingCodeItem.id}',${pendingCodeItem.operation })">删除</a>
                    </c:if>
                    <c:if test="${pendingCodeItem.status==0}">
                        <a class="editBtn" href="javascript:void(0);" onclick="viewCodeItem('${pendingCodeItem.id}',${pendingCodeItem.operation })">查看</a>
                    </c:if>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</form:form>
	
	<!-- 导入HTML S  onClick="doImportSubmit()"-->
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/codesetmrg/codeSetData/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>
			<input type="hidden" name="codeSetId" id="import_codeSetId"/>
			<input type="hidden" name="codeSetNo" id="import_codeSetNo"/>
			<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->
			<input type="hidden" name="operation" id="import_operation"/>
			<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->
			<input type="hidden" name="processStatus" id="import_processStatus"/>
			<input id="btnImportSubmit" class="btn btn-primary" type="submit"  value="   导    入   "/>
			<a href="${ctx}/codesetmrg/codeSetData/import/template">下载模板</a>
		</form>
	</div>
	<!-- 导入HTML E -->
	
	<!-- 设置业务系统HTML S -->
	<!--设置分发关系 -->
	<div class="modal fade hide" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content">
            <%--<div class="modal-header">--%>
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                <%--<h4 class="modal-title" id="myModalLabel">选择业务系统</h4>--%>
            <%--</div>--%>
            <form id="setSystemForm" action="${ctx}/codesetmrg/pedningCodeItem/setSystem" method="post" class="form-horizontal">
            <div class="modal-body">
            	<div class="control-group">
					<%--<label class="control-label">--%>
					<label class="">
						业务系统：
						<input type="checkbox" onclick="isCheckAll(this);"/>是否全选
					</label>
						<%--<div class="controls">--%>
						<div class="">
							<c:forEach items="${systems}" var="system">
								<input type="checkbox" name="systemIds" value="${system.id}"/>${system.systemShort}
							</c:forEach>
						</div>
				</div>
            </div>
            </form>
            <div class="modal-footer">
                <%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
                <button type="button" class="btn btn-primary" onClick="doSetSystem()">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
	</div>
	<!-- 下发代码集 -->
	<div class="modal fade hide" id="sendSystem" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">选择业务系统</h4>
            </div>
            <form id="sendSystemForm" action="${ctx}/codesetmrg/codeSetData/sendingSystemCodeSet" method="post" class="form-horizontal">
            <div class="modal-body">
            	<div class="control-group">
					<label class="control-label">业务系统：</label>
						<div class="controls">
							<c:forEach items="${systems}" var="system">
								<input type="checkbox" name="SendsystemIds" value="${system.id}"/>${system.systemShort}
							</c:forEach>
						</div>
				</div>
            </div>
            </form>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onClick="doSendSystem()">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
	</div>
	<!-- 设置业务系统HTML E -->

	<%--<script src="${ctxStatic}/layui-v2.4.5/layui/layui.js" charset="utf-8"></script>--%>
	<%--<script>--%>
        <%--layui.use('layer', function(){ //独立版的layer无需执行这一句--%>
            <%--var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句--%>
        <%--});--%>
	<%--</script>--%>
</body>
</html>