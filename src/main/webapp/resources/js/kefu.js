function genID(){
    return Number(Math.random().toString().substr(3,6) );
}
function getQueryVariable(variable){
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}
var lockReconnect = false;  //避免ws重复连接
var socket=null;
var Tomid=null;

function conncetWebSocket(layim,connectUrl,mid) {
    try {
        if ('WebSocket' in window) {
            socket = new WebSocket(connectUrl);
            Tomid = mid;
        }
        initEventHandle(layim,connectUrl);
    } catch (e) {
        reconnect(connectUrl);
        console.log(e);
    }
}


function initEventHandle(layim,connectUrl) {
    //连接成功时触发
    socket.onopen = function(){
        layer.msg("通信连接成功!", {time: 3000, icon: 6});
        heartCheck.reset().start();      //心跳检测重置
        console.log("websocket");
    };
    // 连接发生错误的回调方法
    socket.onerror = function () {
        reconnect(connectUrl);
        layer.msg("通讯连接失败!", {time: 3000, icon: 5});
    };
    // 连接关闭的回调方法
    socket.onclose = function (e) {
        connectInfo(layim);
        reconnect(connectUrl);
        console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean)
        layer.msg("通讯关闭连接!", {time: 3000, icon: 0});

    };
    //监听收到的消息
    socket.onmessage = function(res){
        heartCheck.reset().start();      //拿到任何消息都说明当前连接是正常的
        var data = eval("("+res.data+")");
        if(data.type=="heart"){
            console.info("心跳包");

        }else{
            handleMessage(layim,data);
        }
    };
}


