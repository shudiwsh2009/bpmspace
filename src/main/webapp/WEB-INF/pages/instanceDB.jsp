<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title><sp:message code="instanceDB.page"></sp:message></title>
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
<jsp:include page="navigation.jsp" flush="true"/>
<section id='content'> <!-- 内容页面 -->
<div class='container-fluid'>
<div class='row-fluid' id='content-wrapper'>
<div class='span12'>
<div class='page-header'>
    <h1 class='pull-left'>
    	<i class='icon-edit'></i>
        <span><em><sp:message code="instanceDB.instancerepository"></sp:message></em></span>
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
           	<li><sp:message code="instanceDB.instancemanagement"></sp:message></li>
           	<li class='separator'>
               <i class='icon-angle-right'></i>
           	</li>
           	<li class='active'><sp:message code="instanceDB.instancerepository"></sp:message></li>
       </ul>
   </div>
</div>

<div class='step-content'>
  <form accept-charset="UTF-8" action="" class="form form-horizontal" enctype="multipart/form-data" method="post" style="margin-bottom: 0;">
   <div style="margin:0;padding:0;display:inline">
    <input name="utf8" type="hidden" value="&#x2713;" />
    <input name="authenticity_token" type="hidden" value="CFC7d00LWKQsSahRqsfD+e/mHLqbaVIXBvlBGe/KP+I=" />
   </div>
      
   <hr class='hr-normal' />   
   </form>
</div>  

