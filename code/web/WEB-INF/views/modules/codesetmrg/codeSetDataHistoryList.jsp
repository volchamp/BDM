<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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

		});
		//全选、全反选
		function doSelectAll(){
			$("input[name=selectedRow]").prop("checked", $("#selAll").is(":checked"));		
		};
		//结转
		function doCarryOver(){
			var $inputFrom = $("#historyInputForm");
            var isSuccess = "unknown";
            var codeSetId = $.trim($("#codeSetId").val());
            var codeSetNo = $.trim($("#codeSetNo").val());
            var operation = $.trim($("#operation").val());
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
                    } else {
                        isSuccess = "no";
                        alertx("抱歉，结转失败，请稍后再试");
                    }
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// alertx('结转失败，请联系管理员');
					alertx('结转成功');
                    // alert("抱歉，异步请求出错，原因：" + textStatus + ", " + errorThrown);
                }
			});
			return isSuccess;
		}
		//批量导入
		function doImportAll() {
			var str = false;
			$("[name='selectedRow']").each(function() {
				if ($(this).attr("checked")) {
					str = true;
				}
			});
			if (str) {
				var newYear = $("#newYear").val()
				var validStartDate = $("#newValidStartDate").val()
				var validEndDate = $("#newValidEndDate").val()
				var isOK = compareDate(validStartDate, validEndDate);
				if (isOK) {
					if (newYear != null) {
						var arys = validStartDate.split('-');
						if (arys[0] != newYear) {
							alert("输入的年度有误！");
							return false;
						}
					}
					$("#year").val(newYear);
					$("#validStartDate").val(validStartDate);
					$("#validEndDate").val(validEndDate);
					var $inputFrom = $("#historyInputForm");
					$
							.ajax({
								url : '${ctx}/codesetmrg/codeSetData/codeSetHistoryImportSave',
								type : "POST",
								data : $inputFrom.serialize(),
								dataType : "json",
								cache : false,
								success : function(data) {
									if (data) {
										$('#myModal').modal('hide');
										jBox.tip('导入成功', 'success');
									}
								},
								error : function(XMLHttpRequest, textStatus,
										errorThrown) {
									console.log(textStatus);
									console.log(XMLHttpRequest);
									jBox.tip('导入失败，请联系管理员', 'error');
								}
							});
				}
			} else {
				jBox.tip('请选择导入的代码集数据', 'error');
			}
		}

		//比较日前大小  
		function compareDate(checkStartDate, checkEndDate) {
			var arys1 = new Array();
			var arys2 = new Array();
			if (!checkStartDate) {
				alert("日期开始时间不能为空");
				return false;
			}
			if (checkStartDate != null && checkEndDate != null) {
				arys1 = checkStartDate.split('-');
				var sdate = new Date(arys1[0], parseInt(arys1[1] - 1), arys1[2]);
				arys2 = checkEndDate.split('-');
				var edate = new Date(arys2[0], parseInt(arys2[1] - 1), arys2[2]);
				if (sdate > edate) {
					alert("日期开始时间大于结束时间");
					return false;
				} else {
					return true;
				}
			}

		}

		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
	</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId=${codeSetData.codeSetId}&status=0&codeSetNo=${codeSetData.codeSetNo}&operation=${codeSetData.operation}">历史代码集列表</a></li>
	</ul>
	<%-- <form:form id="" modelAttribute="codeSetData" action="${ctx}/codesetmrg/codeSetData/codeSetHistoryImport" method="post" class="breadcrumb form-search">

		<ul class="ul-form">
			<li class="btns">
				<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">历史导入</button>
			</li>
			<li class="btns">
			<button id="carryOver" class="btn btn-primary btn-lg" onClick="doCarryOver()">年度结转</button>
			</li>
			<li class="btns"><input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/></li>
			<li class="btns" style="text-align:right">
				<input id="btnAdvanced"  class="btn btn-primary btn-lg" type="button" value="高级查询"/>
			</li>
			<li class="clearfix"></li>
		</ul>
		
	</form:form> --%>
	<form:form id="searchForm" modelAttribute="codeSetData" action="${ctx}/codesetmrg/codeSetData/codeSetHistoryImport" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="codeSetId" name="codeSetId" type="hidden" value="${codeSetData.codeSetId}"/>
		<input id="codeSetNo" name="codeSetNo" type="hidden" value="${codeSetData.codeSetNo}"/>

		<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">历史导入</button>
		<%--<button id="carryOver" class="btn btn-primary btn-lg" onClick="doCarryOver()">年度结转</button>--%>
		<%--<input id="btnCancel" class="btn" type="button" value="返回上一步" onclick="history.go(-1)"/>--%>
		<%--<a class="btn" href="${ctx}/codesetmrg/pedningCodeItem/list?id=${codeSetData.codeSetId}&operation=${codeSetData.operation}&processStatus=${codeSetData.processStatus}">返回代码集列表</a>--%>
		<input id="btnAdvanced"  class="btn btn-primary btn-lg pull-right" type="button" value="高级查询"/>

		<div id="searchDiv" class="searchDiv2" style="display: none; margin:20px auto auto auto;">
			<ul class="ul-form">
				<li><label>代码集编码：</label><form:input id="itemCode" path="itemCode" htmlEscape="false" maxlength="50" class="input-medium"/></li>
				<li><label>代码集名称：</label><form:input id="itemName" path="itemName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
				<li>
					<label>年度：</label><input id="year" name="year" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
								value=""
								onclick="WdatePicker({dateFmt:'yyyy',isShowClear:false});"/>
		        </li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="btns"><input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>
				<li class="clearfix"></li>
			</ul>
		</div>
		
	</form:form>
	<form:form id="historyInputForm" modelAttribute="codeSetData" action="" method="post" class="breadcrumb form-search">
	<input id="year" name="year" type="hidden"/>
	<input id="validStartDate" name="validStartDate" type="hidden"/>
	<input id="validEndDate" name="validEndDate" type="hidden"/>
	<input id="codeSetId" name="codeSetId" type="hidden" value="${codeSetData.codeSetId}"/>
	<input id="codeSetNo" name="codeSetNo" type="hidden" value="${codeSetData.codeSetNo}"/>
	<input id="operation" name="operation" type="hidden" value="${codeSetData.operation}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th align="center"><input type="checkbox" id="selAll" onclick="doSelectAll()" /></th>
				<th>代码集编码</th>
				<th>代码集父编码</th>
				<th>代码集名称</th>
				<th>版本</th>
				<th>年度</th>
				<th>有效开始时间</th>
				<th>有效结束时间</th>
				<th>状态</th>
				<th>排序</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="codeSetData2">
			
			<tr>
				<td align="center"><input type="checkbox" name="selectedRow" value="${codeSetData2.id}" /></td>
				<td>${codeSetData2.itemCode}</td>
				<td>${codeSetData2.parentItemCode}</td>
				<td>${codeSetData2.itemName}</td>
				<td>${codeSetData2.version}</td>
				<td>${codeSetData2.year}</td>
				<td><fmt:formatDate value="${codeSetData2.validStartDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${codeSetData2.validEndDate}" pattern="yyyy-MM-dd"/></td>
				<td>${codeSetData2.status}</td>
				<td>${codeSetData2.itemCodeSort}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</form:form>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade hide" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">历史数据导入</h4>
            </div>
            <div class="modal-body">
            	<div class="control-group">
					<label class="control-label">导入的年度：</label>
					<div class="controls">
						<input id="newYear" name="newYear" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
							value=""
							onclick="WdatePicker({dateFmt:'yyyy',isShowClear:false});"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">有效开始日期：</label>
					<div class="controls">
						<input id="newValidStartDate" name="newValidStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
						<label class="control-label">至</label>
						<input id="newValidEndDate" name="newValidEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					</div>
				</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onClick="doImportAll()">提交更改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
	</div>
</body>

</html>