function reconnect(url) {
    if(lockReconnect) return;
    lockReconnect = true;
    setTimeout(function () {     //没连接上会一直重连，设置延迟避免请求过多
        conncetWebSocket(url);
        lockReconnect = false;
    }, 2000);
}
//心跳检测
var heartCheck = {
    timeout: 60000,        //1分钟发一次心跳
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function(){
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function(){
        var self = this;
        this.timeoutObj = setTimeout(function(){
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            var jsonData ={};
            jsonData["type"]="heart";
            jsonData["msg"]="ping";
            jsonData["toId"]=Tomid;
            socket.send(JSON.stringify(jsonData));
            self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                socket.close();     //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
}

function connectInfo(layim) {
    layim.setChatStatus('<span style="color:#e20005;">客服链接已断开，请重新进入</span>');
    // layim.getMessage({
    //    system: true
    //    ,id: res.data.id
    //    ,type: "friend"
    //    ,content: ''
    // });
}

function unreadMsg(layim,mid) {
    var jsonData2 = {};
    jsonData2["toId"] =mid;
    var api2='/kefu/getMessage/unread'
    var result = postJson(api2,JSON.stringify(jsonData2));
    if (result.code == 0) {
        //需要查看是否有未读消息
        var dataList = result.data.data;
        $.each(dataList,function(i,item){
            handleMessageUnread(layim,item);
        });
    }
}
function handleMessage(layim,msg){
    var data = msg.data;
    var type = msg.type; //类型  kefu
    var mine = data.mine;
    var to = data.to;
    var content =mine.content;
    var id =to.id.toString();
    var username =mine.username;
    var avatar =mine.avatar;
    var fromid =mine.id;
    var timestamp = new Date();
    // console.info("正常的消息发送"+JSON.parse(msg))
    layim.getMessage({
        username: username //消息来源用户名
        ,avatar: avatar //消息来源用户头像
        ,id: fromid //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
        ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
        ,content: content //消息内容
        ,mine: false //是否我发送的消息，如果为true，则会显示在右方
        // ,fromid: fromid//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
        ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
    });
}
var flag = 0;
var copy ={};
function changeUserInfo(layim,msg){
    var content =msg.fromContent;
    var fromid =msg.fromId;
    var ismineId=msg.ismineId;
    var username =msg.fromUsername;
    var avatar =msg.fromAvatar;
    var timestamp = msg.sendTime;
    var tid = msg.transferId;
    if(ismineId==msg.fromId){
        var toType = msg.toType;
        var chat = layui.data('layim-mobile')[tid];
        var history = chat.history[toType+ismineId];
        copy.username=username;
        copy.avatar=avatar;
        copy.id=ismineId;
        copy.name=username;
        copy.sign="以上为转发消息";
        // history.fromid=fromid;
        flag=1;
        // var topChat = layui.data.layimChat;
        // var cont = topChat.find('.layim-chat');
        var s2 = $.extend(true,history,copy);
        console.info(history)

        console.info(s2)

    }


}
function handleHistory(layim,msg,mid){
    var content =msg.fromContent;
    var fromid =msg.fromId;
    var toId = msg.toId;
    var username =msg.fromUsername;
    var avatar =msg.fromavatar;
    var timestamp = msg.timestamp;
    if(mid==msg.fromId){
        layui.data.sendHistory(msg)
        // layim.getMessage({
        //     username: username //消息来源用户名
        //     ,avatar: avatar //消息来源用户头像
        //     ,id: fromid //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
        //     ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
        //     ,content: content //消息内容
        //     ,mine: true //是否我发送的消息，如果为true，则会显示在右方
        //     // ,fromid: fromid//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
        //     ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
        // });

    }else{
        layim.getMessage({
            username: username //消息来源用户名
            ,avatar: avatar //消息来源用户头像
            ,id: fromid //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
            ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
            ,content: content //消息内容
            ,mine: false //是否我发送的消息，如果为true，则会显示在右方
            // ,fromid: toId//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
            ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
        });
    }

}
function handleTransferMessageinit(layim,msg){
    var ismineId=msg.mid;
    var username =msg.name;
    var avatar =msg.headImgurl;
    var timestamp = new Date();
        layim.getMessage({
            username: username //消息来源用户名
            ,avatar: avatar //消息来源用户头像
            ,id: ismineId //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
            ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
            ,content: "有转发消息，请查看" //消息内容
            ,mine: false //是否我发送的消息，如果为true，则会显示在右方
            // ,fromid: fromid//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
            ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
        });



}
function handleTransferMessageUnread(layim,msg){
    var content =msg.fromContent;
    var fromid =msg.fromId;
    var ismineId=msg.ismineId;
    var username =msg.fromUsername;
    var avatar =msg.fromAvatar;
    var timestamp = msg.sendTime;
    var tid = msg.transferId;
    if(ismineId==msg.fromId){
        layim.getMessage({
            username: username //消息来源用户名
            ,avatar: avatar //消息来源用户头像
            ,id: ismineId //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
            ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
            ,content: content //消息内容
            ,mine: false //是否我发送的消息，如果为true，则会显示在右方
            // ,fromid: fromid//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
            ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
        });

    }
    else{
        layim.getMessage({
            username: username //消息来源用户名
            ,avatar: avatar //消息来源用户头像
            ,id: ismineId //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
            ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
            ,content: content //消息内容
            ,mine: true //是否我发送的消息，如果为true，则会显示在右方
            // ,fromid: fromid//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
            ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
        });
    }

}
function handleMessageUnread(layim,msg){
    var content =msg.fromContent;
    var fromid =msg.fromId;
    var username =msg.fromUsername;
    var avatar =msg.fromAvatar;
    var timestamp = msg.sendTime;
    layim.getMessage({
        username: username //消息来源用户名
        ,avatar: avatar //消息来源用户头像
        ,id: fromid //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
        ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
        ,content: content //消息内容
        // ,mine: false //是否我发送的消息，如果为true，则会显示在右方
        // ,fromid: fromid//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
        ,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
    });
}
function postJsonVoice(api,param) {
    var loadingIndex = null;
    $.ajax({
        type: "POST",
        processData: false,
        contentType: "application/json",
        url: api,
        data: param,
        dataType: "json",
        cache: false,
        async: false, // 同步
        beforeSend: function () {
            loadingIndex = layer.msg('处理中', {icon: 16});
        },
        success: function (result) {
            layer.close(loadingIndex);
            console.info(result);
            if (result.errcode == 0) {
                var voice = result.data;
                var voiceUrl = voice.AudioUrl;
                var fileName = voice.fileName;
                var voiceTime = voice.voiceTime;

                var jsonArry = "audio[" + voiceUrl + "]";

                layui.data.tempVoiceMsg(jsonArry,voiceTime);
                layui.data.sendVoiceMsg("voiceMsg");
            } else {
                layer.msg(result.errmsg, {time: 3000, icon: 5});
            }
        },
        error: function () {
            layer.msg("ajax请求失败", {time: 3000, icon: 5});
        }
    });
}
function uploadRecord(localId,Speaktime) {
    wx.uploadVoice({
        localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
            var serverId = res.serverId; // 返回音频的服务器端ID
            var jsonData = {};
            jsonData["serverId"] = serverId;
            jsonData["Speaktime"] = Speaktime;
            var api2='/wx/upload/record'
           postJsonVoice(api2,JSON.stringify(jsonData));
        }
    })
}


var START,Speaktime,localId, END=0;
function initVoice() {


    // 用localStorage进行记录，之前没有授权的话，先触发录音授权，避免影响后 续交互
    if (!localStorage.allowRecord || localStorage.allowRecord !== 'true') {
        wx.startRecord({
            success: function() {
                localStorage.allowRecord = 'true';
                // 仅仅为了授权，所以立刻停掉
                wx.stopRecord();
            },
            cancel: function() {
                layer.msg("用户拒绝授权录音", {time: 2000, icon: 2});
            }
        });
    }


    // $('#voiceInput').on('click', function (e) {
    //     var jsonData = {};
    //     jsonData["serverId"] = "cNmkz0zirhWayjQZqxke2nuuxX-ZGnNtih7t3YyR_3BJZo8jw_6owx3Dm8lxDYcM";
    //     jsonData["Speaktime"] = "1830";
    //     var api2='/wx/upload/record'
    //     postJsonVoice(api2,JSON.stringify(jsonData));
    // })

    wx.ready(function () {
        var localId;
        $('#voiceInput').on('touchstart', function (e) {
            e.preventDefault();
            wx.startRecord({
                success: function () {
                    START = new Date().getTime();
                    wx.onVoiceRecordEnd({
                        // 录音时间超过一分钟没有停止的时候会执行 complete 回调
                        complete: function (res) {
                            layer.msg("最多只能录制一分钟", {time: 2000, icon: 2});
                            localId = res.localId;
                            uploadRecord(localId,60000);
                        }
                    });
                },
                cancel: function () {
                    layer.msg("用户拒绝授权录音", {time: 2000, icon: 2});
                    return false;
                }
            });
        })

        $('#voiceInput').on('touchend', function () {
            END = new Date().getTime();
            Speaktime = END - START;
            if (Speaktime < 1000) {
                END = 0;
                START = 0;
                wx.stopRecord({});
                layer.msg("录音时间不能少于1秒", {time: 2000, icon: 2});
                return false;
            } else {
                wx.stopRecord({
                    success: function (res) {
                        localId = res.localId;
                        uploadRecord(localId,Speaktime);
                    }
                });
            }
        })

        //监听录音自动停止接口
        wx.onVoiceRecordEnd({
            //录音时间超过一分钟没有停止的时候会执行 complete 回调
            complete: function (res) {
                localId = res.localId;
                // alert(localId);
                // wx.translateVoice({
                //     localId: localId, // 需要识别的音频的本地Id，由录音相关接口获得
                //     isShowProgressTips: 1, // 默认为1，显示进度提示
                //     success: function (res) {
                //         // alert(res.translateResult);
                //         result = res.translateResult;
                //         //去掉最后一个句号
                //         result = result.substring(0, result.length - 1);
                //         var searchUrl = "/mobile/search?utf8=✓&search_type=product&text=" + result;
                //         window.location.href = searchUrl;
                //     }
                // });
            }
        });
        wx.error(function (res) {
            console.info(res);
            alert(res);
        });
    });
}
//wx sdk
function initWx(){
    var api = "/wx/initJSSDK/config";
    var json={};
    json["current_url"] =decodeURIComponent(window.location.href);
    var res = postJson(api,JSON.stringify(json));
    if(res.errcode==0){
        var data = res.data;
        var timestamp = parseInt(data.timestamp);
        var nonceStr =data.noncestr;
        var signature =data.signature;
        wx.config({
            debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: 'wx66861739dbbd59b1', // 必填，公众号的唯一标识
            timestamp: timestamp, // 必填，生成签名的时间戳
            nonceStr: nonceStr, // 必填，生成签名的随机串
            signature: signature,// 必填，签名
            jsApiList: ["startRecord"
                ,"stopRecord"
                ,"onVoiceRecordEnd"
                ,"playVoice"
                ,"pauseVoice"
                ,"stopVoice"
                ,"onVoicePlayEnd"
                ,"uploadVoice"
                ,"downloadVoice"
                ,"translateVoice"
                ,"openLocation"
            ] // 必填，需要使用的JS接口列表
        });



    }

}