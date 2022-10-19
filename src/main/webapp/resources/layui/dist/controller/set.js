/**

 @Name：layuiAdmin 设置
 
 
 @License: LPPL
    
 */
 
layui.define(['form', 'upload'], function(exports){
  var $ = layui.$
  ,layer = layui.layer
  ,laytpl = layui.laytpl
  ,setter = layui.setter
  ,view = layui.view
  ,admin = layui.admin
  ,form = layui.form
  ,upload = layui.upload;

  var $body = $('body');
  
  form.render();
  
  //自定义验证
  form.verify({
    nickname: function(value, item){ //value：表单的值、item：表单的DOM对象
      if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
        return '用户名不能有特殊字符';
      }
      if(/(^\_)|(\__)|(\_+$)/.test(value)){
        return '用户名首尾不能出现下划线\'_\'';
      }
      if(/^\d+\d+\d$/.test(value)){
        return '用户名不能全为数字';
      }
    }
    
    //我们既支持上述函数式的方式，也支持下述数组的形式
    //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
    ,pass: [
      /^[\S]{6,12}$/
      ,'密码必须6到12位，且不能出现空格'
    ]
    
    //确认密码
    ,repass: function(value){
      if(value !== $('#LAY_password').val()){
        return '两次密码输入不一致';
      }
    }
  });
  
  //网站设置
  form.on('submit(set_website)', function(obj){
    layer.msg(JSON.stringify(obj.field));

  });
  
  //邮件服务
  form.on('submit(set_system_email)', function(obj){
    layer.msg(JSON.stringify(obj.field));
    
    //提交修改
    /*
    admin.req({
      url: ''
      ,data: obj.field
      ,success: function(){
        
      }
    });
    */
    return false;
  });
  
  
  //设置我的资料
  form.on('submit(setmyinfo)', function(obj){
    layer.msg(JSON.stringify(obj.field));
    console.info(obj.field);
    var field = obj.field; //获取提交的字段
    var api = "/api/user/worker/newUser/edit";
    postJson(api, JSON.stringify(field))
    form.render();
    setTimeout(function(){
      admin.req({
        url: '/permission/web/user/logout'
        ,type: 'get'
        ,data: {}
        ,done: function(res){
          //清空本地记录的 token，并跳转到登入页
          if(res.code==0){
            layui.data(setter.tableName, {
              key: setter.request.tokenName
              , value: ""
            });
          }
          admin.exit();
          return window.location.href = '/permission/user/login/';
        }
      });
    }, 3000);

  });

  //上传头像
  var avatarSrc = $('#LAY_avatarSrc');
  upload.render({
    url: '/image/user/worker/headImg/upload'
    ,elem: '#LAY_avatarUpload'
    ,accept: 'images'
    ,method: 'get'
    ,acceptMime: 'image/*'
    ,done: function(res){
      if(res.status == 0){
        avatarSrc.val(res.url);
      } else {
        layer.msg(res.msg, {icon: 5});
      }
    }
  });
  
  //查看头像
  admin.events.avartatPreview = function(othis){
    var src = avatarSrc.val();
    layer.photos({
      photos: {
        "title": "查看头像" //相册标题
        ,"data": [{
          "src": src //原图地址
        }]
      }
      ,shade: 0.01
      ,closeBtn: 1
      ,anim: 5
    });
  };
  
  
  //设置密码
  form.on('submit(setmypass)', function(obj){
    layer.msg(JSON.stringify(obj.field));
    var field = obj.field; //获取提交的字段
    var api = "/api/user/worker/newUser/edit";
    postJson(api, JSON.stringify(field))
    form.render();
    setTimeout(function(){
      admin.req({
        url: '/permission/web/user/logout'
        ,type: 'get'
        ,data: {}
        ,done: function(res){
          //这里要说明一下：done 是只有 response 的 code 正常才会执行
          //而 succese 则是只要 http 为 200 就会执行

          //清空本地记录的 token，并跳转到登入页
          if(res.code==0){
            layui.data(setter.tableName, {
              key: setter.request.tokenName
              , value: ""
            });
          }
          admin.exit();
          return window.location.href = '/permission/user/login/';
        }
      });
    }, 3000);
  });
  
  //对外暴露的接口
  exports('set', {});
});