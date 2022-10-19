//voice

function genID(){
    return Number(Math.random().toString().substr(3,6) );
}

//携带json参数的
function postJson(api,param) {
    var loadingIndex=null;
    var res = "";
    $.ajax({
        type: "POST",
        processData: false,
        contentType: "application/json",
        url: api,
        data: param,
        dataType: "json",
        cache: false,
        async: false, // 同步
        beforeSend : function() {
            loadingIndex = layer.msg('处理中', {icon: 16});
        },
        success: function (result) {
            layer.close(loadingIndex);
            console.info(result);
            if (result.errcode == 0 || result.code==0) {
                if(result.errcode==0){
                    if(result.errmsg!=""){
                        layer.msg(result.errmsg, {time: 3000, icon: 6});
                    }
                }else{
                    layer.msg(result.msg, {time: 3000, icon: 6});
                }
            } else {
                if(result.errcode!=0){
                    layer.msg(result.errmsg, {time: 3000, icon: 5});
                }else{
                    layer.msg(result.msg, {time: 3000, icon: 5});
                }
            }
            res= result;
        },
        error: function () {
            layer.msg("ajax请求失败", {time: 3000, icon: 5});
        }
    });
    return res;
}


//不是携带json参数，单独一个参数的   id
function postParam(api,id) {
    var loadingIndex=null;
    var res=""
    $.ajax({
        type: "POST",
        processData: false,
        contentType: "application/json",
        url: api,
        data: id,
        dataType: "json",
        cache: false,
        async: false, // 同步
        beforeSend : function() {
            loadingIndex = layer.msg('处理中', {icon: 16});
        },
        success: function (result) {
            layer.close(loadingIndex);
            if (result.errcode == 0 || result.code==0) {
                if (result.errcode == 0) {
                    if(result.errmsg!=""){
                        layer.msg(result.errmsg, {time: 3000, icon: 6});
                    }
                } else {
                    layer.msg(result.msg, {time: 3000, icon: 6});
                }
            } else {
                if(result.errcode!=0){
                    layer.msg(result.errmsg, {time: 3000, icon: 5});
                }else{
                    layer.msg(result.msg, {time: 3000, icon: 5});
                }
            }
            res= result;
            return res;
        },
        error: function () {
            layer.msg("ajax请求失败", {time: 3000, icon: 5});
        }
    });
    return res;

}




function postJsonNoParam(api) {
    var res = "";
    $.ajax({
        type : "POST",
        processData: false,
        contentType: "application/json",
        url  : api,
        cache: false,
        async: false, // 同步
        success: function (result) {
            if (result.errcode == 0 || result.code==0) {
                if(result.errcode==0){
                    if(result.errmsg!=""){
                        layer.msg(result.errmsg, {time: 3000, icon: 6});
                    }
                } else {
                    layer.msg(result.msg, {time: 3000, icon: 6});
                }
            } else {
                if(result.errcode!=0){
                    layer.msg(result.errmsg, {time: 3000, icon: 5});
                }else{
                    layer.msg(result.msg, {time: 3000, icon: 5});
                }
            }
            res=result
        },
        error: function () {
            layer.msg("ajax请求失败", {time: 3000, icon: 5});
        }
    });
    return res;
}

function getParam(api) {

}

