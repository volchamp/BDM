# BDM
基础数据管控平台主要实现对基础数据的管控，平台聚焦于构建高效、全面的基础数据管控平台，以应对企业在数据治理、标准统一、质量监测及整改处理等方面面临的挑战。基础数据管控提供数据管理、基础数据查询、基础数据修改上报与问题反馈、数据共享服务等。
## 功能介绍
### 系统登录
![process](https://raw.githubusercontent.com/volchamp/BDM/master/screenshot/login.png)
### 数据管理
* 主数据管理
* 参考数据管理
* 标准数据管理
* 标准文件管理
### 基础数据申请
![process](https://raw.githubusercontent.com/volchamp/BDM/master/screenshot/home.png)
* 根据系统显示内容填写相关信息，带红色“*”的条目为必填，否则无法提交申请。因业务需求，申请不同类型的基础数据需下载不同的模板。
* 按照模板要求填写调整信息后，完成界面信息填写并上传附件。
### 质量问题查询及反馈
* 系统自动核验问题查询
* 用户反馈问题处理流程查询<br>
![process](https://raw.githubusercontent.com/volchamp/BDM/master/screenshot/process.png)
### 基础数据查询
* 可查看权限范围内的基础数据信息，包括其值集名称、适用范围、数据来源标准，编码、名称、版本、启用起止时间等信息。
* 可查看权限范围内的内设机构信息，支撑根据编码或名称进行搜索，输入信息后点击查询完成搜索。
* 可查看权限范围内的单位信息，同时可输入单位编码或名称进行搜索，输入后点击查询即可完成搜索。
### 机构查询、新增、修改
* 默认展示当前工号权限范围内可见的机构层级树及列表
* 可对当前工号权限范围内可见的机构新增申请
* 当前工号权限范围内可见的机构进行修改处理
### 管控支撑功能介绍
* 管控支撑重点管控各州（市）、县（区）基础数据是否符合规范，业务数据中使用的基础数据是否规范，并提供存在问题的反馈，以便能够有效的定位问题和完善问题。
* 展示权限范围内各区划基础数据校验情况，其中【✓】表示该项验证通过，【✗】表示该项验证未通过，可以点击图标查看校验详情。
### 数据共享服务
* 提供通用的WebAPI接口提供第三方应用进行数据使用。
## 联系我们
[沃创科技](http://www.volchamp.com.cn/)