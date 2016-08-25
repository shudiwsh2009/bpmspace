<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>


<!DOCTYPE html>
<html>
<head>
    <title><sp:message code="modelDB.page"></sp:message></title>
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
<!-- 导航结束 -->
<section id='content'> </nav><!-- 内容页面 -->
<div class='container-fluid'>
<div class='row-fluid' id='content-wrapper'>
<div class='span12'>
<div class='page-header'>
    <h1 class='pull-left'>
        <i class='icon-cloud'></i>
        <span><em><sp:message code="modelDB.modelrepository"></sp:message></em></span>
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
                    <li><sp:message code="modelDB.modelmanagement"></sp:message></li>
                    <li class='separator'>
                        <i class='icon-angle-right'></i>
                    </li>
                    <li class='active'><sp:message code="modelDB.modelrepository"></sp:message></li>
                </ul>
            </div>
</div>
<div class='alert alert-info'>
    <a class='' data-dismiss='alert' href='#'>&times;</a>
    <sp:message code="modelDB.welcome"></sp:message>
    <strong><sp:message code="modelDB.modelrepository"></sp:message></strong>
    <sp:message code="modelDB.description"></sp:message>
</div>


<div class='row-fluid'>

<div class='span3 box'>
        <div class='box-header'>
            <div class='title'><sp:message code="modelDB.modelcatalog"></sp:message></div>
            <div class='actions'>
                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                </a>
                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                </a>
            </div>
        </div>
         <div class='box-content'>
        	<div class='pull-left'>
                    <a class='btn btn-success fullfill-items' id='btnCreateDB'  data-toggle='modal' role='button'><sp:message code="modelDB.newcatalog"></sp:message></a>	
                    <a class='btn btn-success' id='btnDeleteDB' href='#'><sp:message code="modelDB.removecatalog"></sp:message></a>
                </div>
                  <div class='modal hide fade' id='divNewMoXingKu' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="modelDB.inputcataloginfo"></sp:message></h3>
						</div>
						<div class='modal-body'>
							<form id='fmNewMoXingKu' class='form form-horizontal' style='margin-bottom: 0;' >
								
								  	<div class='control-group'>
										<label class='control-label' for='inputText1'><sp:message code="modelDB.catalogname"></sp:message></label>
										<div class='controls'>
											<input id='processKuName' name="processKuName"  placeholder='<sp:message code="modelDB.catalogname"></sp:message>' type='text' />
											</div>
									</div>
							</form>
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="modelDB.cancel"></sp:message></button>
							<button id='btConfirmType' class='btn btn-primary' onClick="confirmKuName()"><sp:message code="modelDB.confirm"></sp:message></button>
						</div>
					</div>
					
                <div class='clearfix'></div>
                <hr class='hr-normal' />
           
            <div id='tree2'></div>
        </div>
    </div> 
    <div class='span9 box'>
        <div class='row-fluid' id='detail'>
        <div class='span12 box'>
        	<div class='box-header'>
                <div class='title'><sp:message code="modelDB.modellist"></sp:message></div>
                <div class='actions'>
                    <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                    </a>
                    <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                    </a>
                    
                </div>
       		 </div>
            <div class='box-content'>
            
                <div class='pull-left'>
                	<a class='btn btn-success fullfill-items' data-toggle='modal' href='#importModelType' role='button'><sp:message code="modelDB.importmodel"></sp:message></a>
                    <a class='btn btn-success fullfill-items' data-toggle='modal' href='#selectModelType' role='button'><sp:message code="modelDB.newmodel"></sp:message></a>
                    <a id="deleteProcess" class='btn btn-success'><sp:message code="modelDB.removemodel"></sp:message></a>
                    <a id="moveProcess" class='btn btn-success'><sp:message code="modelDB.movemodel"></sp:message></a>
                    <a id="exportProcess" class='btn btn-success'><sp:message code="modelDB.exportmodel"></sp:message></a>
                </div>
                <!-- <div class='pull-right'>
                    <a class='btn' href='#'>取消</a>
                </div> -->
                
                 <div class='modal hide fade' id='importModelType' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="modelDB.importmodelinfo"></sp:message></h3>
						</div>
						<div class='modal-body'>
							<form id='confirmInfoforimport' class='form form-horizontal' style='margin-bottom: 0;' >
								
								  	<div class='control-group'>
										<label class='control-label' for='inputText1'><sp:message code="modelDB.modelname"></sp:message></label>
										<div class='controls'>
											<input  name="processName"  placeholder='<sp:message code="modelDB.modelname"></sp:message>' type='text' />
											</div>
									</div>
									 <div class='control-group'>
										<label class='control-label' for='inputTextArea1'><sp:message code="modelDB.modeldescription"></sp:message></label>
										<div class='controls'>
											<textarea  name="processDescription" placeholder='<sp:message code="modelDB.modeldescription"></sp:message>' rows='3'></textarea>
										</div>
									</div>
									<div class='control-group'>
										<label class='control-label'><sp:message code="modelDB.modeltype"></sp:message></label>
										<div class='controls'>
											<label class='radio'>
											<input type='radio' name='processType' value='BPMN' />
											<sp:message code="modelDB.bpmn"></sp:message>
										</label>
										<label class='radio'>
											<input type='radio' name='processType' value='EPC' />
											<sp:message code="modelDB.epc"></sp:message>
										</label>
										<label class='radio'>
											<input type='radio' name='processType' value='PETRINET' />
											<sp:message code="modelDB.petrinet"></sp:message>
										</label>
										</div>
									</div>
									
								
							</form>
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="modelDB.cancel"></sp:message></button>
							<button class='btn btn-primary' onClick="confirmInfoforimport()" data-dismiss='modal'><sp:message code="modelDB.confirm"></sp:message></button>
						</div>
					</div>
                
                			<div class='modal hide fade' id='uploadModelFile1' role='dialog' tabindex='-1'>
									<div class='modal-header'>
										<button class='close' data-dismiss='modal' type='button'>&times;</button>
										<h3><sp:message code="modelDB.uploadmodel"></sp:message></h3>
									</div>
									<div class='modal-body'>
									
										
									
										<form accept-charset="UTF-8" action="#" class="form form-horizontal" enctype="multipart/form-data" method="post" style="margin-bottom: 0;">
										<div class='span10'>
											<span class='btn btn-success fileinput-button'> 
											<i class='icon-plus icon-white'></i> <span><sp:message code="modelDB.addfiles"></sp:message>...</span> 
												<input id="modelfileupload1" data-bfi-disabled='' name='files[]' type='file' multiple />
											</span>
											<div id="progress1" class="progress">
												<div class="bar" style="width: 0%;"></div>
											</div>
			
											<div id="uploaded-files1">
												<h1></h1>
											</div>
										</div>
										</form>
									</div>
									<div class='modal-footer'>
										<button class='btn' data-dismiss='modal'><sp:message code="modelDB.cancel"></sp:message></button>
										<button class='btn btn-primary'  onClick="uploadModelFile1()"  data-dismiss='modal'><sp:message code="modelDB.confirm"></sp:message></button>
									</div>
								</div>
                
                <div class='modal hide fade' id='divyidongmoxing' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="modelDB.movemodelinfo"></sp:message></h3>
						</div>
						<div class='modal-body'>
							<form id='chooseifcopy' class='form form-horizontal' style='margin-bottom: 0;' >	
						  	<div class='control-group'>
								<label class='control-label' for='inputText1'><sp:message code="modelDB.keeporiginalmodels"></sp:message></label>
								<div class='controls'>
									<input id='checkbox' name="checkbox" type='checkbox' />
									</div>
							</div>
							<div class='control-group'>
								<label class='control-label' for='inputText1'><sp:message code="modelDB.selectcatalog"></sp:message></label>
								<div class='controls'>
									<div id='tree3'></div>
								</div>
							</div>
							</form>
							
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="modelDB.cancel"></sp:message></button>
							<button id='btConfirmMove' class='btn btn-primary' onClick="confirmMove()"><sp:message code="modelDB.confirm"></sp:message></button>
						</div>
					</div>
					 <!-- 响应移动 -->
					<div class='modal hide fade' id='divresponseForMove' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="modelDB.moverespondinginfo"></sp:message></h3>
						</div>
						<div id="divresponseForMoveContent" class='modal-body'>
							
							
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="modelDB.cancel"></sp:message></button>
							<button id='btConfirmMove' class='btn btn-primary' onClick="forceMove()"><sp:message code="modelDB.confirm"></sp:message></button>
						</div>
					</div>
					
					<!-- 响应删除 -->
					<div class='modal hide fade' id='divresponseForRemove' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="modelDB.removerespondinginfo"></sp:message></h3>
						</div>
						<div id="divresponseForRemoveContent" class='modal-body'>
							
						</div>
						<div class='modal-footer'>
							<button id='btConfirmMove' class='btn btn-primary' data-dismiss='modal'><sp:message code="modelDB.confirm"></sp:message></button>
						</div>
					</div>
                
                
				  <div class='modal hide fade' id='selectModelType' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							<h3><sp:message code="modelDB.newmodelinfo"></sp:message></h3>
						</div>
						<div class='modal-body'>
							<form id='confirmInfo' class='form form-horizontal' style='margin-bottom: 0;' >
								
								  	<div class='control-group'>
										<label class='control-label' for='inputText1'><sp:message code="modelDB.modelname"></sp:message></label>
										<div class='controls'>
											<input id='modelName' name="processName"  placeholder='<sp:message code="modelDB.modelname"></sp:message>' type='text' />
											</div>
									</div>
									 <div class='control-group'>
										<label class='control-label' for='inputTextArea1'><sp:message code="modelDB.modeldescription"></sp:message></label>
										<div class='controls'>
											<textarea id='modelsummary' name="processDescription" placeholder='<sp:message code="modelDB.modeldescription"></sp:message>' rows='3'></textarea>
										</div>
									</div>
									<div class='control-group'>
										<label class='control-label'><sp:message code="modelDB.modeltype"></sp:message></label>
										<div class='controls'>
											<label class='radio'>
											<input type='radio' name='processType' value='BPMN' />
											<sp:message code="modelDB.bpmn"></sp:message>
										</label>
										<label class='radio'>
											<input type='radio' name='processType' value='EPC' />
											<sp:message code="modelDB.epc"></sp:message>
										</label>
										<label class='radio'>
											<input type='radio' name='processType' value='PETRINET' />
											<sp:message code="modelDB.petrinet"></sp:message>
										</label>
										</div>
									</div>
									
								
							</form>
						</div>
						<div class='modal-footer'>
							<button class='btn' data-dismiss='modal'><sp:message code="modelDB.cancel"></sp:message></button>
							<button class='btn btn-primary' onClick="confirmInfo()" data-dismiss='modal'><sp:message code="modelDB.confirm"></sp:message></button>
						</div>
					</div>
					
					<div class='modal hide fade' id='lookBigImage' role='dialog' tabindex='-1'>
						<div class='modal-header'>
							<button class='close' data-dismiss='modal' type='button'>&times;</button>
							
						</div>
						<div class='modal-body'>
							<img id='imgLookBigImage' src="" />
						</div>
						<div class='modal-footer'>
							<button class='btn btn-primary'data-dismiss='modal'><sp:message code="modelDB.confirm"></sp:message></button>
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
                <div class='alert alert-info'>
			    <a class='' data-dismiss='alert' href='#'>&times;</a>
				   <sp:message code="modelDB.doubleclick"></sp:message>
				    <strong><sp:message code="modelDB.modelname"></sp:message></strong>
				    	<sp:message code="modelDB.canchange"></sp:message>
				</div>
				
                <table id="modelTable" class='table table-hover'>
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


