<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Scheduler Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        
        
        
        <script type="text/javascript" src="js/wrapper/SchedulerEditorPanel.js"></script>
        <script type="text/javascript" src="dwr/interface/SchedulerService.js"></script>
    </head>

    <body>             
        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
           Ext.onReady(function(){ 
             Ext.QuickTips.init();
             var proxy = new Ext.data.DWRProxy(SchedulerService.getResults,true);
             var recordType = new Ext.data.Record.create(schedulerRecordType);
              var store = new Ext.data.Store({                  
                  proxy:proxy,
                  reader:new Ext.data.ListRangeReader({
                      id:'id',
                      totalProperty:'totalSize'
                      
                  },recordType
              ),
                  remoteSort:false
                  
              });
               
                              
               proxy.on('beforeload', function(thiz, params) {
                    params.match = Ext.getCmp('MATCH').getValue();
                    params.address = Ext.getCmp('ADDRESS').getValue(); 
                    params.sort = 'updateTime desc';
                });
               
               var fm = Ext.form;
               var sm = new Ext.grid.CheckboxSelectionModel();       
               var cm = new Ext.grid.ColumnModel([
                sm,
                {
                    header:'ID',
                    dataIndex:'id',
                    width:50
                },
                {
                    header:'名称',
                    dataIndex:'name',
                    width:150,
                    editor: new fm.TextField({
                        allowBlank:false
                    })
                },
                {
                    header:'地址',
                    dataIndex:'address',
                    width:50,
                    editor: new fm.TextField({
                        allowBlank:false
                    })
                },
                {
                    header:'编辑',
                    width:35,
                    xtype:'actioncolumn',
                    items:[
                        {
                            icon   : 'resources/icons/fam/cog_edit.png',  // Use a URL in the icon config
                            tooltip: '编辑抽取器',
                            handler: editApplication
                        }
                    ]
                },
                {
                    header:'创建时间',
                    dataIndex:'createTime',
                    width:200,
                    hidden:true,
                    editor: new fm.DateField({
                        allowBlank: false,
                        readOnly : true,
                        format: 'Y-m-d H:i:s'
                    })
                     
                },
                {
                    header:'创建人',
                    dataIndex:'createUser',
                    width:100,
                    hidden:true,
                    editor:new fm.TextField({
                        allowBlank:false
                    })
                },
                {
                    header:'修改时间',
                    dataIndex:'updateTime',
                    width:130,
                    editor: new fm.DateField({
                         allowBlank: false,
                         readOnly : true,
                          format: 'Y-m-d H:i:s'
                     })
                  
                },
                {
                    header:'修改人',
                    dataIndex:'lastUpdateUser',
                    width:100,
                    editor:new fm.TextField({
                        allowBlank:false
                    })
                    
                }
               ]);               
               var grid = new Ext.grid.EditorGridPanel({
                   renderTo: 'gridpanel',
                   width: '100%',
                   autoHeight: true,
                   clicksToEdit:1,
                   autoScroll:true,
                   store:store,
                   trackMouseOver:false,
                   loadMask:true,
                   enableHdMenu:true,
                   sm:sm,
                   cm:cm,
                
                   tbar:[{
                       text:'新建',    
                       tooltip:'新建记录',
                       iconCls: 'icon-add',
                       handler:handleAdd
                   },'-',
                   {
                       text:'提交',
                       tooltip:'提交修改记录',
                       iconCls:'icon-edit',
                       handler:handleEdit
                       
                   },'-',
                   {
                       text:'删除',
                       tooltip:'删除记录',
                       iconCls:'icon-delete',
                       handler:handleDelete
                   },'->',
                   ' ','名称',' ',
                   {
                       xtype:'textfield',
                       id:'MATCH',
                       initEvents:function(){
                           var keyPressed= function(e){
                               
                               if(e.getKey()==e.ENTER){
                                   handleQuery();
                               }
                           };
                          this.el.on("keypress",keyPressed,this); 
                       }
                   },' ',
                   ' ','地址',' ',
                   {
                       xtype:'textfield',
                       id:'ADDRESS',
                       initEvents:function(){
                       var keyPressed= function(e){
                          if(e.getKey()==e.ENTER){
                              handleQuery();
                             }
                           };
                          this.el.on("keypress",keyPressed,this); 
                       }
                       
                   },' ',
                   {
                       text:'检索',
                       handler:handleQuery
                   },' ',
                   {
                       text:'清空',
                       handler:function(){
                           Ext.getCmp('MATCH').setValue('');
                           Ext.getCmp('ADDRESS').setValue('');
                       }
                   }
               ],
               bbar:new Ext.PagingToolbar({
                   height:'22',
                   pageSize:pageSize,
                   store:store,
                   displayInfo:true
                   
               })
                });              
      
               grid.render();
                
                store.load(
                    {
                        params:
                            {
                            start : start,
                            limit : pageSize
                            }
                    }
                );
                function editApplication(grid, rowIndex, colIndex){
                    var record=grid.getStore().getAt(rowIndex);
                    if(null !=record){
                        var id = record.data.id;
                        SchedulerService.getById(id,function(data){
                            var editPanel = new SchedulerEditorFormPanel();
                            Ext.getCmp('sch_name').setValue(data.name);
                            Ext.getCmp('sch_address').setValue(data.address);
                            
                            var AddWin = new Ext.Window({
                                title:'新建记录',
                                width:400,
                                height:125,
                                plain:true,
                                items:editPanel,
                                buttons:[{
                                        text:'保存',
                                        handler:function(){
                                            data.name=editPanel.getComponent('sch_name').getValue();
                                            data.address=editPanel.getComponent('sch_address').getValue();
                                            SchedulerService.update(data,function(){
                                             store.reload();
                                            });
                                            AddWin.close();
                                        }
                                },{
                                    text:'取消',
                                    handler:function(){
                                        AddWin.close();
                                    }
                                }]
                            });
                            AddWin.show(this);
                            
                        });
                        
                    }
                }
                function handleAdd(){
                    var p = new recordType();                     
                    p.set("name","name_here");
                    p.set("address","address_here");                    
                    store.insert(0,p);
                    SchedulerService.insert(p.data);
                    store.reload();                    
                }
                function handleEdit(){
                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1){
                            SchedulerService.update(mr[i].data,function(){
                                store.reload();
                            });
                        }else{
                            SchedulerService.update(mr[i].data);
                        }
                    }
                }
                
                 function handleDelete(){             
                    var dr = grid.selModel.getSelections();                     
                    for(var i=0;i<dr.length;i++){                        
                         if(i == dr.length -1){
                            SchedulerService.deleteRecord(dr[i].data,function(){
                                store.reload();
                            });
                         }else{
                            SchedulerService.deleteRecord(dr[i].data);  
                         }
                    }
                 
                 }
                 
                 function handleQuery(){
                    store.reload();
                 }
           });
            
        </script>

    </body>
</html>
