/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var type_store = new Ext.data.SimpleStore(
{
    fields :['name','value'],
    data:[
    ['功能块','FB'],
    ['正则表达式','Regex'],
    ['XSLT','XSLT']
    ]
});

WrapperEditorFormPanel = function(){
    return new Ext.FormPanel({
        id : 'wrapperEditorForm',
        //        frame: true,
        labelAlign: 'left',
        labelWidth: 130,
        autoScroll: true,
        items: [
        {
            fieldLabel: '抽取器名称',
            hiddenName: 'name',
            id: 'fp_name',
            width: 200,
            height: '6%',
            xtype: 'textfield',
            allowBlank: false
        }, {
            fieldLabel: '类型',
            hiddenName: 'wrapperType',
            id: 'fp_wrapperType',
            width: 200,
            height: '6%',
            xtype: 'combo',
            valueField: 'value',
            displayField: 'name',
            store: type_store,
            //            selectOnFocus: true,
            emptyText: '请选择抽取器类型',
            editable: false,
            mode: 'local',
            forceSelection: true,
            typeAhead: true,
            triggerAction: 'all'
        }, {
            fieldLabel: '配置',
            name: 'application',
            id: 'fp_application',
            width: '100%',
            height: '88%',
            xtype: 'textarea',
            allowBlank: true
        }
        ]
    });
};