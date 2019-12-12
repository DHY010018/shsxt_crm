function searchCustomersByParams() {
    $("#dg").datagrid("load",{
        khno:$("#s_khno").val(),
        name:$("#s_name").val(),
        xyd:$("#s_xyd").combobox("getValue"),
        state:$("#s_state").combobox("getValue")
    })
}

function formatterState(val) {
    if(val==0){
        return "未流失"
    }else{
        return "已流失"
    }
}