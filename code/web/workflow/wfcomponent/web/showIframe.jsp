<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>流程图</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/user_css/style-custom.css">

</head>
<%
		String processInstID = request.getParameter("processInstID");		 
  %>
<body>
	<form name="frm"
		action="<%=request.getContextPath()%>/workflow/wfcomponent/web/showWFGraph.jsp"
		target="graph">
		<table width="100%" bgcolor="fefefe" height="100%" border="0">
			<tr valign="top">
				<td align="left">
					<input id="btnCancel" class="btn" style="cursor: pointer;" type="button" value="返 回" onclick="history.go(-1)"/>
				</td>
				<td align="right" height="2%">缩放比例： <select
					onChange="handleZoom(this)" size="1" name="zoomQuotiety">
						<option value="1.0">自动</option>
						<option value="0.4">40%</option>
						<option value="0.55">55%</option>
						<option value="0.7">70%</option>
						<option value="0.85">85%</option>
						<option value="1.0">100%</option>
						<option value="1.5">150%</option>
						<option value="2.0">200%</option>
				</select>
				</td>
			</tr>

			<tr>
				<td id="graphTD" valign="top" height="100%">
					<iframe id="graph"
						name="graph"
						src='<%=request.getContextPath()%>/workflow/wfcomponent/web/showWFGraph.jsp?processInstID=<%=processInstID %>&zoom=1'
						scrolling="auto" style="height: 100%; width: 100%;"
						frameborder="0"> </iframe>
				</td>
			</tr>
		</table>
		<input type="hidden" name="processInstID" value=<%=processInstID%>>
		<input type="hidden" name="zoomvalue">
	</form>
</body>
<script>
  function handleZoom(selectObj) {
	 frm.zoomvalue.value=selectObj.value;
	 frm.submit();	 
  }
</script>
</html>
