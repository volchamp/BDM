/***************************************************************
 简述：拼音工具类
 @author Xwt
 @date 2017-6-7
 @description
 
****************************************************************/

// 实例化Share
var Share = function(){}

/**
 * 功能：传入字符串，取得相应的拼音
 * @param {Object} input:'用于计算拼音的字符串',preCallback:Function,postCallback:Function,只有参数input是必填
 * @returns {String} 传入字符串对应的拼音
 */
Share.getPingyin = function(conf) {	
	var v = true;
	if (!conf) {
		v = false;
	} else if (typeof(conf) == 'object') {
		if (!conf.input) {
			v = false;
		}
	} else {
		conf = {
			input : conf
		};
	}
	
	if (!v) {
		throw new Error("方法GetPingyin用法：GetPingyin(String) 或 GetPingyin({input:'用于计算拼音的字符串',preCallback:Function,postCallback:Function}),只有参数input是必填！");
	}

	
	var py;
	var url = ctx + "/sys/tSystem/getPingyin";
	url = url.getNewUrl(); // 调用StringUtils.js的getNewUrl函数
	$.ajax(url, {
		type : "post",
		async : false,
		data : {
			input : conf.input
		},
		beforeSend : function() {
			conf.preCallback && conf.preCallback();
		}
	}).done(function(data) {
		conf.postCallback && conf.postCallback(data);
		py = data.output;
	});
	return py;
};

/**
 * 功能：绑定拼音转换。
 * @param {Object} 需要转换的源对象。
 * @param {Object} 目标对象。
 * @description 参数均为jquery对象。
 */
Share.setPingyin = function(source,target) {
	
	if(source.val().length == 0) return ;

	// 判断对象是否是数组
	if (isArray(target)) {
		for (var i in target) {
			Share.getPingyin({
				input:source.val(),
				postCallback:function(data) {
					target[i].val(data.output);
				}
			});
		}
	} else {
		if (target.val().length > 0) return;
		Share.getPingyin({
			input:source.val(),
			postCallback:function(data) {
				target.val(data.output);
			}
		});
	}
}

/**
 * 功能：绑定拼音转换。（大写）
 * @param {Object} 需要转换的源对象。
 * @param {Object} 目标对象。
 * @description 参数均为jquery对象。
 */
Share.setPingyinUpperCase = function(source,target) {
	if(source.val().length == 0) return;
	
	if (target.val().length > 0) return;
	Share.getPingyin({
		input:source.val(),
		postCallback:function(data) {
			target.val(data.output.toUpperCase());
		}
	});
}

/**
 * 功能：判断是否为数组
 * @param {Object} 传过来的对象
 * @returns {Boolean} 返回布尔型
 */
function isArray(obj) {
	return Object.prototype.toString.call(obj) === '[object Array]';
}