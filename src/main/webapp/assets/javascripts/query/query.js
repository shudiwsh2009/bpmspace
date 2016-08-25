String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {    
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if(args[key]!=undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                	var reg= new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};

function loadIndexList(category){
	 $.ajax({
         cache: true,
         type: "POST",
         url:'index/il',
         data:{cat:category},
         async: true,
         error: function(request) {
             alert("Connection error");
         },
         success: function(data) {
         	if(data.state == "SUCCESS"){
         		var list = eval(data.list);
         		var htmlContent1 = "";
         		var htmlContent2 = "";
         		var i = 0;
         		while(i < list.length){
         			var item = list[i];
         			
         			if(item.supportedQueryType == "graph"){
         				htmlContent2 += "<option value='"+item.id+"'>" + item.class_name;
         			}else if(item.supportedQueryType == "text"){
         				htmlContent1 += "<option value='"+item.id+"'>" + item.class_name;
         			}
         			i++;
         		}
         	   $("#index_list_1").html(htmlContent1);
         	   $("#index_list_2").html(htmlContent2);
         	} else {
         		alert(data.message);
         	}
         	
         }
     });
}


function load_query_result(actionUrl,eleID,iid, query){
	var nCloneTh = document.createElement('th');
	var nCloneTd = document.createElement('td');
	nCloneTd.className = "center";
	oTable = $('#'+eleID).dataTable({
		"bDestroy" : true,
		"bProcessing" : true,
		"sAjaxSource" : actionUrl,
		"fnServerParams" : function(aoData) {
			aoData.push({
				"name" : "iid",
				"value" : iid
			});
			aoData.push({
				"name" : "q",
				"value" : query
			});
		},
		"sServerMethod" : "POST",
		"aoColumns" : [{
			"bSearchable" : false,
			"sTitle" : "模型名称",
			"sClass" : "center"
		}, {
			"sTitle" : "模型类型",
			"sClass" : "center"
		}, {
			"sTitle" : "创建时间",
			"sClass" : "center"
		}, {
			"sTitle" : "修改时间",
			"sClass" : "center"
		}, {
			"sTitle" : "大小",
			"sClass" : "center"
		}, {
			"sTitle" : "作者",
			"sClass" : "center"
		} ],
		"fnInitComplete" : function() {
			$('#instanceTable thead tr').each(function() {
				this.insertBefore(nCloneTh, this.childNodes[0]);
			});
		},
		"fnCreatedRow" : function(nRow, aData, iDataIndex) {
		},
		"fnDrawCallback" : function() {
			$('#newtable tbody tr').each(
				function() {
					this.childNodes[0].Abbr = "defined";
				});
			$('.details').each(
				function() {
					this.parentNode.childNodes[0].Abbr = "defined";
				});
			$('#'+eleID+' tbody tr').each(
				function() {
					if (this.childNodes[0].Abbr != "defined") {
					
						this.insertBefore(nCloneTd.cloneNode(true),
								this.childNodes[0]);
						this.childNodes[0].Abbr = "defined";
					}
					
			});
			$('#'+eleID+" tbody tr").unbind("click");
			$('#'+eleID+" tbody tr").click(function(e) {
				 if ( $(this).hasClass('row_selected') ) {
				 $(this).removeClass('row_selected'); } else {
				 $(this).addClass('row_selected'); }
			});
			$('#'+eleID+" tbody td").dblclick(
				function(e) {
					processIdforrename = oTable.fnGetData(oTable
							.fnGetPosition(this)[0], 0);
				});

			$('.editable1111').editable({
				url : 'null',
				type : 'text',
				pk : 1,
				mode : 'inline',
				toggle : 'null',
				name : 'newname',
				// value:newname 传给后台的值
				title : 'Change process name',
				params : function(params) {
					// originally params contain pk, name and value
					params.processId = processIdforrename;
					return params;
				},
				validate: function(value) {
				    return "unsopported";
				},
				success : function(response, newValue) {
					if (response.state == 'FAIL'){
					}
				}

			});
		},

	});
}




function showQueryResult(ele, l){
	var list = eval(l);
	var content = "";
	var i = 0;
	var templete = "<tr><td style='width:100px;'>{0}</td><td style='width:50px;' >{1}</td><td style='width:100px;' >{2}</td><td style='width:100px;' >{3}</td><td style='width:50px;'>{4}</td><td style='width:50px;'>{5}</td></tr>";
	while(i < list.length){
		var item = list[i];
		content += templete.format(item.mn,item.type,item.ct,item.mt,item.size,item.creator);
		i++;
	}
	ele.html(content);	
}
