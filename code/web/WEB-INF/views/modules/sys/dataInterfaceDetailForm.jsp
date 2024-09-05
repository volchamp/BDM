<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口授权管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(function(){
			//获取字段的选中状态，监听全选的checkbox
			$("#checkAll").click(function(){
				var isCheck = ('checked' == $(this).attr("checked"))?true:false;
				$("input[name='check']").attr('checked',isCheck);
			});
		});
		//返回选择的结果
		var getTableAndFiledFun = function(){
			var ids = [];
			$("input[name='check']").each(function(){
				if((('checked' == $(this).attr("checked"))?true:false)){
					ids.push($(this).val());
				}
			});
			var rs = new Object();
			rs.tablename = $('#tablename option:selected').val();
			rs.fields = ids;
			return rs;
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="dataInterfaceDetail" action="${ctx}/sys/dataInterfaceDetail/form" method="post" class="form-horizontal">
		<div class="row-fluid span12">
			<div class="control-group">
				<label class="control-label">业务表名:</label>
				<div class="controls">
					<form:select id="tablename" path="tablename" class="required input-xlarge" onchange="javascript:$('#inputForm').submit();">
						<form:options items="${tableList}" itemLabel="nameAndComments" itemValue="name" htmlEscape="false"/>
					</form:select>
					<span class="help-inline">选择需要共享或上传的业务表名称。</span>
				</div>
			</div>
		</div>
		<div class="row-fluid span12">
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead><tr><th><input type="checkbox" id="checkAll"/></th><th title="数据库字段名">列名</th><th title="默认读取数据库字段备注">说明</th><th title="数据库中设置的字段类型及长度">物理类型</th><th>是否主键</th></tr></thead>
				<tbody>
				<c:forEach items="${genTable.columnList}" var="column" varStatus="vs">
					<tr${column.delFlag eq '1'?' class="error" title="已删除的列，保存之后消失！"':''}>
						<td><input type="checkbox" name="check" value="${column.name}"></td>
						<td nowrap>${column.name}</td>
						<td>${column.comments}</td>
						<td nowrap>${column.jdbcType}</td>
						<td>${'1' == column.isPk?"是":"否"}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</form:form>
</body>
</html>