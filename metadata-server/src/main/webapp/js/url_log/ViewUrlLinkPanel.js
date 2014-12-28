/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

ViewUrlLinkPanel = function(){
    return new Ext.FormPanel({
        id : 'ViewUrlLinkForm',
        labelAlign: 'left',
        autoScroll: true,
        items: [
        {
            fieldLabel: 'RowKey',
            id: 'fp_rowKey',
            xtype: 'textfield',
            width: '100%',
            height: '8%',
            allowBlank: false
        }, {
            fieldLabel: 'URL',
            id: 'fp_url',
            width: '100%',
            height: '8%',
            xtype: 'textfield',
            allowBlank: false,
            editable: false
        }, {
            fieldLabel: '采集内容',
            id: 'fp_content',
            width: '100%',
            height: '84%',
            xtype: 'textarea',
            allowBlank: true
        }
        ]
    });
};