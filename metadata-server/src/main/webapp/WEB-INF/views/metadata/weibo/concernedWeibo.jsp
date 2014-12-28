<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String analyzerTips = (String) request.getAttribute("AnalyzerTips");
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Site Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="js/weibo/concernedWeiboFormPanel.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/ConcernedWeiboService.js"></script>
        <script type="text/javascript" src="dwr/interface/SiteService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(ConcernedWeiboService.getResults, true);
                var recordType = new Ext.data.Record.create(concernedWeiboRecordType);

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
                //store.setDefaultSort('title', 'desc');
                proxy.on('beforeload', function(thiz, params) {
                    params.match = Ext.getCmp('MATCH').getValue();
                    params.sort = 'updateTime desc';
                });

                

                var siteProxy = new Ext.data.DWRProxy(SiteService.getResults, true);
                var srt = new Ext.data.Record.create(siteRecordType);
                var weibo_type_store = new Ext.data.Store({
                    proxy : siteProxy,
                    reader : new Ext.data.ListRangeReader(
                    {
                        id : 'id',
                        totalProperty : 'totalSize'
                    }, srt
                ),
                    remoteSort: false
                });
                weibo_type_store.load({params :{start : 0, limit : 100, sort : 'id desc', siteType: 'weibo'}});
         
                // the column model has information about grid columns
                // dataIndex maps the column to the specific data field in
                // the data store
                //var nm = new Ext.grid.RowNumberer();
                var fm = Ext.form;
                var sm = new Ext.grid.CheckboxSelectionModel();  // add checkbox column
                var cm = new Ext.grid.ColumnModel([
                    //nm,
                    sm,
                    {
                        header: 'id',
                        dataIndex: 'id',
                        width: 30
                    },
                    {
                        header: '博主名称',
                        dataIndex: 'name',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '类型',
                        dataIndex: 'objectType',
                        width: 50,
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
                        header: '职位',
                        dataIndex: 'title',
                        width: 100,
                        editor: new fm.ComboBox({
                            store : title_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = title_type_store.find('value',value);
                            if(index!=-1){
                                return title_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '网站ID',
                        dataIndex: 'siteId',
                        width: 60,
                        editor: new fm.ComboBox({
                            store : weibo_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'siteName',
                            valueField:'siteId'

                        }),
                        renderer: function(value,metadata,record){
                            var index = weibo_type_store.find('siteId',value);
                            if(index!=-1){
                                return weibo_type_store.getAt(index).data.siteName;
                            }
                            return value;
                        }
                    },
                    {
                        header: '微博地址',
                        dataIndex: 'weiboURL',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '机构',
                        dataIndex: 'org',
                        width: 150,
                        editor: new fm.ComboBox({
                            store : com_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            editable: true,
                            forceSelection: false,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = com_type_store.find('value',value);
                            if(index!=-1){
                                return com_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '粉丝数量',
                        dataIndex: 'fans',
                        width: 50,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '已关注',
                        dataIndex: 'follow',
                        width: 50,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '已发微博',
                        dataIndex: 'weibo',
                        hidden : true,
                        width: 50,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '备注',
                        dataIndex: 'notes',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '认证信息',
                        dataIndex: 'certification',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
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
                    
                    // inline toolbars
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
                        ' ', '关键词', ' ',
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
                            iconCls: 'icon-query',
                            handler: handleQuery
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
                    var addPanel = new DataFormPanel();
                    var AddWin = new Ext.Window({
                        title: '新建记录',
                        width: 400,
                        height: 300,
                        plain: true,
                        items: addPanel,
                        buttons: [{
                                text: '保存',
                                handler: function(){
                                    var p = new recordType();
                                    p.set('name', addPanel.getComponent('fp_name').getValue());
                                    p.set('objectType', addPanel.getComponent('fp_objectType').getValue());
                                    p.set('title', addPanel.getComponent('fp_title').getValue());
                                    p.set('weiboURL', addPanel.getComponent('fp_weiboURL').getValue());
                                    p.set('notes', addPanel.getComponent('fp_notes').getValue());
                                    p.set('org', addPanel.getComponent('fp_org').getValue());
                                    ConcernedWeiboService.insert(p.data, function(){
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
                }


                function handleEdit(){
                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1) {
                            ConcernedWeiboService.update(mr[i].data, function(){
                                store.reload();
                            });
                        } else {
                            ConcernedWeiboService.update(mr[i].data);
                        }
                    }
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    alert("是否要继续删除？");
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            ConcernedWeiboService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            ConcernedWeiboService.deleteRecord(selections[i].data);
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
