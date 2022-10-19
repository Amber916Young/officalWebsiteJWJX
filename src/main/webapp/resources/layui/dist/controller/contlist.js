/**

 @Name：layuiAdmin 内容系统
 @Author：star1029
 
 @License：LPPL
    
 */


layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,admin = layui.admin
  ,view = layui.view
  ,table = layui.table
  ,form = layui.form;

  //文章管理
  table.render({
    elem: '#LAY-app-content-list'
    ,url: '/api/app/content/article/product/queryAll' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 100, title: '文章ID', sort: true}
      // ,{field: 'label', title: '文章标签', minWidth: 100}
      ,{field: 'title', title: '文章标题'}
      ,{field: 'author', title: '作者'}
      ,{field: 'createTime', title: '上传时间', sort: true}
      ,{field: 'publishTime', title: '发布时间', sort: true}

      ,{field: 'status', title: '发布状态', templet: '#buttonTpl', minWidth: 80, align: 'center'}
      ,{title: '操作', minWidth: 150, align: 'center', fixed: 'right', toolbar: '#table-content-list'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-app-content-list)', function(obj){
    var data = obj.data;
    var ids=new Array(),jsonData={};
    ids.push(data.id);
    jsonData["ids"] = ids;
    console.info(jsonData);
    if(obj.event === 'del'){
      layer.confirm('确定删除此文章？', function(index){
          $.ajax({
              type : "POST",
              processData: false,
              contentType: "application/json",
              url: '/api/app/content/articleDelete', //实际使用请改成服务端真实接口
              data : JSON.stringify(jsonData),
              dataType: "json",
              cache: false,
              success : function(result) {
                  if ( result.code==0 ) {
                      table.reload('LAY-app-content-list');
                      obj.del();
                      layer.msg('已删除');
                  } else {
                      layer.msg("上传失败", {time:3000, icon:5});
                  }
              },
              error:function () {
                  layer.msg("ajax请求失败", {time:3000, icon:5});
              }
          });

        layer.close(index);
      });
    } else if(obj.event === 'edit'){
        window.open("/admin#/app/content/articleEdit/id="+data.id)
    }
  });

  //分类管理
  table.render({
    elem: '#LAY-app-content-tags'
    ,url: '/api/app/content/tags' //模拟接口
    ,cols: [[
      {type: 'numbers', fixed: 'left'}
      ,{field: 'id', width: 100, title: 'ID', sort: true}
      ,{field: 'tags', title: '分类名', minWidth: 100}
      ,{title: '操作', width: 150, align: 'center', fixed: 'right', toolbar: '#layuiadmin-app-cont-tagsbar'}
    ]]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-app-content-tags)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
      layer.confirm('确定删除此分类？', function(index){
        obj.del();
        layer.close(index);
      });
    } else if(obj.event === 'edit'){
      admin.popup({
        title: '编辑分类'
        ,area: ['450px', '200px']
        ,id: 'LAY-popup-content-tags'
        ,success: function(layero, index){
          view(this.id).render('app/content/tagsform', data).done(function(){
            form.render(null, 'layuiadmin-form-tags');
            
            //监听提交
            form.on('submit(layuiadmin-app-tags-submit)', function(data){
              var field = data.field; //获取提交的字段

              //提交 Ajax 成功后，关闭当前弹层并重载表格
              //$.ajax({});
              layui.table.reload('LAY-app-content-tags'); //重载表格
              layer.close(index); //执行关闭 
            });
          });
        }
      });
    }
  });

  //评论管理
  table.render({
    elem: '#LAY-app-content-comm'
    ,url: '/resources/layui/start/json/content/comment.js' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 100, title: 'ID', sort: true}
      ,{field: 'reviewers', title: '评论者', minWidth: 100}
      ,{field: 'content', title: '评论内容', minWidth: 100}
      ,{field: 'commtime', title: '评论时间', minWidth: 100, sort: true}
      ,{title: '操作', width: 150, align: 'center', fixed: 'right', toolbar: '#table-content-com'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-app-content-comm)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
      layer.confirm('确定删除此条评论？', function(index){
        obj.del();
        layer.close(index);
      });
    } else if(obj.event === 'edit'){
      admin.popup({
        title: '编辑评论'
        ,area: ['450px', '300px']
        ,id: 'LAY-popup-content-comm'
        ,success: function(layero, index){
          view(this.id).render('app/content/contform', data).done(function(){
            form.render(null, 'layuiadmin-form-comment');
            
            //监听提交
            form.on('submit(layuiadmin-app-com-submit)', function(data){
              var field = data.field; //获取提交的字段

              //提交 Ajax 成功后，关闭当前弹层并重载表格
              //$.ajax({});
              layui.table.reload('LAY-app-content-comm'); //重载表格
              layer.close(index); //执行关闭 
            });
          });
        }
      });
    }
  });

  exports('contlist', {})
});