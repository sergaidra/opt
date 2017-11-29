var xlsForm = Ext.create('Ext.form.Panel', {});		//엑셀 저장용 폼

Ext.define('PurchsInfo', {
	extend: 'Ext.data.Model',
	fields: ['PURCHS_SN', 'ESNTL_ID', 'USER_NM', 'PURCHS_DT', 'PURCHS_DE', 'PURCHS_TM', 'TOT_SETLE_AMOUNT', 'REAL_SETLE_AMOUNT', 'USE_POINT', 'SETLE_AMOUNT', 'SETLE_POINT', 'PYMNT_SE', 'PYMNT_SE_NM', 'TOURIST_NM', 'TOURIST_CTTPC', 'DELETE_AT', 'DELETE_AT_NM', 'DELETE_DT', 'GOODS_NM', 'GOODS_CNT']
});

var comboPymntSe = fn_cmmnCombo('결제구분', 'sch-pymnt-se', 'PYMNT_SE', 'COM006', '', true, 200, 80);

var frPurchs = Ext.create('Ext.form.Panel', {
	id: 'form-sch',
	region: 'north',
	height: 95,
	items: [{
		xtype: 'fieldset',
		title: '검색조건',
		padding: '5 5 5 10',
		items: [{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			items: [{
				xtype: 'hiddenfield',
				id: 'sch-ds-esntl-id',
				name: 'ESNTL_ID',
			}, {
				xtype: 'textfield',
				id: 'sch-ds-user-nm',
				name: 'ESNTL_ID',
				fieldLabel: '사용자정보',
				labelWidth: 80,
				labelAlign: 'right',
				border: false,
				width: 160,
				readOnly: true,
				enableKeyEvents: true,
				listeners: {
					focus: function(tf, e, eOpts ) {
						Ext.getCmp('btn-search-user').fireEvent('click');
					}
				}
			}, {
				xtype: 'textfield',
				id: 'sch-ds-user-id',
				hideLabel : true,
				border: false,
				width: 200,
				margin: '0 0 0 5',
				readOnly: true,
				enableKeyEvents: true,
				listeners: {
					focus: function(tf, e, eOpts ) {
						Ext.getCmp('btn-search-user').fireEvent('click');
					}
				}
			}, {
				xtype: 'button',
				id: 'btn-search-user',
				iconCls: 'icon-search',
				margin: '0 0 0 5',
				listeners: {
					click: function() {
						winUserList.show();
					}
				}
			}, {
				xtype: 'datefield',
				vtype: 'daterange',
				id: 'sch-fr-date',
				name: 'FR_PURCHS_DE',
				format: 'Y-m-d',
				altFormats: 'Y-m-d|Y.m.d|Y/m/d|Ymd',
				fieldLabel: '검색일자',
				labelAlign: 'right',
				labelWidth: 80,
				width: 200,
				allowBlank: false,
				value: new Date(Date.parse(new Date())-6*1000*60*60*24),
				endDateField: 'sch-to-date',
				autoCreate: { tag: 'input', type: 'text', maxLength: '10' },
				style: { 'ime-mode': 'disabled' },
				maskRe: /[0-9]/,
				maxLength: 10,
				enforceMaxLength: true,
				selectOnFocus: true,
				enableKeyEvents: true,
				listeners: {
					'keyup': function(tf, e) {
						var dt = tf.getRawValue().replace(/-/gi,'');
						if(dt.length == 8){
							if(e.getKey() != 13 && e.getKey() != 39 && e.getKey() != 37 && e.getKey() != 8 && e.getKey() != e.TAB){
								tf.setValue(fn_renderDate(dt));
								Ext.getCmp('sch-to-date').focus();
							}
							if(e.getKey() == 13){
								Ext.getCmp('btn-search-log').fireEvent('click');
							}
						}
					}
				}
			}, {
				xtype: 'datefield',
				vtype: 'daterange',
				id: 'sch-to-date',
				name: 'TO_PURCHS_DE',
				format: 'Y-m-d',
				altFormats: 'Y-m-d|Y.m.d|Y/m/d|Ymd',
				fieldLabel: '-',
				labelAlign: 'right',
				labelSeparator: '',
				labelWidth: 10,
				width: 130,
				allowBlank: false,
				value: new Date(),
				startDateField : 'sch-fr-date',
				autoCreate: { tag: 'input', type: 'text', maxLength: '10' },
				style: { 'ime-mode': 'disabled' },
				maskRe: /[0-9]/,
				maxLength: 10,
				enforceMaxLength: true,
				selectOnFocus: true,
				enableKeyEvents: true,
				listeners: {
					'keyup': function(tf, e) {
						var dt = tf.getRawValue().replace(/-/gi,'');
						if(dt.length == 8){
							if(e.getKey() != 13 && e.getKey() != 39 && e.getKey() != 37 && e.getKey() != 8 && e.getKey() != e.TAB){
								tf.setValue(fn_renderDate(dt));
							}
							if(e.getKey() == 13){
								Ext.getCmp('btn-search-log').fireEvent('click');
							}
						}
					}
				}
			}, {
				xtype: 'button',
				id: 'btn-search-log',
				margin: '0 0 0 10',
				text: '조회',
				width: 60,
				listeners: {
					click: function() {
						stPurchs.proxy.extraParams = Ext.getCmp('form-sch').getForm().getValues();
						stPurchs.loadPage(1);

						/*if(!Ext.getCmp('sch-ds-esntl-id').getValue()) {
							Ext.Msg.alert('확인', '사용자를 선택하세요.', function(){
								Ext.getCmp('btn-search-user').fireEvent('click');
							});
						} else {
							stPurchs.proxy.extraParams = Ext.getCmp('form-sch').getForm().getValues();
							stPurchs.loadPage(1);
						}*/
					}
				}
			}, {
				xtype: 'button',
				id: 'btn-excel',
				margin: '0 0 0 10',
				text: '엑셀',
				width: 60,
				listeners: {
					click: function() {
						xlsForm.getForm().standardSubmit = true;
						xlsForm.getForm().submit({
		        			url   : '../selectPurchsListExcel/',
		        			method: 'POST',
		        			params: stPurchs.proxy.extraParams
		        		});
					}
				}			
			},{
				xtype: 'button',
				margin: '0 0 0 5',
				text: '초기화',
				width: 60,
				handler: function(){
					frPurchs.getForm().reset();
					frUser.getForm().reset();
					stPurchs.removeAll();
					stUser.removeAll();
				}
			}]
		}, {
			xtype: 'fieldcontainer',
			layout: 'hbox',
			items: [comboPymntSe, {
				xtype: 'radiogroup',
				id: 'sch-rd-delete-at',
				fieldLabel: '결제취소여부',
				labelWidth: 100,
				labelAlign: 'right',
				border: false,
				width: 300,
				items: [{ boxLabel: '전체', id:'radio-delete-all', name: 'DELETE_AT', inputValue:'', checked: true},
						{ boxLabel: '결제', id:'radio-delete-n', name: 'DELETE_AT', inputValue:'N'},
						{ boxLabel: '취소', id:'radio-delete-y', name: 'DELETE_AT', inputValue:'Y'}]
			}]
		}]
	}]
});

