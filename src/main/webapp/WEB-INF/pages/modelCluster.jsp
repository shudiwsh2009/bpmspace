<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<%@ page language="java" import="com.chinamobile.bpmspace.core.util.FileUtil" %>
<%@ page language="java" import="java.lang.*" %>


<!DOCTYPE html>
<html>
<head>
    <title><sp:message code="modelClustering.page"></sp:message></title>
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
<section id='content'> </nav><!-- 内容页面 -->
<div class='container-fluid'>
<div class='row-fluid' id='content-wrapper'>
<div class='span12'>
<div class='page-header'>
    <h1 class='pull-left'>
        <i class='icon-dashboard'></i>
        <span><sp:message code="modelClustering.modelclustering"></sp:message></span>
    </h1>
    <div class='pull-right'>
                <ul class='breadcrumb'>
                    <li>
                        <a href="ProcessModelAnalyze.html"><i class='icon-bar-chart'></i>
                        </a>
                    </li>
                    <li class='separator'>
                        <i class='icon-angle-right'></i>
                    </li>
                    <li><sp:message code="modelClustering.modelanalyzer"></sp:message></li>
                    <li class='separator'>
                        <i class='icon-angle-right'></i>
                    </li>
                    <li class='active'><sp:message code="modelClustering.modelclustering"></sp:message></li>
                </ul>
            </div>
</div>
<div class='alert alert-info'>
    <a class='close' data-dismiss='alert' href='#'>&times;</a>
    <sp:message code="modelClustering.welcome"></sp:message>
    <strong><sp:message code="modelClustering.modelclustering"></sp:message></strong>
    <sp:message code="modelClustering.description"></sp:message>
</div>

 <div class='row-fluid'>
<div class='span12 box'>
<div class='box-header'>
    <div class='title'><sp:message code="modelClustering.selectmodels"></sp:message></div>
    <div class='actions'>
        <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
        </a>
        <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
        </a>
    </div>
</div>
<div class='box-content'>
<div class='row-fluid'>
<div class='span2'>
  		<label class='text-center'><font face="微软雅黑"><big><sp:message code="modelClustering.selecttype"></sp:message></big></font></label>
  	</div>
<div class='row-fluid'>
			<div class='span10 box'>
				<div class='row-fluid'>
						<div class='span10'>
							 <div class='pull-left'>
			                	<a class='btn btn-success fullfill-items' id="btnselectfromDB1"  data-toggle='modal' href='#showModelsInDB1' role='button'><sp:message code="modelClustering.fromrepository"></sp:message></a>
			                    <a class='btn btn-success fullfill-items' data-toggle='modal' href='#uploadModelFile1' role='button'><sp:message code="modelClustering.fromlocalfiles"></sp:message></a>
			                </div>
			                <div class='pull-right'>
			                	<a class='btn btn-success fullfill-items' id='btnremoveprocesses' role='button'><sp:message code="modelClustering.removeselected"></sp:message></a>
			                </div>
							
							<div class='clearfix'></div>
							 <hr class='hr-normal' />
		           
		            		<div id='tree2'></div>
								
							<div >
						</div>
					</div>
				</div>
			</div>
</div>
	<div class='span1'></div>
</div>
</div>
</div>
</div>

 										
 	
        
 <div class='row-fluid'>
    		<div class='span12 box'>
       			<div class='box-content'>
             <div class='clearfix'></div>
          	<form accept-charset="UTF-8" action="#" class="form form-horizontal" method="post" style="margin-bottom: 0;" /><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="CFC7d00LWKQsSahRqsfD+e/mHLqbaVIXBvlBGe/KP+I=" /></div>
                    <div class='text-center'>
                            <a class='btn btn-success btn-large'  id="btn_caclulate" role="button"><sp:message code="modelClustering.start"></sp:message></a>
                    </div>
                </div>
            </form>
           	<hr class='hr-normal' />
            <div class='alert alert-info'>
                <a class='close' data-dismiss='alert' href='#'>&times;</a>
                 <sp:message code="modelClustering.showresult"></sp:message>
            </div>
            </div>
            </div>
            </div>
            
            
             <div class='row-fluid'>
<div class='span12 box bordered-box' style='margin-bottom:0;'>
<div class='box-header'>
    <div class='title'><sp:message code="modelClustering.result"></sp:message></div>
    <div class='actions'>
        <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
        </a>
        <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
        </a>
    </div>
