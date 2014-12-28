var start = 0;
var pageSize = 10;
Ext.onReady(function(){
    Ext.QuickTips.init();
    var proxy = new Ext.data.DWRProxy(CipherService.getResults, true);
    var recordType = new Ext.data.Record.create(cipherRecordType);
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
    proxy.on('beforeload', function(thiz, params) {
        params.match = Ext.getCmp('MATCH').getValue();
        params.username = Ext.getCmp('USERNAME').getValue();
        params.sort = 'createTime desc';
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
            header: '网站ID',
            dataIndex: 'siteId',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '用户名',
            dataIndex: 'username',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '密码',
            dataIndex: 'password',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: 'Cookie',
            dataIndex: 'cookie',
            width: 200,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: 'Header',
            dataIndex: 'header',
            width: 200,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '编辑',
            width: 40,
            xtype: 'actioncolumn',
            items: [
            {
                icon   : 'resources/icons/fam/cog_edit.png',  // Use a URL in the icon config
                tooltip: '编辑任务配置',
                handler: editJob
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
        }, '->',
        ' ', '网站ID', ' ',
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
        },' ', '用户名', ' ',
        {
            xtype: 'textfield',
            id: 'USERNAME',
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
        var data = p.data;
        var editPanel = new AddCipherPanel();
        var AddWin = new Ext.Window({
            title: '新建记录',
            width: 400,
            height: 400,
            plain: true,
            items: editPanel,
            buttons: [{
                text: '保存',
                handler: function(){
                    data.siteId = editPanel.getComponent('fp_siteId').getValue();
                    data.username = editPanel.getComponent('fp_username').getValue();
                    data.password = editPanel.getComponent('fp_password').getValue();
                    data.cookie = editPanel.getComponent('fp_cookie').getValue();
                    data.header = editPanel.getComponent('fp_header').getValue();
                    CipherService.insert(data, function(){
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
                CipherService.update(mr[i].data, function(){
                    store.reload();
                });
            } else {
                CipherService.update(mr[i].data);
            }
        }
    }

    //删除数据
    function handleDelete(){
        var selections = grid.selModel.getSelections();
        for (var i = 0,len = selections.length; i < len; i++) {
            if(i == len - 1) {
                CipherService.deleteRecord(selections[i].data, function(){
                    store.reload();
                });
            } else {
                CipherService.deleteRecord(selections[i].data);
            }
        }
    }

    function handleQuery(){
        store.reload();
    }

    function editJob(grid, rowIndex, colIndex) {
        var job= grid.getStore().getAt(rowIndex);
        if(null != job) {
            var id = job.data.id;
            CipherService.getById(id, function(data){
                var editPanel = new AddCipherPanel();
                Ext.getCmp('fp_id').setValue(data.id);
                Ext.getCmp('fp_siteId').setValue(data.siteId);
                Ext.getCmp('fp_username').setValue(data.username);
                Ext.getCmp('fp_password').setValue(data.password);
                Ext.getCmp('fp_cookie').setValue(data.cookie);
                Ext.getCmp('fp_header').setValue(data.header);
                var AddWin = new Ext.Window({
                    title: '新建记录',
                    width: 600,
                    height: 350,
                    plain: true,
                    items: editPanel,
                    buttons: [{
                        text: '保存',
                        handler: function(){
                            data.siteId = editPanel.getComponent('fp_siteId').getValue();
                            data.username = editPanel.getComponent('fp_username').getValue();
                            data.password = editPanel.getComponent('fp_password').getValue();
                            data.cookie = editPanel.getComponent('fp_cookie').getValue();
                            data.header = editPanel.getComponent('fp_header').getValue();
                            CipherService.update(data, function(){
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