/**

 @Name：layuiAdmin 工单系统
 @Author：star1029
 
 @License：GPL-2
    
 */


layui.define(['table', 'form', 'element'], function(exports){
  var $ = layui.$
  ,admin = layui.admin
  ,view = layui.view
  ,table = layui.table
  ,form = layui.form
  ,element = layui.element;

  table.render({
    elem: '#LAY-app-workorder'
    ,url: '/message/message/queryAll?type=NO'
    ,cols: [[
      {type: 'numbers', fixed: 'left'}
      ,{field: 'id', width: 100, title: '工单号', sort: true}
      ,{field: 'type', width: 100, title: '业务性质'}
      ,{field: 'title', width: 200, title: '工单标题'}
      ,{field: 'process', title: '进度', width: 160, align: 'center', templet: '#progressTpl'}
      ,{field: 'nickName', width: 70, title: '提交者'}
      ,{field: 'createTime', width: 100, title: '提交时间'}
      ,{field: 'processor', width: 80, title: '受理人员'}
      ,{field: 'status', title: '工单状态', templet: '#buttonTpl', minWidth: 80, align: 'center'}
      ,{title: '操作', align: 'center', fixed: 'right', toolbar: '#table-system-order'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
    ,done: function(){
      element.render('progress');
    }
  });

  //监听工具条
  table.on('tool(LAY-app-workorder)', function(obj){
    var data = obj.data;
    if(obj.event === 'edit'){
      admin.popup({
        title: '编辑工单'
        ,area: ['450px', '450px']
        ,id: 'LAY-popup-workorder-add'
        ,success: function(layero, index){
          view(this.id).render('app/workorder/listform').done(function(){
            form.render(null, 'layuiadmin-form-workorder');
            
            //监听提交
            form.on('submit(LAY-app-workorder-submit)', function(data){
              var field = data.field; //获取提交的字段

              //提交 Ajax 成功后，关闭当前弹层并重载表格
              //$.ajax({});
              layui.table.reload('LAY-app-workorder'); //重载表格
              layer.close(index); //执行关闭 
            });
          });
        }
      });
    }
  });

  exports('workorder', {})
});