</div>
<div class='box-content box-no-padding'>
	<div id="calculateResult" style="border-radius:20px;height:600px;background:-webkit-radial-gradient(white,#0087b9);background:-moz-radial-gradient(white,#0087b9);">
		
		
	</div>
	<div id="log"></div>
</div>
</div>
</div><!-- 表格结束-->






<!-- 从模型库中导入代码 -->
<div class='modal hide fade' id='showModelsInDB1' role='dialog' tabindex='-1'>
		<div class='modal-header'>
			<button class='close' data-dismiss='modal' type='button'>&times;</button>
			<h3><sp:message code="modelClustering.fromrepository"></sp:message></h3>
		</div>
		<div class='modal-body'>
			
			<div class='control-group'>
				<label class='control-label' for='inputText1'><sp:message code="modelClustering.selectcatalog"></sp:message></label>
				<div class='controls'>
					<div id='modelsInDB1'></div>
				</div>
				<table id="modelsTable1" class='table table-hover'>
                    <thead>
                    
                    </thead>
                    <tbody >
                   
                    </tbody>
                </table>
			</div>
			
		</div>
		<div class='modal-footer'>
			<button class='btn' data-dismiss='modal'><sp:message code="modelClustering.cancle"></sp:message></button>
			<button id='btConfirmMove1' class='btn btn-primary' onClick="confirmQueryByModelInDB1()"><sp:message code="modelClustering.confirm"></sp:message></button>
		</div>
 </div>
 
<!--  本地上传代码 -->
 <div class='modal hide fade' id='uploadModelFile1' role='dialog' tabindex='-1'>
	<div class='modal-header'>
		<button class='close' data-dismiss='modal' type='button'>&times;</button>
		<h3><sp:message code="modelClustering.uploadmodel"></sp:message></h3>
	</div>
	<div class='modal-body'>
		<form accept-charset="UTF-8" action="#" class="form form-horizontal" enctype="multipart/form-data" method="post" style="margin-bottom: 0;">
		<div class='span10'>
			<span class='btn btn-success fileinput-button'> 
			<i class='icon-plus icon-white'></i> <span><sp:message code="modelClustering.addfiles"></sp:message>Add files...</span> 
				<input id="modelfileupload1" data-bfi-disabled='' name='files[]' type='file' multiple />
			</span>
			<div id="progress1" class="progress progress-striped active">
				<div class="bar" style="width: 0%;"></div>
			</div>

			<div id="uploaded-files1">
				<h1></h1>
			</div>
		</div>
		</form>
	</div>
	<div class='modal-footer'>
		<button class='btn' data-dismiss='modal'><sp:message code="modelClustering.cancle"></sp:message></button>
		<button class='btn btn-primary'  data-dismiss='modal'><sp:message code="modelClustering.confirm"></sp:message></button>
	</div>
</div>





<div class='modal hide fade' style="width:300px" data-backdrop="static" id='waitForResponse' role='dialog'>
	<div class='span12 box'>
		<div class='box-padding'>
			<div class='box-header'>
                                        <div class='title'>
                                         	 <font color="#00acec" face="微软雅黑"><sp:message code="modelClustering.inprogress"></sp:message></font>
                                        </div>
                                    </div>
                                    <div class='box-content'>
				<img alt="1" src="assets/images/ajax-loaders/19.gif" />
			</div>
		</div>
	</div>
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
<%
		String webrootpath = FileUtil.WEBAPP_ROOT;
		webrootpath=webrootpath.substring(0, webrootpath.length()-1);
	
	
%>
<script>
	var webapproot;
	$(document).ready(function() {	
		webapproot = "<%=webrootpath%>";
	});


</script>
<script type="text/javascript">
         internationalNames = {
        	//confirm
        	 "FileTree.alreadySelectedFile":"<sp:message code="FileTree.alreadySelectedFile"></sp:message>",
         }
</script>

<!-- / page script -->
<script src='assets/javascripts/modelCluster/modelCluster.js' type='text/javascript'></script>
<!--[if IE]><script language="javascript" type="text/javascript" src="assets/javascripts/jit/Extras/excanvas.js"></script><![endif]-->
<script language="javascript" type="text/javascript" src="assets/javascripts/jit/jit.js"></script>
<script language="javascript" type="text/javascript" src="assets/javascripts/modelCluster/spacetree.js"></script>
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

</body>
</html>