<!-- <div class='row-fluid'>
<div class="span12 box">
		<div class='box-header'>
            <div class='title'>模型展示</div>
            <div class='actions'>
                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                </a>
                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                </a>
            </div>
        </div>
		<div class='box-content'>
			<div id="test"><img alt="1" src="assets/images/ajax-loaders/19.gif" /></div>
			<div id="test1"></div>
		</div>
</div>
</div> -->







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

<!-- / datatables -->
<script src='assets/javascripts/plugins/datatables/jquery.dataTables.min.js'  charset="utf-8" type='text/javascript'></script>
<script src='assets/javascripts/plugins/datatables/jquery.dataTables.columnFilter.js'  charset="utf-8" type='text/javascript'></script>

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
<script src='assets/javascripts/demo/modelDB.js'  type='text/javascript' charset="utf-8"></script>

<script type="text/javascript">
         internationalNames = {
        	//confirm
        	 "modelDB.confirm.deleteAllprocess":"<sp:message code="modelDB.confirm.deleteAllprocess"></sp:message>",
        	 
        	//DBTable
             "DBTable.modelName":"<sp:message code="DBTable.modelName"></sp:message>",
             "DBTable.modelType":"<sp:message code="DBTable.modelType"></sp:message>",
             "DBTable.CreateInfo":"<sp:message code="DBTable.CreateInfo"></sp:message>",
             "DBTable.Lastmodifiedtime":"<sp:message code="DBTable.Lastmodifiedtime"></sp:message>",
             "DBTable.size":"<sp:message code="DBTable.size"></sp:message>",
             "DBTable.version":"<sp:message code="DBTable.version"></sp:message>",
         }
</script>

</body>
</html>