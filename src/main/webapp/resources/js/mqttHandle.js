function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

//参数n为休眠时间，单位为毫秒:
function sleep(n) {

    var start = new Date().getTime();
    //  console.log('休眠前：' + start);
    while (true) {
        if (new Date().getTime() - start > n) {
            break;
        }
    }
    // console.log('休眠后：' + new Date().getTime());
}

var client = "";
var options = {};
var topic = "";
var s ="";
var openid="";
var regNO="";
var subTopic=new Array()
function  initMqtt(t) {
    var hostname = 'mqtt.jwjxinfo.com',
        port = 61614,
        timeout = 5,
        keepAlive = 100,
        cleanSession = true,
        ssl = false;
    topic = t
    client = new Paho.MQTT.Client(hostname, port, openid);
    //建立客户端实例
    options = {
        invocationContext: {
            host: hostname,
            port: port,
            clientId: openid
        },
        timeout: timeout,
        reconnect: true,
        keepAliveInterval: keepAlive,
        cleanSession: cleanSession,
        useSSL: ssl,
        onSuccess: onConnect,
        onFailure: function (e) {
            console.log(JSON.stringify(e));
            s = "{time:" + new Date().format("yyyy-MM-dd HH:mm:ss") + ", onFailure()}";
            console.log(s);
            layer.msg("连接失败", {time: 3000, icon: 5});
            $("#queryBtn1").css('display',"none");
            $("#queryBtn2").css('display',"block");
            // var ui = "000"+guid();
            // this.invocationContext.clientId = ui;
            // setCookie("uid", ui, 2 * 60 * 60);
            // sleep(10000);
            // delCookie("uid");
            // window.location.reload();
        }
    };
    client.connect(options);

    client.onConnectionLost = onConnectionLost;

    //注册连接断开处理事件
    client.onMessageArrived = onMessageArrived;
}
//连接服务器并注册连接成功处理事件
function onConnect() {
    console.log("onConnected:" + client.isConnected());
    s = "{time:" + new Date().format("yyyy-MM-dd HH:mm:ss") + ", onConnected()}";
    console.log(s);
    client.subscribe(topic);
    layer.msg("连接成功", {time: 3000, icon: 6});
    $("#queryBtn2").css('display',"none");
    $("#queryBtn1").css('display',"block");
}
function subscribeTopic(topicList) {
    this.client.subscribe(topicList);
    console.info(topicList+"订阅"+ new Date().format("yyyy-MM-dd HH:mm:ss"))
}
function unsubscribeTopic(topicList) {
    var opt={
        onSuccess(){
            console.log("退订成功")
        },
        onFailure(error){
            console.log("退订失败")
            console.log(error)
        }
    }
    this.client.unsubscribe(topicList,opt);
    console.info(topicList+"取消订阅"+ new Date().format("yyyy-MM-dd HH:mm:ss"))
}

//注册消息接收处理事件
function onConnectionLost(responseObject) {
    console.log(responseObject);
    s = "{time:" + new Date().format("yyyy-MM-dd HH:mm:ss") + ", onConnectionLost()}";
    console.log(s);
    console.log("onConnected:" + client.isConnected());
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
        console.log("连接已断开,自动重连中。。");
        sleep(10000);
        window.location.reload();
    }
}

function onMessageArrived(message) {
    s = "{time:" + new Date().format("yyyy-MM-dd HH:mm:ss") + ", onMessageArrived()}";
    console.log(s);
    console.log("收到消息:" + message.payloadString);
    var obj = null;
    try {
        obj = JSON.parse(message.payloadString);
    } catch (e) {
        console.log(e);
        console.log("消息格式异常：" + message.payloadString);
        return;
    }
}
function sendMsg(checkArry) {
    var tmp="";
    for(var i in checkArry){
        if(checkArry.length==parseInt(i)+1){
            tmp +=checkArry[i]
        }else{
            tmp +=checkArry[i]+"&"
        }

    }
    var obj = {"request":tmp};
    console.info(obj);
    var message = new Paho.MQTT.Message(JSON.stringify(obj));
    message.destinationName = "LANconnect/"+regNO+"/push/0/"+openid+"/systemMaintenance";
    message.qos = 2;
    message.retained = false;
    client.send(message);
}

Date.prototype.format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[
            k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}