<!-- ======================instanceDB Tree========================== -->
<div class="row-fluid">


	<div class='span3 box'>
        <div class='box-header'>
            <div class='title'><sp:message code="instanceDB.instancerepository"></sp:message></div>
            <div class='actions'>
                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                </a>
                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                </a>
            </div>
        </div>
         <div class='box-content'>
        	<div class='pull-left'>
                    <a class='btn btn-success fullfill-items' id='btnCreateCatalog'  data-target='#divNewShiLiKu' data-toggle='modal' ><sp:message code="instanceDB.newcatalog"></sp:message></a>
                    <a class='btn btn-danger' id='btnDeleteDB' data-target='#divNewRemoveConfirm' data-toggle='modal' ><sp:message code="instanceDB.removecatalog"></sp:message></a>
            </div>
            <div class='modal hide fade' id='divNewShiLiKu' tabindex='-1'>
				<div class='modal-header'>
					<button class='close' data-dismiss='modal' type='button'>&times;
					</button>
					<h3><sp:message code="instanceDB.newcatalog"></sp:message></h3>
				</div>
				<div class='modal-body'>
					<form id='fmNewShiLiKu' class='form form-horizontal' style='margin-button: 0;'>
						<div class='control-group'>
							<label class='control-label' for='inputText1'><sp:message code="instanceDB.catalogname"></sp:message></label>
							<div class='controls'>
								<input id='shiLiKuName' name="shiLiKuName" placeholder='<sp:message code="instanceDB.catalogname"></sp:message>' type='text' />
							</div>
						</div>
					</form>
				</div>
				<div class='modal-footer'>
					<button class='btn' data-dismiss='modal'><sp:message code="instanceDB.cancel"></sp:message></button>
						<button id='btConfirmType' class='btn btn-primary' onClick="confirmName()"><sp:message code="instanceDB.confirm"></sp:message></button>
				</div>
			</div>
			<!-- remove modal -->
			<div class='modal hide fade' id='divNewRemoveConfirm' tabindex='-1'>
				<div class='modal-header'>
					<button class='close' data-dismiss='modal' type='button'>&times;
					</button>
					<h3><sp:message code="instanceDB.removecatalog"></sp:message></h3>
				</div>
				<div class='modal-body'>
					<div class='controls'>
						<h5><sp:message code="instanceDB.removecheck"></sp:message></h5>
					</div>
				</div>
				<div class='modal-footer'>
					<button class='btn' data-dismiss='modal'><sp:message code="instanceDB.cancel"></sp:message></button>
						<button id='btConfirmRmv' class='btn btn-primary' onClick="confirmRemove()"><sp:message code="instanceDB.confirm"></sp:message></button>
				</div>
			</div>
			<!-- log name input modal -->
			<div class = 'modal hide fade' id ='divNewLogName' tabindex = '-1'>
				<div class = 'modal-header'>
					<button class='close' data-dismiss='modal' type = 'button'>
					</button>
					<h3><sp:message code="instanceDB.uploadlog"></sp:message></h3>
				</div>
				<div class = 'modal-body'>
					<form id='confirmInfo' class='form form-horizontal' style='margin-bottom: 0;' >
						<label class='control-label' for='inputText1'><sp:message code="instanceDB.logname"></sp:message></label>
						<div class='controls'>
							<input id='logName' name="logName"  placeholder='<sp:message code="instanceDB.logname"></sp:message>' type='text' onkeyup="showHint(this.value)"/>
							<span id="txtHint"></span>
						</div>
						
					</form>
				</div>
				<div class = 'modal-footer'>
						<div class = 'controls'>
							<span  id="hintLog"></span>
						</div>
					<button class = 'btn' data-dismiss = 'modal'><sp:message code="instanceDB.cancel"></sp:message></button>
					<button class = 'btn' id='btnNext'  data-target='#divChooseLog' onClick = "LogNameUpload()"><sp:message code="instanceDB.nextstep"></sp:message></button>
				</div>
			</div>
			<!-- log choose modal -->
			<div class = 'modal hide fade' id ='divChooseLog' tabindex = '-1'>
				<div class = 'modal-header'>
					<button class='close' data-dismiss='modal' type = 'button'>
					</button>
					<h3><sp:message code="instanceDB.uploadlog"></sp:message></h3>
				</div>
				<div class = 'modal-body'>
					<div class = 'controls'>
						<span class='btn btn-success fileinput-button'>
                   			<i class='icon-plus icon-white'></i>
                   			<span><sp:message code="instanceDB.addfiles"></sp:message></span>
                   			<input id="fileuploadInstance" data-bfi-disabled='' name='files[]' type='file' multiple  />
                   		</span>
                   		<div id="progress" class="progress">
					   		<div class="bar"  id = "bar" style="width: 0%;"></div>
						</div>
					</div>
				</div>
				<div class = 'modal-footer'>
					<span id = 'upload-files-info' align = 'left'></span>
					<button id = 'btn-upload-finish' class = 'btn' data-dismiss = 'modal' onClick = "cleanModal()"><sp:message code="instanceDB.finish"></sp:message></button>
				</div>
			</div>
            <!--====================== modal finished ======================-->
            <div class='clearfix'></div>
                <hr class='hr-normal' />    
            <div id='instanceDB_tree'></div>
        </div>
    </div>
    <!-- ======================instanceDB Table======================== -->
	<div class='row-fluid' id='detail'>
        <div class='span9 box'>
        <div class='row-fluid' id='detail'>
        <div class='span12 box'>
        	<div class='box-header'>
                <div class='title'><sp:message code="instanceDB.instancelist"></sp:message></div>
                <div class='actions'>
                    <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                    </a>
                    <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                    </a>
                    
                </div>
       		 </div>
            <div class='box-content'>
            
                <div class='pull-left'>
                    <a class='btn btn-success fileinput-button' id='btnChooseLog' data-target='#divNewLogName' data-toggle = 'modal' >
	                    <sp:message code="instanceDB.uploadlog"></sp:message>
                    </a>
                    <a id="moveProcess" class='btn btn-success'><sp:message code="instanceDB.movelog"></sp:message></a>
                    <a id="exportProcess" class='btn btn-success'><sp:message code="instanceDB.exportlog"></sp:message></a>
                </div>
                
                <div class='modal hide fade' id='divyidongmoxing' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="instanceDB.movelog"></sp:message></h3>
						</div>
						<div class='modal-body'>
							<form id='chooseifcopy' class='form form-horizontal' style='margin-bottom: 0;' >	
						  	<div class='control-group'>
								<label class='control-label' for='inputText1'><sp:message code="instanceDB.keeporiginallog"></sp:message></label>
								<div class='controls'>
									<input id='checkbox' name="checkbox" type='checkbox' />
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label' for='inputText1'><sp:message code="instanceDB.selectcatalog"></sp:message></label>
								<div class='controls'>
									<div id='tree3'></div>
								</div>
							</div>
							</form>
							
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="instanceDB.cancel"></sp:message></button>
							<button id='btConfirmMove' class='btn btn-primary' onClick="confirmMove()"><sp:message code="instanceDB.confirm"></sp:message></button>
						</div>
					</div>
					 <!-- 响应移动 -->
					<div class='modal hide fade' id='divresponseForMove' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="instanceDB.moverespondinginfo"></sp:message></h3>
						</div>
						<div id="divresponseForMoveContent" class='modal-body'>
							
							
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="instanceDB.cancel"></sp:message></button>
							<button id='btConfirmMove' class='btn btn-primary' onClick="forceMove()"><sp:message code="instanceDB.confirm"></sp:message></button>
						</div>
					</div>
					
					<!-- 响应删除 -->
					<div class='modal hide fade' id='divresponseForRemove' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="instanceDB.removerespondinginfo"></sp:message></h3>
						</div>
						<div id="divresponseForRemoveContent" class='modal-body'>
							
						</div>
						<div class='modal-footer'>
							<button id='btConfirmMove' class='btn btn-primary' data-dismiss='modal'><sp:message code="instanceDB.confirm"></sp:message></button>
						</div>
					</div>
					
					<div class='modal hide fade' data-backdrop="static" id='waitForResponse' role='dialog'>
						
						<div class="container">
						<div class='row'>
							<img alt="1" src="assets/images/ajax-loaders/19.gif" />
						</div>
						</div>
						
					</div>
					
                <div class='clearfix'></div>
                <hr class='hr-normal' />
                
				<div class='box-content' id='tableOperator'>
	                <table id="instanceTable" class='table table-hover'>
	                    <thead>
	                    
	                    </thead>
	                    <tbody >
	                   
	                    </tbody>
	                </table>
               </div>
                
               
            </div>
        </div>
    </div>
        	
    </div>

    </div>
