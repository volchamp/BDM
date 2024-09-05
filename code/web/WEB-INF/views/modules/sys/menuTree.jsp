<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%--
<html>
<head>
	<title>菜单导航</title>
	<meta name="decorator" content="blank"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".accordion-heading a").click(function(){
				$('.accordion-toggle i').removeClass('icon-chevron-down');
				$('.accordion-toggle i').addClass('icon-chevron-right');
				if(!$($(this).attr('href')).hasClass('in')){
					$(this).children('i').removeClass('icon-chevron-right');
					$(this).children('i').addClass('icon-chevron-down');
				}
			});
			$(".accordion-body a").click(function(){
				$("#menu-${param.parentId} li").removeClass("active");
				$("#menu-${param.parentId} li i").removeClass("icon-white");
				$(this).parent().addClass("active");
				$(this).children("i").addClass("icon-white");
				//loading('正在执行，请稍等...');
			});
			//$(".accordion-body a:first i").click();
			//$(".accordion-body li:first li:first a:first i").click();
		});
	</script>
</head>
<body> --%>
<style type="text/css">
	.n1203_a {
		cursor: pointer;
	}

	/*右键关闭选项卡*/
	.rightmenu { position: absolute; width: 80px; z-index: 9999; display: none; background-color: #fff; padding: 2px; color: #333; border: 1px solid #eee; border-radius: 2px; cursor: pointer; }
	.rightmenu li { text-align: center; display: block; height: 25px; line-height: 25px; }
	.rightmenu li:hover { background-color: #666; color: #fff; }
</style>

	<!-- 侧边菜单 -->
	<div class="accordion" id="menu-${param.parentId}"><c:set var="menuList" value="${fns:getMenuList()}"/><c:set var="firstMenu" value="true"/><c:forEach items="${menuList}" var="menu" varStatus="idxStatus"><c:if test="${menu.parent.id eq (not empty param.parentId ? param.parentId:1)&&menu.isShow eq '1'}">
	<%--<div class="accordion" id="menu-${param.parentId}"><c:set var="menuList" value="${menuList2}"/><c:set var="firstMenu" value="true"/><c:forEach items="${menuList}" var="menu" varStatus="idxStatus"><c:if test="${menu.parent.id eq (not empty param.parentId ? param.parentId:1)&&menu.isShow eq '1'}">--%>
		<div class="accordion-group">
		    <div class="accordion-heading">
		    	<a class="accordion-toggle" data-toggle="collapse" data-parent="#menu-${param.parentId}" data-href="#collapse-${menu.id}" href="#collapse-${menu.id}" title="${menu.remarks}"><i class="icon-chevron-${not empty firstMenu && firstMenu ? 'down' : 'right'}"></i>&nbsp;${menu.name}</a>
		    </div>
		    <div id="collapse-${menu.id}" class="accordion-body collapse ${not empty firstMenu && firstMenu ? 'in' : ''}">
				<div class="accordion-inner">
					<ul class="nav nav-list">
						<c:forEach items="${menuList}" var="menu2"><c:if test="${menu2.parent.id eq menu.id&&menu2.isShow eq '1'}">
						<li>
							<a class="n1203_a" tabName="${menu2.name}" tabId="menu2${menu2.id}" data-href=".menu3-${menu2.id}"
							   jhref="${fn:indexOf(menu2.href, '://') eq -1 ? ctx : ''}${not empty menu2.href ? menu2.href : '/404'}"
							   target="${not empty menu2.target ? menu2.target : 'mainFrame'}" >
								<i class="icon-${not empty menu2.icon ? menu2.icon : 'circle-arrow-right'}"></i>&nbsp;${menu2.name}
							</a>
							<ul class="nav nav-list hide" style="margin:0;padding-right:0;">
								<c:forEach items="${menuList}" var="menu3"><c:if test="${menu3.parent.id eq menu2.id&&menu3.isShow eq '1'}">
								<li class="menu3-${menu2.id} hide">
									<a class="n1203_a" tabName="${menu3.name}" tabId="menu3${menu3.id}" jhref="${fn:indexOf(menu3.href, '://') eq -1 ? ctx : ''}${not empty menu3.href ? menu3.href : '/404'}" target="${not empty menu3.target ? menu3.target : 'mainFrame'}" ><i class="icon-${not empty menu3.icon ? menu3.icon : 'circle-arrow-right'}"></i>&nbsp;${menu3.name}</a></li></c:if>
							</c:forEach>
						</ul>
						</li>
						<c:set var="firstMenu" value="false"/></c:if>
					</c:forEach></ul>
				</div>
		    </div>
		</div>
	</c:if></c:forEach></div><%--
</body>
</html> --%>

<script >
	var element ={};
	var height22=0;
	$(document).ready(function(){
		// 引用layui模块
		layui.use('element', function(){
			element = layui.element;
			height22 =($('#right').height())-45;

			var timestamp = Date.parse(new Date());// 当前时间戳
			// 绑定侧边栏跳转事件
			// var if1=$("#mainFrame");// 右侧iframe
			var idName='menu-'+'${param.parentId}';//
			var idDom=$("#"+idName);
			idDom.on('click','.n1203_a',function(){
				var t=$(this);
				var jhref = t.attr('jhref');
				var tabName = t.attr('tabName');
				var id=t.attr('tabId');
				// id = "r/" + id;
				// if1.attr('src',jhref);
				var obj ={
					jurl:jhref,
					id:tabName,
					tabName:tabName
				};
				n1127_addIframe(obj);
			});

            // alert("222, idName = " + idName);

            idDom.find('.n1203_a').eq(0).click();
            //隐藏关闭图标
            var curLiElem = $(".layui-this");
            var iElem = curLiElem.children(":first");
            if (curLiElem.html().indexOf("我的") > -1) {
                iElem.hide();
            }
        });
    });
    var liElem = $(".layui-tab-title").children(":first");
    // var iElem = liElem.children(":first");
    // iElem.hide();
    // alert("jhref = " + 555 + iElem.html());

    // 添加iframe
	function n1127_addIframe(obj){
		var jurl =obj.jurl||''; // 获取跳转路径
		var id=obj.id||''; // 获取id
		var tabName=obj.tabName||''; // 获取选项卡名称

		// 获取当前页面高度
		//var height =parent.$('body').height()-80;
		// 820px 740px
		// 用于新增一个Tab选项
		// 参数filter：tab元素的 lay-filter="value" 过滤器的值（value）
		// 参数options：设定可选值的对象，目前支持的选项如下述示例：
		// 判断是否已经添加过该选项卡
		var layId_listLen=parent.$("#n1127_demo li[lay-id="+id+"]").length;
		if(layId_listLen>0){// 若已经添加过该选项卡 则不再添加 根据id判断
			// 选项卡切换到当前新添加的选项卡
			element.tabChange('demo', id);
			return false;
		}

		var title = '<a href="http://www.baidu.com" target="">' + tabName + '</a>';
        // alert("888,element = " + element);
        // api https://www.layui.com/doc/modules/element.html
		element.tabAdd('demo', {
			title: tabName
			,content: '<iframe id="mainFrame'+id+'" class="n1127_indexif" name="mainFrame" src="'+jurl+'" style="overflow: visible;" scrolling="yes" frameborder="no" width="100%" height="'+height22+'"></iframe>' //支持传入html
			,id: id
		});
        // alert("111, tabName = " + tabName);

        CustomRightClickMenu(id);//绑定右键菜单

        // alert("999,id = " + id);
        // 选项卡切换到当前新添加的选项卡
		element.tabChange('demo', id);
		//隐藏关闭图标
        var curLiElem = $(".layui-this");
        var iElem = curLiElem.children(":first");
        if (curLiElem.html().indexOf("我的") > -1) {
            iElem.hide();
        }
	}

    //选项卡右键关闭功能
    // var $ = layui.$;
    var deleteIndex;//全局变量
    // element.on('tab(demo)', function (data) {
    //     deleteIndex = $(this).attr("lay-id");
    // });

    var active = {
        //在这里给active绑定几项事件，后面可通过active调用这些事件
        tabAdd: function (url, id, name) {
            //新增一个Tab项 传入三个参数，分别对应其标题，tab页面的地址，还有一个规定的id，是标签中data-id的属性值
            //关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
            element.tabAdd('demo', {
                title: name,
                content: '<iframe data-frameid="' + id + '" scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:99%;"></iframe>',
                id: id //规定好的id
            });
            //以下是新加的******************************************************
            // CustomRightClickMenu(id);//绑定右键菜单
            // FrameWHMenu();  //计算ifram层的大小
        },
        tabChange: function (id) {
            //切换到指定Tab项
            element.tabChange('demo', id); //根据传入的id传入到指定的tab项
            //以下是新加的*************************************************************
            $("iframe[data-frameid='" + id + "']").attr("src", $("iframe[data-frameid='" + id + "']").attr("src"));//切换后刷新框架
        },
        tabDelete: function (id) {
            element.tabDelete("demo", id);//删除
        },
        //以下是新加的******************************************************************
        tabDeleteAll: function (ids) {//删除所有
            $.each(ids, function (i, item) {
                element.tabDelete("demo", item);
            })
        }
        //以上是新增的*********************************************************************
    };

    function FrameWHMenu() {
        var h = $(window).height();
        $("iframe").css("height", h + "px");
    }

    //绑定右键菜单
    function CustomRightClickMenu(id) {
        //取消右键
        $('.layui-tab-title li').on('contextmenu', function () {
            return false;
        });
        $('.layui-tab-title,.layui-tab-title li').click(function () {
            $('.rightmenu').hide();
        });
        //桌面点击右击
        $('.layui-tab-title li').on('contextmenu', function (e) {
            var popupmenu = $(".rightmenu");
            popupmenu.find("li").attr("data-id", id);
            l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
            t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
            popupmenu.css({left: l, top: t}).show();
            return false;
        });
    }

    //就需要给右键添加功能
    $(".rightmenu li").click(function () {
        if ($(this).attr("data-type") == "closeOther") {//关闭其他
            $.each($(".layui-tab-title li[lay-id]"), function () {
                //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                var layId = $(this).attr("lay-id");
                if (layId != deleteIndex && layId.indexOf("我的") == -1) {
                    console.log("mt即将删除lay-id" + $(this).attr("lay-id"));
                    active.tabDelete($(this).attr("lay-id"));
                }
            });
        } else if ($(this).attr("data-type") == "closeall") {//关闭所有
            var tabtitle = $(".layui-tab-title li");
            var ids = new Array();
            $.each(tabtitle, function (i) {
                var layId = $(this).attr("lay-id");
                // if (layId.indexOf("我的") == -1) {
                ids[i] = layId;
                // }
            });
            active.tabDeleteAll(ids);
        }
        $('.rightmenu').hide();
    });

</script>