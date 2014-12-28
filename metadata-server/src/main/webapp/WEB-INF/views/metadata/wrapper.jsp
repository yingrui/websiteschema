<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Wrapper Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="js/wrapper/WrapperEditorPanel.js"></script>
        <script type="text/javascript" src="dwr/interface/WrapperService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                //数据获取代理
                var proxy = new Ext.data.DWRProxy(WrapperService.getResults, true);
                //数据记录
                var recordType = new Ext.data.Record.create(wrapperRecordType);
                //存出了grid所需要的数据
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                    {
                        id : 'id',
                        totalProperty : 'totalSize'
                    }, recordType
                ),
                    remoteSort: false
                });
                
                
                //获取数据之前从页面取得限制条件
                proxy.on('beforeload', function(thiz, params) {
                    params.match = Ext.getCmp('MATCH').getValue();
                    params.type = Ext.getCmp('WRAPPERTYPE').getValue();
                    params.sort = 'updateTime desc';
                });
                
                
                var type_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['功能块','FB'],
                        ['正则表达式','Regex'],
                        ['XSLT','XSLT']
                    ]
                });

                // the column model has information about grid columns
                // dataIndex maps the column to the specific data field in
                // the data store
                //var nm = new Ext.grid.RowNumberer();
                var fm = Ext.form;
                // 创建 checkbox column
                var sm = new Ext.grid.CheckboxSelectionModel();  
                //把一个record对象映射到页面一个列的模型
                var cm = new Ext.grid.ColumnModel([
                    //nm,
                    sm,
                    {
                        header: 'ID',
                        dataIndex: 'id',
                        width: 50
                    },
                    {
                        header: '名称',
                        dataIndex: 'name',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '类型',
                        dataIndex: 'wrapperType',
                        width: 50,
                        hidden : false,
                        editor: new fm.ComboBox({
                            store : type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = type_store.find('value',value);
                            if(index!=-1){
                                return type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '编辑',
                        width: 35,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/cog_edit.png',  // Use a URL in the icon config
                                tooltip: '编辑抽取器',
                                handler: editApplication
                            }
                        ]
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'createTime',
                        width: 200,
                        hidden : true,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    },
                    {
                        header: '创建人',
                        dataIndex: 'createUser',
                        width: 100,
                        hidden : true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '修改时间',
                        dataIndex: 'updateTime',
                        width: 130,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    },
                    {
                        header: '修改人',
                        dataIndex: 'lastUpdateUser',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = false;
                //就是那个数据框对象
                var grid = new Ext.grid.EditorGridPanel({
                    //el:'topic-grid',
                    renderTo: 'gridpanel',
                    width: '100%',
                    autoHeight: true,
                    clicksToEdit:1,
                    autoScroll: true,
                    //title: '分页和排序列表',
                    store: store,
                    trackMouseOver: false,
                    loadMask: true,
                    enableHdMenu: true,
                    sm: sm,
                    cm: cm,
                    
                    //添加工具栏
                    tbar: [ {
                            text: '新建',
                            tooltip: '新建记录',
                            iconCls: 'icon-add',
                            handler: handleAdd
                        }, '-',
                        {
                            text: '提交',
                            tooltip: '提交修改记录',
                            iconCls: 'icon-edit',
                            handler: handleEdit
                        }, '-',
                        {
                            text: '删除',
                            tooltip: '删除记录',
                            iconCls: 'icon-delete',
                            handler: handleDelete
                        }, '->',
                        ' ', '类型', ' ',
                        {
                            xtype: 'combo',
                            id: 'WRAPPERTYPE',
                            width: 100,
                            valueField: 'value',
                            displayField: 'name',
                            mode: 'local',
                            emptyText: '',
                            allowblank: true,
                            forceSelection: false,
                            store: type_store
                        }, ' ',
                        ' ', '名称', ' ',
                        {
                            xtype: 'textfield',
                            id: 'MATCH',
                            initEvents : function(){
                                var keyPressed = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPressed, this);
                            }
                        }, ' ',
                        {
                            text: '检索',
                            handler: handleQuery
                        }, ' ',
                        {
                            text: '清空',
                            handler: function(){
                                Ext.getCmp('WRAPPERTYPE').setValue('');
                                Ext.getCmp('MATCH').setValue('');
                            }
                        }
                    ],
                    bbar: new Ext.PagingToolbar({
                        height: '22',
                        pageSize: pageSize,
                        store: store,
                        displayInfo: true
                    })
                });

                // render it
                grid.render();

                // trigger the data store load
                store.load(
                {
                    params :
                        {
                        start : start,
                        limit : pageSize
                    }
                });
                
                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("name","name_here");
                    p.set("wrapperType","FB");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    WrapperService.insert(p.data);
                    store.reload();
                }

                function handleEdit(){

                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1) {
                            WrapperService.update(mr[i].data, function(){
                                store.reload();
                            });
                        } else {
                            WrapperService.update(mr[i].data);
                        }
                    }
                    
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            WrapperService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            WrapperService.deleteRecord(selections[i].data);
                        }
                    }
                }

                function handleQuery(){
                    store.reload();
                }

                function editApplication(grid, rowIndex, colIndex) {
                    var record= grid.getStore().getAt(rowIndex);
                    if(null != record) {
                        var id = record.data.id;
                        WrapperService.getById(id, function(data){
                            var editPanel = new WrapperEditorFormPanel();
                            Ext.getCmp('fp_name').setValue(data.name);
                            Ext.getCmp('fp_wrapperType').setValue(data.wrapperType);
                            Ext.getCmp('fp_application').setValue(data.application);
                            var AddWin = new Ext.Window({
                                title: '新建记录',
                                width: 800,
                                height: 500,
                                plain: true,
                                items: editPanel,
                                buttons: [{
                                        text: '保存',
                                        handler: function(){
                                            data.name = editPanel.getComponent('fp_name').getValue();
                                            data.wrapperType = editPanel.getComponent('fp_wrapperType').getValue();
                                            data.application = editPanel.getComponent('fp_application').getValue();
                                            WrapperService.update(data, function(){
                                                store.reload();
                                            });
                                            AddWin.close();
                                        }
                                    }, {
                                        text: '取消',
                                        handler: function(){
                                            AddWin.close();
                                        }
                                    }]
                            });
                            AddWin.show(this);
                        });
                    }
                }
            });
        </script>

    </body>
</html>