</div>
</div>
</div>
</div>
</section>
</div>
<style> 
.table-a table td{border:1px solid #F00} 
/* css注释：只对table td标签设置红色边框样式 */ 
</style>
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
<!--  fileupload 
<script src='assets/javascripts/plugins/fileupload/tmpl.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/load-image.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/canvas-to-blob.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.iframe-transport.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload-fp.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload-ui.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload-init.js' type='text/javascript'></script>


<script src='assets/javascripts/plugins/fileupload//cors/jquery.xdr-transport.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/myuploadfunction.js' type='text/javascript'></script>
-->
<!-- / fileupload -->
<script src='assets/javascripts/plugins/fileupload/jquery.iframe-transport.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload/jquery.fileupload.min.js' type='text/javascript'></script>
<script src='assets/javascripts/plugins/fileupload//cors/jquery.xdr-transport.js' type='text/javascript'></script>
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
<!-- <script src='assets/javascripts/index/index.js' type='text/javascript'></script> -->
<!--  <script src='assets/javascripts/index/caseindex.js' type='text/javascript'></script> -->
<!-- <script src='assets/javascripts/ajaxfileupload.js' type='text/javascript' charset="utf-8"></script> -->
<script src='assets/javascripts/instance/instanceDB.js' type='text/javascript'></script>
<!-- <script src='assets/javascripts/instance/input_instance.js' type='text/javascript'></script> -->
<script src='assets/javascripts/instance/instanceLoad.js' type='text/javascript'></script>
</body>
</html>