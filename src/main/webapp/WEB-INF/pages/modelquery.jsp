<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>过程数据空间</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport' />
    
    <!--[if lt IE 9]>
    <script src='assets/javascripts/html5shiv.js' type='text/javascript'></script>
    <![endif]-->
    <link href='assets/stylesheets/bootstrap/bootstrap.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/bootstrap/bootstrap-responsive.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / jquery ui -->
    <link href='assets/stylesheets/jquery_ui/jquery-ui-1.10.0.custom.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/jquery_ui/jquery.ui.1.10.0.ie.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / switch buttons -->
    <link href='assets/stylesheets/plugins/bootstrap_switch/bootstrap-switch.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / xeditable -->
    <link href='assets/stylesheets/plugins/xeditable/bootstrap-editable.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/common/bootstrap-wysihtml5.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / wysihtml5 (wysywig) -->
    <link href='assets/stylesheets/plugins/common/bootstrap-wysihtml5.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / jquery file upload -->
    <link href='assets/stylesheets/plugins/jquery_fileupload/jquery.fileupload-ui.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / full calendar -->
    <link href='assets/stylesheets/plugins/fullcalendar/fullcalendar.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / select2 -->
    <link href='assets/stylesheets/plugins/select2/select2.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / mention -->
    <link href='assets/stylesheets/plugins/mention/mention.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / tabdrop (responsive tabs) -->
    <link href='assets/stylesheets/plugins/tabdrop/tabdrop.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / jgrowl notifications -->
    <link href='assets/stylesheets/plugins/jgrowl/jquery.jgrowl.min.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / datatables -->
    <link href='assets/stylesheets/plugins/datatables/bootstrap-datatable.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / dynatrees (file trees) -->
    <link href='assets/stylesheets/plugins/dynatree/ui.dynatree.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / color picker -->
    <link href='assets/stylesheets/plugins/bootstrap_colorpicker/bootstrap-colorpicker.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / datetime picker -->
    <link href='assets/stylesheets/plugins/bootstrap_datetimepicker/bootstrap-datetimepicker.min.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / daterange picker) -->
    <link href='assets/stylesheets/plugins/bootstrap_daterangepicker/bootstrap-daterangepicker.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / flags (country flags) -->
    <link href='assets/stylesheets/plugins/flags/flags.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / slider nav (address book) -->
    <link href='assets/stylesheets/plugins/slider_nav/slidernav.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / fuelux (wizard) -->
    <link href='assets/stylesheets/plugins/fuelux/wizard.css' media='all' rel='stylesheet' type='text/css' />
    <!-- / flatty theme -->
    <link href='assets/stylesheets/light-theme.css' id='color-settings-body-color' media='all' rel='stylesheet' type='text/css' />
    <!-- / demo -->
    <link href='assets/stylesheets/demo.css' media='all' rel='stylesheet' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
	.row_selected{
	background-color:#49BF67}
</style>
</head>
<body class='contrast-blue '>
<header>
    <jsp:include page="navigation.jsp" flush="true"/>

<section id='content'> </nav><!-- 内容页面 -->
<div class='container-fluid'>
<div class='row-fluid' id='content-wrapper'>
<div class='span12'>
<div class='page-header'>
    <h1 class='pull-left'>
        <i class='icon-edit'></i>
        <span><em>流程管理</em></span>
    </h1>
    <div class='pull-right'>
                <ul class='breadcrumb'>
                    <li>
                        <a href="ProcessManagerIndex.html"><i class='icon-bar-chart'></i>
                        </a>
                    </li>
                    <li class='separator'>
                        <i class='icon-angle-right'></i>
                    </li>
                    <li>模型管理</li>
                    <li class='separator'>
                        <i class='icon-angle-right'></i>
                    </li>
                    <li class='active'>自动生成模型</li>
                </ul>
            </div>
</div>
<div class='alert alert-info'>
    <a class='close' data-dismiss='alert' href='#'>&times;</a>
    	欢迎来到
    <strong>模型检索界面</strong>
    ，你可以使用定制好的索引进行模型高效检索！
</div>
<div class='group-header'>
    <div class='row-fluid'>
        <div class='span6 offset3'>
            <div class='text-center'>
                <h2>利用子句查询</h2>
                <small class='muted'>输入子句比如：</small>
            </div>
        </div>
    </div>
</div>
<div class='row-fluid'>
	
    <div class='span12 box'>
    	
        <div class='box-header blue-background'>
            <div class='title'>
                <div class='icon-edit'></div>
                通过子句查询
            </div>
            <div class='actions'>
                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                </a>
                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                </a>
            </div>
        </div>
        <hr class='hr-normal' />
          
          <div class='row-fluid'>
    		<div class='span12 box'>
       			<div class='box-content'>
           
           
            <div class='clearfix'></div>
          	<form accept-charset="UTF-8" action="#" class="form form-horizontal" method="post" style="margin-bottom: 0;" /><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="CFC7d00LWKQsSahRqsfD+e/mHLqbaVIXBvlBGe/KP+I=" /></div>
               
                <div class='control-group'>
                    <label class='control-label' for='inputTextArea2'>查询语句</label>
                    <div class='controls'>
                        <textarea class='input-block-level' id='query_input' placeholder='Textarea' rows='3'></textarea>
                    </div>
                </div>
                <div class='control-group'>
                    <label class='control-label'>选择索引</label>
                    <div class='controls'>
                        <select class='span4 index_list' id="index_list_1">
                        <!-- 
                            <option />1
                            <option />2
                            <option />3
                            <option />4
                            <option />5
                        -->
                        </select>
                    </div>
                   
                    <div class='text-right'>
                        <div class='btn btn-primary btn-large' id="btn_text_query">
                            <i class='icon-info' ></i>
                            查询
                        </div>
                    </div>
                </div>
                
            </form>
            <hr class='hr-normal' />
            <div class='alert alert-info'>
                <a class='close' data-dismiss='alert' href='#'>&times;</a>
                按照子句条件检查出来的结果将会在下面显示
            </div>
            <div class='row-fluid'>
                <div class='span12 box bordered-box ' style='margin-bottom:0;'>
                <div class='box-header '>
                    <div class='title'>查询结果</div>
                    <div class='actions'>
                        <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                        </a>
                        <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                        </a>
                    </div>
                </div>
                <div class='box-content box-no-padding'>
                <div class='responsive-table'>
                <div class='scrollable-area'>
                <table id="queryResult1" class='table table-hover'>
                    <thead>
                    
                    </thead>
                    <tbody >
                   
                    </tbody>
                </table>
                <!-- 
                <table class='data-table table table-bordered table-striped' style='margin-bottom:0;'>
                <thead>
                <tr>
                    <th>
                        模型名称
                    </th>
                    <th>
                        模型类型
                    </th>
                    <th>
                        创建时间
                    </th>
                     <th>
                        修改时间
                    </th>
                     <th>
                        大小
                    </th>
                     <th>
                        作者
                    </th>
                </tr>
                </thead>
                <tbody id="query_result_1">
                
                <tr>
                    <td>hello</td>
                    <td>hello</td>
                    <td>hello</td>
                    <th>
                        hello
                    </th>
                     <th>
                        hello
                    </th>
                    <td>
                        hello
                    </td>
                    <td>
                        <div class='text-right'>
                            <a class='btn btn-success btn-mini' href='#'>
                                <i class='icon-ok'></i>
                            </a>
                            <a class='btn btn-danger btn-mini' href='#'>
                                <i class='icon-remove'></i>
                            </a>
                        </div>
                    </td>
                </tr>
				 
				 </tbody>
                </table> -->
                </div>
                </div>
                </div>
                </div>
                </div><!-- / 查询结果 -->
        </div>
    </div>
</div><!-- / 这段结束 -->


<div class='group-header'>
    <div class='row-fluid'>
        <div class='span6 offset3'>
            <div class='text-center'>
                <h2>利用图查询</h2>
                <small class='muted'>选择从模型库中还是本地还是手工画.</small>
            </div>
        </div>
    </div>
</div>
<div class='row-fluid'>
    <div class='span12 box'>
    	
        <div class='box-header blue-background'>
            <div class='title'>
                <div class='icon-edit'></div>
                利用图查询
            </div>
            <div class='actions'>
                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                </a>
                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                </a>
            </div>
        </div>
        
 		<div class='row-fluid'>
			<div class='span12 box'>          
                <div class='span2'>
               
                 <div class='btn-group-vertical'>
                     <a class='btn btn-success fullfill-items' data-toggle='modal' href='#selectModelType' role='button'>绘制模型</a>
                    <a class='btn btn-primary btn-large'  style="margin-bottom:5px" id="btnselectfromDB" data-toggle='modal' href="#showModelsInDB" role="button">库中选择 </a>
                    <a class='btn btn-primary btn-large' style="margin-bottom:5px" data-toggle='modal' href='#uploadModelFile' role='button'>本地模型</a>
                </div>
                
                 <div class='modal hide fade' id='selectModelType' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3>请输入想要建立的模型信息</h3>
						</div>
						<div class='modal-body'>
							<form id='confirmInfo' class='form form-horizontal' style='margin-bottom: 0;' >
									<div class='control-group'>
										<label class='control-label'>选择类型</label>
										<div class='controls'>
											<label class='radio'>
											<input type='radio' name='processType' value='BPMN' />
											bmpn2.0
										</label>
										<label class='radio'>
											<input type='radio' name='processType' value='EPC' />
											epc
										</label>
										<label class='radio'>
											<input type='radio' name='processType' value='PETRINET' />
											petrinet
										</label>
										</div>
									</div>
							</form>
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'>Close</button>
							<button class='btn btn-primary' onClick="openDrawWin()" data-dismiss='modal'>Confirm</button>
						</div>
					</div>
                
                  <div class='modal hide fade' id='showModelsInDB' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3>选择模型</h3>
						</div>
						<div class='modal-body'>
							
							<div class='control-group'>
								<label class='control-label' for='inputText1'>选择模型库</label>
								<div class='controls'>
									<div id='modelsInDB'></div>
								</div>
								<table id="modelsTable" class='table table-hover'>
				                    <thead>
				                    
				                    </thead>
				                    <tbody >
				                   
				                    </tbody>
				                </table>
							</div>
							
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'>Close</button>
							<button id='btConfirmMove' class='btn btn-primary' onClick="confirmQueryByModelInDB()">Confirm</button>
						</div>
				 </div>
                 <hr class='hr-normal' />
            </div>
            <div class='span10'>
            	<div class="row-fluid"> 
                 
                    <h1><strong>图片的预留位置</strong></h1>
                
                </div>
               
            </div>
         </div>
         
</div>
          
          <div class='row-fluid'>
    		<div class='span12 box'>
       			<div class='box-content'>
           
           
            <div class='clearfix'></div>
          	<form accept-charset="UTF-8" action="#" class="form form-horizontal" method="post" style="margin-bottom: 0;" /><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="CFC7d00LWKQsSahRqsfD+e/mHLqbaVIXBvlBGe/KP+I=" /></div>
                <div class='control-group'>
                    <label class='control-label'>选择索引</label>
                    <div class='controls'>
                        <select class='span4 index_list' id="index_list_2">
                        	<!-- 
                            <option />1
                            <option />2
                            <option />3
                            <option />4
                            <option />5 -->
                        </select>
                    </div>
                   
                    <div class='text-right'>
                        <div class='btn btn-primary btn-large' id="btn_graph_query">
                            <i class='icon-info'></i>
                            查询
                        </div>
                    </div>
                </div>
                
            </form>
            <hr class='hr-normal' />
            <div class='alert alert-info'>
                <a class='close' data-dismiss='alert' href='#'>&times;</a>
                 按照图条件检查出来的结果将会在下面显示
            </div>
            <div class='row-fluid'>
                <div class='span12 box bordered-box' style='margin-bottom:0;'>
                <div class='box-header'>
                    <div class='title'>检索结果</div>
                    <div class='actions'>
                        <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                        </a>
                        <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                        </a>
                    </div>
                </div>
                <div class='box-content box-no-padding'>
                <div class='responsive-table'>
                <div class='scrollable-area'>
                <table id="queryResult2" class='table table-hover'>
                    <thead>
                    
                    </thead>
                    <tbody >
                   
                    </tbody>
                </table>
                </div>
                </div>
                </div>
                </div>
                </div><!-- / 查询结果 -->
        </div>
    </div>
</div><!--这段的结束-->


				 <div class='modal hide fade' id='uploadModelFile' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3>请上传查询的模型</h3>
						</div>
						<div class='modal-body'>
							<form accept-charset="UTF-8" action="#" class="form form-horizontal" enctype="multipart/form-data" method="post" style="margin-bottom: 0;">
							<div class='span10'>
								<span class='btn btn-success fileinput-button'> 
								<i class='icon-plus icon-white'></i> <span>Add files...</span> 
									<input id="modelfileupload" data-bfi-disabled='' name='files[]' type='file' multiple />
								</span>
								<div id="progress" class="progress">
									<div class="bar" style="width: 0%;"></div>
								</div>

								<table id="uploaded-files" class="table">
									<tr>
										<th>File Name</th>
										<th>File Size</th>
										<th>File Type</th>
									</tr>
								</table>
							</div>
							</form>
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'>Close</button>
							<button class='btn btn-primary'  data-dismiss='modal'>Confirm</button>
						</div>
					</div>
</div>
</div>
</section>
</div>
<!-- / jquery -->
<script src='assets/javascripts/jquery/jquery.min.js' type='text/javascript'></script>
<!-- / jquery mobile events (for touch and slide) -->
<script src='assets/javascripts/plugins/mobile_events/jquery.mobile-events.min.js' type='text/javascript'></script>
<!-- / jquery migrate (for compatibility with new jquery) -->
<script src='assets/javascripts/jquery/jquery-migrate.min.js' type='text/javascript'></script>
<!-- / jquery ui -->
<script src='assets/javascripts/jquery_ui/jquery-ui.min.js' type='text/javascript'></script>
<!-- / bootstrap -->
<script src='assets/javascripts/bootstrap/bootstrap.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/flot/excanvas.js' type='text/javascript'></script>
<!-- / sparklines -->
<script src='assets/javascripts/plugins/sparklines/jquery.sparkline.min.js' type='text/javascript'></script>
<!-- / flot charts -->
<script src='assets/javascripts/plugins/flot/flot.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/flot/flot.resize.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/flot/flot.pie.js' type='text/javascript'></script>
<!-- / bootstrap switch -->
<script src='assets/javascripts/plugins/bootstrap_switch/bootstrapSwitch.min.js' type='text/javascript'></script>
<!-- / fullcalendar -->
<script src='assets/javascripts/plugins/fullcalendar/fullcalendar.min.js' type='text/javascript'></script>
<!-- / fileupload -->
<script src='assets/javascripts/plugins/fileupload/jquery.iframe-transport.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload//cors/jquery.xdr-transport.js' type='text/javascript'></script>

<!-- / page script -->
<script src='assets/javascripts/query/query.js' type='text/javascript'></script>
<script src='assets/javascripts/query/modelquery.js' type='text/javascript'></script>

<!-- / datatables -->
<script src='assets/javascripts/plugins/datatables/jquery.dataTables.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/datatables/jquery.dataTables.columnFilter.js' type='text/javascript'></script>
<!-- / wysihtml5 -->
<script src='assets/javascripts/plugins/common/wysihtml5.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/common/bootstrap-wysihtml5.js' type='text/javascript'></script>
<!-- / select2 -->
<script src='assets/javascripts/plugins/select2/select2.js' type='text/javascript'></script>
<!-- / color picker -->
<script src='assets/javascripts/plugins/bootstrap_colorpicker/bootstrap-colorpicker.min.js' type='text/javascript'></script>
<!-- / mention -->
<script src='assets/javascripts/plugins/mention/mention.min.js' type='text/javascript'></script>
<!-- / input mask -->
<script src='assets/javascripts/plugins/input_mask/bootstrap-inputmask.min.js' type='text/javascript'></script>
<!-- / fileinput -->
<script src='assets/javascripts/plugins/fileinput/bootstrap-fileinput.js' type='text/javascript'></script>
<!-- / modernizr -->
<script src='assets/javascripts/plugins/modernizr/modernizr.min.js' type='text/javascript'></script>
<!-- / retina -->
<script src='assets/javascripts/plugins/retina/retina.js' type='text/javascript'></script>
<!-- / timeago -->
<script src='assets/javascripts/plugins/timeago/jquery.timeago.js' type='text/javascript'></script>
<!-- / slimscroll -->
<script src='assets/javascripts/plugins/slimscroll/jquery.slimscroll.min.js' type='text/javascript'></script>
<!-- / autosize (for textareas) -->
<script src='assets/javascripts/plugins/autosize/jquery.autosize-min.js' type='text/javascript'></script>
<!-- / charCount -->
<script src='assets/javascripts/plugins/charCount/charCount.js' type='text/javascript'></script>
<!-- / validate -->
<script src='assets/javascripts/plugins/validate/jquery.validate.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/validate/additional-methods.js' type='text/javascript'></script>
<!-- / naked password -->
<script src='assets/javascripts/plugins/naked_password/naked_password-0.2.4.min.js' type='text/javascript'></script>
<!-- / nestable -->
<script src='assets/javascripts/plugins/nestable/jquery.nestable.js' type='text/javascript'></script>
<!-- / tabdrop -->
<script src='assets/javascripts/plugins/tabdrop/bootstrap-tabdrop.js' type='text/javascript'></script>
<!-- / jgrowl -->
<script src='assets/javascripts/plugins/jgrowl/jquery.jgrowl.min.js' type='text/javascript'></script>
<!-- / bootbox -->
<script src='assets/javascripts/plugins/bootbox/bootbox.min.js' type='text/javascript'></script>
<!-- / inplace editing -->
<script src='assets/javascripts/plugins/xeditable/bootstrap-editable.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/xeditable/wysihtml5.js' type='text/javascript'></script>
<!-- / ckeditor -->
<script src='assets/javascripts/plugins/ckeditor/ckeditor.js' type='text/javascript'></script>
<!-- / filetrees -->
<script src='assets/javascripts/plugins/dynatree/jquery.dynatree.min.js' type='text/javascript'></script>
<!-- / datetime picker -->
<script src='assets/javascripts/plugins/bootstrap_datetimepicker/bootstrap-datetimepicker.js' type='text/javascript'></script>
<!-- / daterange picker -->
<script src='assets/javascripts/plugins/bootstrap_daterangepicker/moment.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/bootstrap_daterangepicker/bootstrap-daterangepicker.js' type='text/javascript'></script>
<!-- / max length -->
<script src='assets/javascripts/plugins/bootstrap_maxlength/bootstrap-maxlength.min.js' type='text/javascript'></script>
<!-- / dropdown hover -->
<script src='assets/javascripts/plugins/bootstrap_hover_dropdown/twitter-bootstrap-hover-dropdown.min.js' type='text/javascript'></script>
<!-- / slider nav (address book) -->
<script src='assets/javascripts/plugins/slider_nav/slidernav-min.js' type='text/javascript'></script>
<!-- / fuelux -->
<script src='assets/javascripts/plugins/fuelux/wizard.js' type='text/javascript'></script>
<!-- / flatty theme -->
<script src='assets/javascripts/nav.js' type='text/javascript'></script>
<script src='assets/javascripts/tables.js' type='text/javascript'></script>
<script src='assets/javascripts/theme.js' type='text/javascript'></script>
<!-- / demo -->
<script src='assets/javascripts/demo/jquery.mockjax.js' type='text/javascript'></script>
<script src='assets/javascripts/demo/inplace_editing.js' type='text/javascript'></script>
<script src='assets/javascripts/demo/charts.js' type='text/javascript'></script>
<script src='assets/javascripts/demo/demo.js' type='text/javascript'></script>


<!-- / fileupload 
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload-init.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/tmpl.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/load-image.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/canvas-to-blob.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.iframe-transport.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload-fp.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload-ui.min.js' type='text/javascript'></script>
-->
</body>
</html>