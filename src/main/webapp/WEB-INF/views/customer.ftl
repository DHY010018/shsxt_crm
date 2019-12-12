<!--
  客户管理主页面
-->
<!doctype html>
<html>
<head>
    <#include "common.ftl" >
    <script type="text/javascript" src="${ctx}/static/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/base.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/customer.js"></script>
</head>
<body style="margin: 1px">
<table id="dg" class="easyui-datagrid"
       pagination="true" rownumbers="true"
       url="${ctx}/customer/list" fit="true" toolbar="#tb" style="width:700px;height:250px">
    <thead frozen="true">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="50" align="center">编号</th>
        <th field="name" width="200" align="center">客户名</th>
        <th field="khno" width="100" align="center">客户编号</th>
        <th field="area" width="50" align="center">地区</th>
        <th field="cusManager" width="200" align="center">客户经理</th>
        <th field="state" width="200" align="center"  formatter="formatterState">流失状态</th>
    </tr>
    </thead>


    <thead>
    <tr>
        <th field="myd" width="100" align="center">满意度</th>
        <th field="level" width="100" align="center">客户级别</th>
        <th field="xyd" width="200" align="center">信用度</th>
        <th field="address" width="100" align="center">详细地址</th>
        <th field="postCode" width="150" align="center">邮编</th>
        <th field="phone" width="200" align="center">电话</th>
        <th field="web_site" width="200" align="center">网站</th>
        <th field="fax" width="100" align="center">传真</th>
        <th field="fr" width="150" align="center">法人</th>
        <th field="yyzzzch" width="200" align="center">营业执照</th>
        <th field="khyh" width="200" align="center">开户行</th>
        <th field="khzh" width="150" align="center">开户账号</th>
        <th field="gsdjh" width="200" align="center">国税</th>
        <th field="dsdjh" width="200" align="center">地税</th>
        <th field="createDate" width="200" align="center">创建时间</th>
        <th field="updateDate" width="200" align="center">更新时间</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openCustomerAddDialog()" class="easyui-linkbutton" iconCls="icon-add"
           plain="true">创建</a>
        <a href="javascript:openCustomerModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit"
           plain="true">修改</a>
        <a href="javascript:deleteCustomer()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
        <a href="javascript:deleteCustomer()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">联系人管理</a>
        <a href="javascript:deleteCustomer()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">交往记录</a>
        <a href="javascript:deleteCustomer()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">订单查看</a>
    </div>
    <div>
        客户名称： <input type="text" id="s_name" size="20" onkeydown="if(event.keyCode==13) searchCustomersByParams()"/>
        客户编号： <input type="text" id="s_khno" size="20" onkeydown="if(event.keyCode==13) searchCustomersByParams()"/>
        <br/>
        信用度： <select class="easyui-combobox" id="s_xyd" editable="false" panelHeight="auto">
            <option value="">请选择...</option>
            <option value="☆">☆</option>
            <option value="☆☆">☆☆</option>
            <option value="☆☆☆">☆☆☆</option>
            <option value="☆☆☆☆">☆☆☆☆</option>
            <option value="☆☆☆☆☆">☆☆☆☆☆</option>
        </select>
        流失状态： <select class="easyui-combobox" id="s_state" editable="false" panelHeight="auto">
            <option value="">请选择...</option>
            <option value="0">未流失</option>
            <option value="1">已流失</option>
        </select>

        <a href="javascript:searchCustomersByParams()" class="easyui-linkbutton" iconCls="icon-search"
           plain="true">搜索</a>
    </div>
</div>


</body>
</html>
