var start = 0;
var pageSize = 10;
Ext.onReady(function(){
    Ext.QuickTips.init();
    var proxy = new Ext.data.DWRProxy(SysConfService.getResults, true);
    var recordType = new Ext.data.Record.create(sysConfRecordType);
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

    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store
    // var nm = new Ext.grid.RowNumberer();
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
            header: '域',
            dataIndex: 'field',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '参数名',
            dataIndex: 'name',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '参数值',
            dataIndex: 'value',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '描述',
            dataIndex: 'description',
            width: 200,
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
        height: 530,
        clicksToEdit:1,
        autoScroll: true,
        //title: '分页和排序列表',
        store: store,
        trackMouseOver: false,
        loadMask: true,
        enableHdMenu: false,
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
        }, '-',
        {
            text: '检查状态',
            tooltip: '检查服务器状态',
            iconCls: 'icon-edit',
            handler: checkStatus
        }, '-',
        {
            text: '更新配置',
            tooltip: '更新metadata-server和slaves的配置',
            iconCls: 'icon-edit',
            handler: updateConfig
        }, '-',
        {
            text: '导出采集报告',
            tooltip: '最近两天采集情况报告',
            iconCls: 'icon-edit',
            handler: exportReport
        }, '->',
        ' ', '用户名', ' ',
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
        p.set("field","field");
        p.set("name","name");
        p.set("value","passwd");
        store.insert(0, p);
        grid.startEditing(0, 0);
        SysConfService.insert(p.data, function(){
            store.reload();
        });
    }


    function handleEdit(){
        var mr = store.getModifiedRecords();
        for(var i=0;i<mr.length;i++){
            if(i == mr.length - 1) {
                SysConfService.update(mr[i].data, function(){
                    store.reload();
                });
            } else {
                SysConfService.update(mr[i].data);
            }
        }
    }

    //删除数据
    function handleDelete(){
        var selections = grid.selModel.getSelections();
        for (var i = 0,len = selections.length; i < len; i++) {
            if(i == len - 1) {
                SysConfService.deleteRecord(selections[i].data, function(){
                    store.reload();
                });
            } else {
                SysConfService.deleteRecord(selections[i].data);
            }
        }
    }

    function handleQuery(){
        store.reload();
    }

    function checkStatus(){
        var win;
        SystemCheckService.checkStatus(function(data){
            if(!win) {
                
                win = new Ext.Window({
                    layout:'fit',
                    width:600,
                    height:480,
                    plain: true,

                    items: new Ext.Panel({
                        preventBodyReset: true,
                        renderTo: 'status-panel',
                        width: 400,
                        html: data
                    }),

                    buttons: [{
                        text: 'Close',
                        handler: function(){
                            win.close();
                        }
                    }]
                });
            }
            win.show(this);
        });
    }

    //删除数据
    function updateConfig(){
        SysConfService.updateConfig(function(ret){
            MsgTip.msg("更新配置", ret, true, 5);
        });
    }

    function exportReport() {
        if(Ext.isIE) {
            window.open('/rest/report/','_blank');
        } else {
            window.open('/rest/report/','_newtab');
        }
    }
});