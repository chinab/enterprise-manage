Ext.ux.SampleGrid = function(limitColumns){

    function italic(value){
        return '<i>' + value + '</i>';
    }

    function change(val){
    	return '<span style="color:red;">' + val + '</span>';
    }

    function pctChange(val){
        if(val < 0){
            return '<span style="color:green;">↓' + (val*(-1)) + '</span>';
        }else if(val > 0){
            return '<span style="color:red;">↑' + val + '</span>';
        }else{
        	return '<span style="color:blue;">-</span>';
        }
        return val;
    }

    var columns = [
        {header: "排名", width: 50, sortable: true, dataIndex: 'price'},
        {id:'company',header: "公司名称", width: 160, sortable: true, renderer:change,dataIndex: 'company'},
        {header: "得分", width: 70, sortable: true, dataIndex: 'change'},
        {header: "名次升降", width: 75, sortable: true, renderer: pctChange, dataIndex: 'pctChange'}//,
        //{header: "Last Updated", width: 85, sortable: true, renderer: Ext.util.Format.dateRenderer('m/d/Y'), dataIndex: 'lastChange'}
    ];

    // allow samples to limit columns
    if(limitColumns){
        var cs = [];
        for(var i = 0, len = limitColumns.length; i < len; i++){
            cs.push(columns[limitColumns[i]]);
        }
        columns = cs;
    }

    Ext.ux.SampleGrid.superclass.constructor.call(this, {
        store: new Ext.data.Store({
            reader: new Ext.data.ArrayReader({}, [
                   {name: 'price'},
                   {name: 'company'},
                   {name: 'change'},
                   {name: 'pctChange', type: 'int'}//,
                   //{name: 'lastChange', type: 'date', dateFormat: 'n/j h:ia'}
              ]),
            data: [
				['第1名','广西分公司','1745.21分',0],  
				['第2名','甘肃发电有限公司','1594.9分',2],  
				['第3名','陕西发电有限公司','1547.35分',-1], 
				['第4名','湖南分公司','1476.18分',-1],  
				['第5名','大唐国际发电股份有限公司','1185.85分',0],  
				['第6名','安徽分公司','913.15分',0] 
			]
        }),
        columns: columns,
        autoExpandColumn: 'company',
        layout:'fit',
        autoWidth:true
        //height:250,
        //width:600
    });

}

Ext.extend(Ext.ux.SampleGrid, Ext.grid.GridPanel);

Ext.ux.SampleGrid1 = function(limitColumns){

    function italic(value){
        return '<i>' + value + '</i>';
    }

    function change(val){
    	return '<span style="color:red;">' + val + '</span>';
    }

    function pctChange(val){
        if(val < 0){
            return '<span style="color:green;">↑' + (val*(-1)) + '</span>';
        }else if(val > 0){
            return '<span style="color:red;">↓' + val + '</span>';
        }else{
        	return '<span style="color:blue;">-</span>';
        }
        return val;
    }

    var columns = [
        {header: "排名", width: 50, sortable: true, dataIndex: 'price'},
        {id:'company',header: "公司名称", width: 160, sortable: true, renderer:change,dataIndex: 'company'},
        {header: "得分", width: 70, sortable: true, dataIndex: 'change'},
        {header: "名次升降", width: 75, sortable: true, renderer: pctChange, dataIndex: 'pctChange'}//,
        //{header: "Last Updated", width: 85, sortable: true, renderer: Ext.util.Format.dateRenderer('m/d/Y'), dataIndex: 'lastChange'}
    ];

    // allow samples to limit columns
    if(limitColumns){
        var cs = [];
        for(var i = 0, len = limitColumns.length; i < len; i++){
            cs.push(columns[limitColumns[i]]);
        }
        columns = cs;
    }

    Ext.ux.SampleGrid1.superclass.constructor.call(this, {
        store: new Ext.data.Store({
            reader: new Ext.data.ArrayReader({}, [
                   {name: 'price'},
                   {name: 'company'},
                   {name: 'change'},
                   {name: 'pctChange', type: 'int'}//,
                   //{name: 'lastChange', type: 'date', dateFormat: 'n/j h:ia'}
              ]),
            data: [
                ['第1名','吉林发电有限公司','2300.61分',0 ], 
				['第2名','黑龙江发电有限公司','2266.47分',0],
				['第3名','安徽分公司','2022.88分',1],
				['第4名','陕西发电有限公司','2012.65分',-1],
				['第5名','河南分公司','1930.95分',0],
				['第6名','大唐国际发电股份有限公司','1894.94分',0  ],
				['第7名','山西分公司','1838.13分',0  ],
				['第8名','江苏分公司','1737.7分',0  ],
				['第9名','河北发电有限公司','1706.08分',1  ],
				['第10名','湖南分公司','1640.92分',-1  ],
				['第11名','甘肃发电有限公司','1613.72分',1  ],
				['第12名','广西分公司','1608.2分',-1 ]
            ]
        }),
        columns: columns,
        autoExpandColumn: 'company',
        layout:'fit',
        autoWidth:true
        //height:250,
        //width:600
    });

}

Ext.extend(Ext.ux.SampleGrid1, Ext.grid.GridPanel);

//EOP