var stPurchs = Ext.create('Ext.data.JsonStore', {
	autoLoad: false,
	pageSize: 20,
	model: 'PurchsInfo',
	groupField: 'PURCHS_DE',
	proxy: {
		type: 'ajax',
		url: '../selectPurchsList/',
		reader: {
			type: 'json',
			root: 'data',
			totalProperty: 'rows'
		}
	}
});

var grPurchs = Ext.create('Ext.grid.Panel', {
	title: '결제목록',
	region:'center',
	store: stPurchs,
	border: true,
	split : true,
	loadMask : true,
	//columnLines: true,
	viewConfig: {
		emptyText: '등록된 자료가 없습니다.',
        getRowClass: function(record, rowIndex, rowParams, store) {
			if (record.get('DELETE_AT') == 'Y') { 
				return 'row_err';
			} else {
				return 'row_not';
			}
	    }
	},
	features: [{
		ftype: 'groupingsummary',
		groupHeaderTpl: '<font color="blue">{columnName} : {name} ( {rows.length} 건 )</font>',
		hideGroupedHeader: true,
		enableGroupingMenu: false
	}, {
		ftype: 'summary',
		dock: 'top'
	}],
	columns: [{
		text: '결제번호',
    	width: 150,
    	align: 'center',
    	dataIndex: 'PURCHS_SN',
        summaryType : function(record) {
			var cntY = 0;
			var cntN = 0;
			for (var idx in record) {
				if(record[idx].data.DELETE_AT == 'Y') cntY++;
				else cntN++;
			}
			var reVal = {
				'cntY' : cntY,
				'cntN' : cntN
			}
			return reVal;
		},
        summaryRenderer: function(value, summaryData, dataIndex) {
        	var str = '결제 ' + value.cntN + '건 , 취소 ' + value.cntY + '건';
        	return '<b><font color="#9F0000">'+str+'</font></b>';
        }
	},{
		text: '구매일시',
		width: 100,
		align: 'center',
		hidden: true,
		dataIndex: 'PURCHS_DT'
	},{
		text: '구매일자',
		width: 100,
		align: 'center',
		hidden: true,
		dataIndex: 'PURCHS_DE'
	},{
		text: '구매시각',
		width: 100,
		align: 'center',
		dataIndex: 'PURCHS_TM'
	},{
		text: '구매자',
		width: 100,
		align: 'center',
		dataIndex: 'USER_NM'
	},{
		text: '결제금액',
		width: 100,
		style: 'text-align:center',
		align: 'right',
		dataIndex: 'TOT_SETLE_AMOUNT',
		renderer: function(value, metaData, record) {
			return Ext.util.Format.number(value , '0,000');
		}
	},{
		text: '실결제금액',
		width: 100,
		style: 'text-align:center',
		align: 'right',
		dataIndex: 'SETLE_AMOUNT',
		renderer: function(value, metaData, record) {
			return Ext.util.Format.number(record.data.REAL_SETLE_AMOUNT , '0,000');
		},
		summaryType : 'sum',
		summaryRenderer: function(value, summaryData, dataIndex) {
			return '<b><font color="#9F0000">'+Ext.util.Format.number(value , '0,000')+'</font></b>';
		}
	},{
		text: '사용포인트',
		width: 100,
		style: 'text-align:center',
		align: 'right',
		dataIndex: 'USE_POINT',
		renderer: function(value, metaData, record) {
			return Ext.util.Format.number(value , '0,000');
		},
		summaryType : 'sum',
		summaryRenderer: function(value, summaryData, dataIndex) {
			return '<b><font color="#9F0000">'+Ext.util.Format.number(value , '0,000')+'</font></b>';
		}
		
	},{
		text: '결제수단',
		width: 100,
		align: 'center',
		dataIndex: 'PYMNT_SE_NM'
	},{
		text: '여행자',
		width: 100,
		align: 'center',
		dataIndex: 'TOURIST_NM'
	},{
		text: '여행자연락처',
		width: 150,
		align: 'center',
		dataIndex: 'TOURIST_CTTPC'
	},{
		text: '취소여부',
		width: 100,
		align: 'center',
		dataIndex: 'DELETE_AT_NM'
	},{
		text: '취소일시',
		width: 150,
		align: 'center',
		dataIndex: 'DELETE_DT'
	},{
		text: '구매상품',
		width: 300,
		style: 'text-align:center',
		align: 'left',
		dataIndex: 'GOODS_NM',
		renderer: function(value, metaData, record) {
			if(record.data.GOODS_CNT != '0') return value + ' 외 ' + record.data.GOODS_CNT + '건';
			else return value;
		}
	}],
	bbar: Ext.create('Ext.PagingToolbar', {
		store: stPurchs,
		displayInfo: true,
		displayMsg: '전체 {2}건 중 {0} - {1}',
		emptyMsg: "조회된 자료가 없습니다."
	})
});

Ext.onReady(function(){
	Ext.create('Ext.Viewport', {
		layout: 'border',
		padding:'5 10 5 10',
		style: {
			backgroundColor: '#FFFFFF'
		},
		items: [frPurchs, grPurchs]
	});
});