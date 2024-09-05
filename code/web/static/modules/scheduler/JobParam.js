
/**
 * 功能：添加行
 * @author Xwt
 */
function addRow()
{
	var objContainer = $("#trContainer");
	var len = objContainer.children().length;
	var strTr = '';
	strTr += '<tr>';
	strTr += '<td style="text-align: center;">';
	strTr += '<input type="text" id="paraName" name="paraName" />';
	strTr += '</td>';
	strTr += '<td style="text-align: center;">';
	strTr += '<select id="paraType" name="paraType">';
	strTr += '<option value="int">int</option>';
	strTr += '<option value="long">long</option>';
	strTr += '<option value="float">float</option>';
	strTr += '<option value="string">string</option>';
	strTr += '<option value="blooean">blooean</option>';
	strTr += '</select>';
	strTr += '</td>';
	strTr += '<td style="text-align: center;">';
	strTr += '<input type="text" id="paraValue" name="paraValue"/>';
	strTr += '</td>';
	strTr += '<td>';
	strTr += '<a href="javascript:;" onclick="delRow(this);">删除</a>';
	strTr += '</td>';
	strTr += '</tr>';
	var trObj = $(strTr);
	var trContainer = $("#trContainer");
	trContainer.append(trObj);
}

/**
 * 功能：删除行
 * @param obj 当前对象
 * @author Xwt
 * @returns
 */
function delRow(obj) {
 	var obj = $(obj);
	var trObj = obj.parent().parent();
	trObj.remove();
}


/**
 * 功能：设置XML格式参数
 * @author Xwt
 * @returns
 */
function setParameterXml() {
	var objContainer = $("#trContainer");
	var len = objContainer.children().length;
	var children = objContainer.children() ;
	var xml = "[";
	children.each(function(i) {
		var name = $(this).find('input[name=paraName]').val();
		var type = $(this).find('select[name=paraType]').val();
		var value = $(this).find('input[name=paraValue]').val();
		if (i < len-1)
			xml += "{\"name\":\"" + name + "\",\"type\":\"" + type + "\",\"value\":\"" + value + "\"},";
		else
			xml += "{\"name\":\"" + name + "\",\"type\":\"" + type + "\",\"value\":\"" + value + "\"}";
		 
	});
	xml += "]";
	return xml;
}