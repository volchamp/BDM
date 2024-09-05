<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集数据预览页</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">代码集预览</a></li>
	</ul>
	<form:form id="" action="" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li class="btns"><input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<form id="inputForm" action="" method="post" class=" form-horizontal">
		<input type="hidden" id="id" name="id" value="${pendingCodeSet.id }"/>
		<input type="hidden" id="codeSetNo" name="codeSetNo" value="${pendingCodeSet.codeSetNo }"/>
		<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">代码集目录编码: ${pendingCodeSet.codeSetNo}</span>
			<span class="span4">代码集目录名称: ${pendingCodeSet.codeSetName}</span>
			<span class="span4">数据状态:
			<c:if test="${pendingCodeSet.processStatus==0 || pendingCodeSet.processStatus==1}"><!-- //处理状态。0：已保存，1，初审、2：复审、3：审核通过、4、审核不通过 -->
				<c:if test="${pendingCodeSet.operation==0}">  <!-- 操作类型0：新增;1：修改 ;2：撤消 -->
				 	新增
				</c:if>
				<c:if test="${pendingCodeSet.operation==1}">
				 	修改
				</c:if>
			</c:if>
			<c:if test="${pendingCodeSet.processStatus==2}">
				送审
			</c:if>
			<c:if test="${pendingCodeSet.processStatus==10}">
				正常
			</c:if></span>
			</div>
		</div>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>代码</th>
					<th>名称</th>
					<th>年度</th>
					<th>版本</th>
					<th>分发关系</th>
					<th>有效时间</th>
					<th>操作</th>
					<th>原名称</th>
					<th>原年度</th>
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
						${pendingCodeItem.operation eq 0 ? '新增':pendingCodeItem.operation eq 1 ? '修改' : pendingCodeItem.operation eq 2 ? '撤消': pendingCodeItem.operation eq 5 ? '新增' : '修改'}
					</td>
					<td>
						${pendingCodeItem.codeSetData.itemName}
					</td>
					<td>
						${pendingCodeItem.codeSetData.year}
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
		<br/><br/>
	</form>
</body>
</html>