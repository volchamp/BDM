/***************************************************************
 简述：字符串工具类
 @author Xwt
 @date 2017-6-7
 @description
 
****************************************************************/


// 功能：给url添加一个当前时间日期数值，使页面不会被缓存。
String.prototype.getNewUrl = function() {
	// 如果url中没有参数。
 	var time = new Date().getTime();
 	var url = this;
 	//去除‘#’后边的字符
 	if (url.indexOf("#") != -1) {
 		var index = url.lastIndexOf("#",url.length-1);
 		url = url.substring(0, index);
 	}
 	
 	while (url.endWith("#")) {
 		url = url.substring(0, url.length-1);
 	}
 	
 	url = url.replace(/(\?|&)rand=\d*/g, "");
 	
 	if (url.indexOf("?") == -1) {
 		url += "?rand=" + time;
 	} else {
 		url += "&rand=" + time;
 	}
 	
 	return url;
}

/**
 * 功能：判断结束是否相等
 * @param {String} 字符串
 * @param {Boolean} 是否区分大小写
 * @return {Boolean}
 */
String.prototype.endWith = function(str, isCasesensitive) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	var tmp = this.substring(this.length - str.length);
	if (isCasesensitive == undefined || isCasesensitive) {
		return tmp == str;
	} else {
		return tmp.toLowerCase() == str.toLowerCase();
	}
};