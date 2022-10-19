/**

 @Name：layuiAdmin 用户管理 管理员管理 角色管理
 @Author：star1029
 
 @License：LPPL
    
 */


layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,admin = layui.admin
  ,view = layui.view
  ,table = layui.table
  ,form = layui.form;

  //用户管理
  table.render({
    elem: '#LAY-user-manage'
    ,url: '/resources/layui/start/json/useradmin/webuser.js' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 100, title: 'ID', sort: true}
      ,{field: 'username', title: '用户名', minWidth: 100}
      ,{field: 'avatar', title: '头像', width: 100, templet: '#imgTpl'}
      ,{field: 'phone', title: '手机'}
      ,{field: 'email', title: '邮箱'}
      ,{field: 'sex', width: 80, title: '性别'}
      ,{field: 'ip', title: 'IP'}
      ,{field: 'jointime', title: '加入时间', sort: true}
      ,{title: '操作', width: 150, align:'center', fixed: 'right', toolbar: '#table-useradmin-webuser'}
    ]]
    ,page: true
    ,limit: 30
    ,height: 'full-320'
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-user-manage)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
      layer.prompt({
        formType: 1
        ,title: '敏感操作，请验证口令'
      }, function(value, index){
        layer.close(index);
        
        layer.confirm('真的删除行么', function(index){
          obj.del();
          layer.close(index);
        });
      });
    } else if(obj.event === 'edit'){
      admin.popup({
        title: '编辑用户'
        ,area: ['500px', '450px']
        ,id: 'LAY-popup-user-add'
        ,success: function(layero, index){
          view(this.id).render('user/user/userform', data).done(function(){
            form.render(null, 'layuiadmin-form-useradmin');
            
            //监听提交
            form.on('submit(LAY-user-front-submit)', function(data){
              var field = data.field; //获取提交的字段

              //提交 Ajax 成功后，关闭当前弹层并重载表格
              //$.ajax({});
              layui.table.reload('LAY-user-manage'); //重载表格
              layer.close(index); //执行关闭 
            });
          });
        }
      });
    }
  });
  //=============================================================================================
  //值班表！！！！！！2021 4 6
  table.render({
    elem: '#LAY-user-manage-duty'
    ,url: '/api/duty/queryAll' // 获取值班表 值班人员
    ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
    ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
      title: '提示'
      ,layEvent: 'LAYTABLE_TIPS'
      ,icon: 'layui-icon-tips'
    }]
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'dutyNumber', title: '客服号', minWidth: 100,sort: true}
      ,{field: 'kf_account', title: '微信客服号', minWidth: 100,sort: true}
      ,{field: 'openid', title: 'openid', minWidth: 100,sort: true}
      ,{field: 'username', title: '用户名', minWidth: 100}
      ,{field: 'gender', title: '性别', minWidth: 100}
      ,{field: 'phone', title: '手机'}
      ,{field: 'email', title: '邮箱'}
      ,{field: 'dutyTime', title: '值班日期', sort: true}
      ,{title: '操作', width: 150, align:'center', fixed: 'right', toolbar: '#table-useradmin-webuser'}
    ]]
    ,page: true
    ,limit: 30
    ,height: 'full-320'
    ,text: '对不起，加载出现异常！'
  });

  //=============================================================================================





  //管理员管理
  table.render({
    elem: '#LAY-user-back-manage'
    ,url: '/api/useradmin/mangadmin/queryAll' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 80, title: 'ID', sort: true}
      ,{field: 'username', title: '用户名'}
      ,{field: 'phone', title: '手机'}
      ,{field: 'adminName', title: '角色'}
      ,{field: 'jobPosition', title: '职位'}
      // ,{field: 'jointime', title: '加入时间', sort: true}
      ,{field: 'check', title:'审核状态', templet: '#buttonTpl', minWidth: 80, align: 'center'}
      ,{title: '操作', width: 150, align: 'center', fixed: 'right', toolbar: '#table-useradmin-admin'}
    ]]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-user-back-manage)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
      layer.prompt({
        formType: 1
        ,title: '敏感操作，请验证口令'
      }, function(value, index){
        layer.close(index);
        layer.confirm('确定删除此管理员？', function(index){
          console.log(obj)
          obj.del();
          layer.close(index);
        });
      });
    }else if(obj.event === 'edit'){
      admin.popup({
        title: '编辑管理员'
        ,area: ['420px', '80%']
        ,id: 'LAY-popup-user-add'
        ,success: function(layero, index){
          view(this.id).render('user/administrators/adminform', data).done(function(){

            form.render(null, 'layuiadmin-form-admin');
            //监听提交
            form.on('submit(LAY-user-back-submit)', function(data){
              var field = data.field; //获取提交的字段


              layui.table.reload('LAY-user-back-manage'); //重载表格
              layer.close(index); //执行关闭 
            });
          });
        }
      });
    }
  });

  //角色管理
  table.render({
    elem: '#LAY-user-back-role'
    ,url: '/api/useradmin/role/queryAll' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 80, title: 'ID', sort: true}
      ,{field: 'rolename', title: '角色名'}
      ,{field: 'limits', title: '拥有权限'}
      ,{field: 'descr', title: '具体描述'}
      ,{title: '操作', width: 300, align: 'center', fixed: 'right', toolbar: '#table-useradmin-admin'}
    ]]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-user-back-role)', function(obj){
    var data = obj.data;
    console.info(data)
    if(obj.event === 'del'){
      layer.confirm('确定删除此角色？', function(index){
        var api = "/permission/role/delete";
        //删除角色，对应的权限，已经用户角色表要删除，此时，用户无菜单显示
        if(data.id==2||data.id==1){
          layer.msg("默认角色不可删除", {time: 3000, icon: 0});
        }else {
          postParam(api, data.id)
          layui.table.reload('LAY-user-back-role'); //重载表格
        }
        layer.close(index);
      });
    }else if(obj.event === 'edit'){
      admin.popup({
        title: '修改角色信息'
        ,area: ['500px', '80%']
        ,id: 'LAY-popup-user-add'
        ,success: function(layero, index){
          view(this.id).render('user/administrators/roleform', data).done(function(){
            form.render(null, 'layuiadmin-form-role');
            //监听提交
            form.on('submit(LAY-user-role-submit)', function(data){
              var field = data.field; //获取提交的字段
              var api = "/permission/web/role/edit";
              if(data.field.permission==null||data.field.permission==""){
                layer.msg("当前角色权限为空", {time: 2000, icon: 5, shift: 6});
                return;
              }
              postJson(api, JSON.stringify(field))
              layui.table.reload('LAY-user-back-role'); //重载表格
              layer.close(index); //执行关闭 
            });
          });
        }
      });
    }else if(obj.event=="assgin"){
      admin.popup({
        title: '分配用户'
        ,area: ['auto', '60%']
        ,id: 'LAY-popup-user-add'
        ,success: function(layero, index){
          view(this.id).render('user/administrators/assginform',data).done(function(){
            form.render(null, 'layuiadmin-form-role');
            //监听提交
            form.on('submit(LAY-user-role-submit)', function(data){
              var field = data.field; //获取提交的字段
              var api = "/permission/web/user/assgin/add";
              console.info(field)
              postJson(api, JSON.stringify(field))
              layui.table.reload('LAY-user-back-role'); //重载表格
              layer.close(index); //执行关闭
            });
          });
        }
      });
    }
  });

  exports('useradmin', {})
});