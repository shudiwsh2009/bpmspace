<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title><sp:message code="exeeffianalyzer.page"></sp:message></title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport' /> 
    
    <!--[if lt IE 9]>
    <script src='assets/javascripts/html5shiv.js' type='text/javascript'></script>
    <![endif]-->

    <!-- / bootstrap -->
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
<!--顶部导航开始-->
<body class='contrast-blue '>
<jsp:include page="navigation.jsp" flush="true"/>
    <!--内容页面开始-->
    <section id='content'>
        <div class='container-fluid'>
            <div class='row-fluid' id='content-wrapper'>
            <!--功能简介-->
                <div class='span12'>
                 <div class='page-header'>
                  <h1 class='pull-left'>
                      <i class='icon-tint'></i>
                      <span><em><sp:message code="exeeffianalyzer.exeeffianalyzer"></sp:message></em></span>
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
                           <li><sp:message code="exeeffianalyzer.processmonitor"></sp:message></li>
                           <li class='separator'>
                               <i class='icon-angle-right'></i>
                           </li>
                           <li class='active'><sp:message code="exeeffianalyzer.exeeffianalyzer"></sp:message></li>
                       </ul>
                    </div>
                  </div>
                  <div class='alert alert-info'>
                  <a class='' data-dismiss='alert' href='#'>&times;</a>
                  <sp:message code="exeeffianalyzer.welcome"></sp:message>
                  <strong><sp:message code="exeeffianalyzer.exeeffianalyzer"></sp:message></strong>
                  <sp:message code="exeeffianalyzer.description"></sp:message>
                  </div>
                <!--日志选择--> 
                <div class="row-fluid">
                    <div class='span3 box'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.selectlog"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>
                        <div class='box-content'>
                        	<div class='pull-left'>
                                <a class='btn btn-success fileinput-button' id='btnChooseLog' data-target='#divNewLogName' data-toggle = 'modal'><sp:message code="exeeffianalyzer.uploadlog"></sp:message></a>
                                <a class='btn btn-danger' id='btnDeleteDB' data-target='#divNewRemoveConfirm' data-toggle='modal' ><sp:message code="exeeffianalyzer.removelog"></sp:message></a>
                            </div>
                            <div class='clearfix'></div>
                            <hr class='hr-normal' />                              
                            <div id='instanceDB_tree'></div> 
                        </div>
                    </div> 
                    <!-- 日志命名弹窗 -->
                    <div class = 'modal hide fade' id ='divNewLogName' tabindex = '-1'>
                        <div class = 'modal-header'>
                            <button class='close' data-dismiss='modal' type = 'button'>
                            </button>
                            <h3><sp:message code="exeeffianalyzer.uploadlog"></sp:message></h3>
                        </div>
                        <div class = 'modal-body'>
                            <form id='confirmInfo' class='form form-horizontal' style='margin-bottom: 0;' >
                                <label class='control-label' for='inputText1'><sp:message code="exeeffianalyzer.logname"></sp:message></label>
                                <div class='controls'>
                                    <input id='logName' name="logName"  placeholder='<sp:message code="exeeffianalyzer.logname"></sp:message>' type='text' onKeyUp="showHint(this.value)"/>
                                    <span id="txtHint"></span>
                                </div>
                                
                            </form>
                        </div>
                        <div class = 'modal-footer'>
                                <div class = 'controls'>
                                    <span  id="hintLog"></span>
                                </div>
                            <button class = 'btn' data-dismiss = 'modal'><sp:message code="exeeffianalyzer.cancel"></sp:message></button>
                            <button class = 'btn' id='btnNext'  data-target='#divChooseLog' onClick = "LogNameUpload()"><sp:message code="exeeffianalyzer.nextstep"></sp:message></button>
                        </div>
                    </div>
                    <!-- 日志上传弹窗 -->
                    <div class = 'modal hide fade' id ='divChooseLog' tabindex = '-1'>
                        <div class = 'modal-header'>
                            <button class='close' data-dismiss='modal' type = 'button'>
                            </button>
                            <h3><sp:message code="exeeffianalyzer.uploadlog"></sp:message></h3>
                        </div>
                        <div class = 'modal-body'>
                            <div class = 'controls'>
                                <span class='btn btn-success fileinput-button'>
                                    <i class='icon-plus icon-white'></i>
                                    <span><sp:message code="exeeffianalyzer.addfiles"></sp:message></span>
                                    <input id="fileuploadInstance" data-bfi-disabled='' name='files[]' type='file' multiple  />
                                </span>
                                <div id="progress" class="progress">
                                    <div class="bar"  id = "bar" style="width: 0%;"></div>
                                </div>
                            </div>
                        </div>
                        <div class = 'modal-footer'>
                            <span id = 'upload-files-info' align = 'left'></span>
                            <button id = 'btn-upload-finish' class = 'btn' data-dismiss = 'modal' onClick = "cleanModal()"><sp:message code="exeeffianalyzer.finish"></sp:message></button>
                        </div>
                    </div>
                    <!-- 删除弹窗 -->
                    <div class='modal hide fade' id='divNewRemoveConfirm' tabindex='-1'>
                        <div class='modal-header'>
                            <button class='close' data-dismiss='modal' type='button'>&times;
                            </button>
                            <h3><sp:message code="exeeffianalyzer.removelog"></sp:message></h3>
                        </div>
                        <div class='modal-body'>
                            <div class='controls'>
                                <h5><sp:message code="exeeffianalyzer.removecheck"></sp:message></h5>
                            </div>
                        </div>
                        <div class='modal-footer'>
                            <button class='btn' data-dismiss='modal'><sp:message code="exeeffianalyzer.cancel"></sp:message></button>
                                <button id='btConfirmRmv' class='btn btn-primary' onClick="confirmRemove()"><sp:message code="exeeffianalyzer.confirm"></sp:message></button>
                        </div>
                    </div>
                    <!--====================== 弹窗结束 ======================-->
                    <div class='span9 box'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.logoverview"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>
                            <div class='box-content' id='instanceTableOperator'>
                                <table id="instanceTable" class='table table-hover'></table>
                           </div>
                        </div>
                    </div>
                </div>
                 <!--实例统计报表-->                
                <div class="row-fluid">
                    <div class='span12 box' style='margin-bottom:0;'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.logreport"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>          
						<div class='box-content'>
                            <div class='clearfix'></div>
                            <hr class='hr-normal' />                              
                            <div id="instanceChart"></div>
                        </div>
                    </div>
                 </div>
                <!--实例类型统计--> 
                <div class="row-fluid">
                    <div class='span3 box'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.logtypestat"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>
                        <div class='box-content'>
                        <div class='clearfix'></div>
                        <hr class='hr-normal' /> 
                        <div class='scrollable' data-scrollable-height='400' data-scrollable-start='top'>
                            <div id='activity_tree'></div>   
                         </div>
                       </div>
                    </div>
                    <div class='span9 box' style='margin-bottom:0;'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.activitytypestat"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>          
                         <div class='box-content' id='variantsTableOperator'>
                             <table id="variantsTable" class='table table-hover'></table>
                        </div>
                    </div>
                </div>
                <!--活动统计内容内容-->          
                <div class="row-fluid">
                    <div class='span7 box'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.activitystat"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                            </div>
                            <div class='box-content' id='activityTableOperator'>
                                <table id="activityTable" class='table table-hover'></table>
                           </div>
                    </div>
                    <div class='span5 box' style='margin-bottom:0;'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.activityreport"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>          
						<div class='box-content'>
                            <div class='clearfix'></div>
                            <hr class='hr-normal' /> 
                            <div id="activityChart"></div>                             
                        </div>
                    </div>
                </div>
                <!--资源统计内容--> 
                <div class="row-fluid">
                    <div class='span7 box'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.resourcestat"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>
                        <div class='box-content' id='resourceTableOperator'>
                            <table id="resourceTable" class='table table-hover'></table>
                       </div>
                    </div>
                    <div class='span5 box' style='margin-bottom:0;'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.resourcerepost"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>          
						<div class='box-content'>
                            <div class='clearfix'></div>
                            <hr class='hr-normal' /> 
                            <div id="resourceChart"></div>                               
                        </div>
                    </div>
                </div>
                <!--高频率活动及资源统计内容--> 
                 <div class="row-fluid">
                    <div class='span6 box'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.highfrequencyactivitystat"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>
                        <div class='box-content' id='resourceTableOperator'>
                            <div class='clearfix'></div>
                            <hr class='hr-normal' /> 
                            <div id="activityPieChart"></div>   
                       </div>
                    </div>
                    <div class='span6 box' style='margin-bottom:0;'>
                        <div class='box-header'>
                            <div class='title'><sp:message code="exeeffianalyzer.activeresourcestat"></sp:message></div>
                            <div class='actions'>
                                <a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
                                </a>
                                <a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
                                </a>
                            </div>
                        </div>          
                        <div class='box-content'>
                            <div class='clearfix'></div>
                            <hr class='hr-normal' /> 
                            <div id="resourcePieChart"></div>                               
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
<script src='assets/javascripts/demo/demo.js' type='text/javascript'></script>
<!-- <script src='assets/javascripts/demo/modelDB.js'  type='text/javascript'></script> -->
<!-- / Table -->
<script src="assets/javascripts/highcharts/table.js" type="text/javascript"></script> 
<!-- / highchats -->
<script src="assets/javascripts/highcharts/highcharts.js"></script>
<script src="assets/javascripts/highcharts/exporting.js"></script>
<script src="assets/javascripts/visualization/executeefficencyanalyze.js" type="text/javascript"></script>
<script src="assets/javascripts/visualization/loadinstance.js" type="text/javascript"></script>
<script type="text/javascript">
         internationalNames = {
        	"InstanceLogOverViewTable.instanceId":"<sp:message code="InstanceLogOverViewTable.instanceId"></sp:message>",
        	"InstanceLogOverViewTable.ActivityNum":"<sp:message code="InstanceLogOverViewTable.ActivityNum"></sp:message>",
        	"InstanceLogOverViewTable.startTime":"<sp:message code="InstanceLogOverViewTable.startTime"></sp:message>",
        	"InstanceLogOverViewTable.endTime":"<sp:message code="InstanceLogOverViewTable.endTime"></sp:message>",
        	"InstanceLogOverViewTable.avergeConsuming":"<sp:message code="InstanceLogOverViewTable.avergeConsuming"></sp:message>",
        	"InstanceLogOverViewTable.medianConsuming":"<sp:message code="InstanceLogOverViewTable.medianConsuming"></sp:message>",
        	"InstanceLogOverViewTable.totalConsuming":"<sp:message code="InstanceLogOverViewTable.totalConsuming"></sp:message>",
        	"InstanceActivityStatisticsTables.acttivityName":"<sp:message code="InstanceActivityStatisticsTables.acttivityName"></sp:message>",
        	"InstanceActivityStatisticsTables.excuteResources":"<sp:message code="InstanceActivityStatisticsTables.excuteResources"></sp:message>",
        	"InstanceActivityStatisticsTables.startTime":"<sp:message code="InstanceActivityStatisticsTables.startTime"></sp:message>",
        	"InstanceActivityStatisticsTables.endTime":"<sp:message code="InstanceActivityStatisticsTables.endTime"></sp:message>",
        	"InstanceActivityStatisticsTables.totalCosuming":"<sp:message code="InstanceActivityStatisticsTables.totalCosuming"></sp:message>",
        	"InstanceActivityStatisticsTables.waitingTime":"<sp:message code="InstanceActivityStatisticsTables.waitingTime"></sp:message>",
        	"InstanceActivityStatisticsTables.occurFrequency":"<sp:message code="InstanceActivityStatisticsTables.occurFrequency"></sp:message>",
        	"InstanceActivityStatisticsTables.relativefrequency":"<sp:message code="InstanceActivityStatisticsTables.relativefrequency"></sp:message>",
        	"InstanceActivityStatisticsTables.averageConsuming":"<sp:message code="InstanceActivityStatisticsTables.averageConsuming"></sp:message>",
        	"ResouceTable.resouceName":"<sp:message code="ResouceTable.resouceName"></sp:message>",
        	"ResouceTable.occurFrequency":"<sp:message code="ResouceTable.occurFrequency"></sp:message>",
        	"ResouceTable.relativeFrequency":"<sp:message code="ResouceTable.relativeFrequency"></sp:message>",
        	"ResouceTable.averageConsuming":"<sp:message code="ResouceTable.averageConsuming"></sp:message>",
        	"ResouceTable.totalCosuming":"<sp:message code="ResouceTable.totalCosuming"></sp:message>",
        	"js.InstanceDBTables.instanceId":"<sp:message code="js.InstanceDBTables.instanceId"></sp:message>",
        	"js.InstanceDBTables.instanceType":"<sp:message code="js.InstanceDBTables.instanceType"></sp:message>",
        	"js.InstanceDBTables.instanceName":"<sp:message code="js.InstanceDBTables.instanceName"></sp:message>",
        	"js.InstanceDBTables.instncaeCatalog":"<sp:message code="js.InstanceDBTables.instncaeCatalog"></sp:message>",
        	"js.InstanceDBTables.createTime":"<sp:message code="js.InstanceDBTables.createTime"></sp:message>",
			        
         }
</script>
</body>
</html>