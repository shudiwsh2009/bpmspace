<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title><sp:message code="processMining.page"></sp:message></title>
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
    </head>
    <body class='contrast-blue '>
       <jsp:include page="navigation.jsp" flush="true"/>                </ul>
                       
            
            <section id='content'>
                <nav><!-- 内容页面 -->
                    <div class='container-fluid'>
                        <div class='row-fluid' id='content-wrapper'>
                            <div class='span12'>
                                <div class='page-header'>
                                    <h1 class='pull-left'>
                                        <i class='icon-dashboard'></i>
                                        <span><sp:message code="processMining.processmining"></sp:message></span>
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
	                                        <li><sp:message code="processMining.instanceanalyzer"></sp:message></li>
	                                        <li class='separator'>
	                                            <i class='icon-angle-right'></i>
	                                        </li>
	                                        <li class='active'><sp:message code="processMining.processmining"></sp:message></li>
	                                    </ul>
	                                </div>
                                </div>
                                <div class='alert alert-info'>
                                    <a class='close' data-dismiss='alert' href='#'>&times;</a>
                                    <sp:message code="processMining.welcome"></sp:message>
                                    <strong><sp:message code="processMining.processmining"></sp:message></strong>
                              		<sp:message code="processMining.description"></sp:message>
                                </div>

                                <div class='row-fluid'>
                                    <div class='span12 box'>
                                        <div class='box-content box-padding'>
                                            <div class='fuelux'>
                                                <div class='wizard'>
                                                    <ul class='steps'>
                                                        <li class='active' data-target='#step1'>
                                                            <span class='step'>1</span>
                                                        </li>
                                                        
                                                        <li data-target='#step2'>
                                                            <span class='step'>2</span>
                                                        </li>
                                                        
                                                    </ul>
                                                    <div class='actions'>
                                                        <button class='btn btn-mini btn-prev'><i class='icon-arrow-left'></i><sp:message code="processMining.prevstep"></sp:message>
                                                        </button>
                                                        <button class='btn btn-mini btn-success btn-next' data-last='<sp:message code="processMining.finish"></sp:message>'>
                                                            <sp:message code="processMining.nextstep"></sp:message>
                                                            <i class='icon-arrow-right'></i>
                                                        </button>
                                                    </div>
                                                </div>
                                                <div class='step-content'>
                                                    <hr class='hr-normal' />
                                                    <form accept-charset="UTF-8" action="#" class="form form-horizontal" enctype="multipart/form-data" method="post" style="margin-bottom: 0;">
	                                                    <div style="margin:0;padding:0;display:inline">
		                                                    <input name="utf8" type="hidden" value="&#x2713;" />
		                                                    <input name="authenticity_token" type="hidden" value="CFC7d00LWKQsSahRqsfD+e/mHLqbaVIXBvlBGe/KP+I=" />
	                                                    </div>
                                                        <div class='step-pane active' id='step1'>
                                                            <div class='control-group'>
                                                            	<ul class="nav nav-tabs" id="fileChoose">
																	  <li class="active"><a href="#home" data-toggle="tab">从本地上传</a></li>
																	  <li><a href="#FromInstanceDB" data-toggle="tab">从数据库中选择</a></li>
																</ul>
																<div class="tab-content">
																	<div class="tab-pane active" id="home">
                                                                		<div class='row-fluid'>
		                                                                	<div class='span2'>
		                                                                		<label class='text-center'><font face="微软雅黑"><big><sp:message code="processMining.uploadlog"></sp:message></big></font></label>
		                                                                		<label class='text-center'><font face="微软雅黑"><big><sp:message code="processMining.uploadtype"></sp:message></big></font></label>
		                                                                	</div>
			                                                                <div class='span8'>
			                                                                	<div class='text-left'>
				                                                               		<span class='btn btn-success fileinput-button'>
				                                                               			<i class='icon-plus icon-white'></i>
				                                                               			<span><sp:message code="processMining.addfiles"></sp:message></span>
				                                                               			<input id="logfileuploadpm" data-bfi-disabled='' name='files[]' type='file' multiple />
				                                                               		</span>
				                                                                    <div id="progress" class="progress">
																				   		<div class="bar" style="width: 0%;"></div>
																					</div>
																					<table id="uploaded-files" class="table">
																						<tr>
																							<th><sp:message code="processMining.filename"></sp:message></th>
																							<th><sp:message code="processMining.filesize"></sp:message></th>
																							<th><sp:message code="processMining.filetype"></sp:message></th>
																							<th><sp:message code="processMining.download"></sp:message></th>
																						</tr>
																					</table>
																				</div>
			                                                                </div>
			                                                                
			                                                                <div class='span1'></div>
                                                               			</div>
                                                               		</div>
	                                                               	<div class="tab-pane" id="FromInstanceDB">
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
														                        	<!-- 
														                        	<div class='pull-left'>
														                                <a class='btn btn-success fileinput-button' id='btnChooseLog' data-target='#divNewLogName' data-toggle = 'modal'><sp:message code="exeeffianalyzer.uploadlog"></sp:message></a>
														                                <a class='btn btn-danger' id='btnDeleteDB' data-target='#divNewRemoveConfirm' data-toggle='modal' ><sp:message code="exeeffianalyzer.removelog"></sp:message></a>
														                            </div>
														                             -->
														                             <div class='pull-left'>
														                             	<p>
														                             	日志库
														                             	</p>
														                             </div>
														                            <div class='clearfix'></div>
														                            <hr class='hr-normal' />                              
														                            <div id='instanceDB_tree'></div> 
														                        </div>
														                    </div>
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
                                            
                                                        <div class='step-pane' id='step2'>
                                                        	<div class='control-group'>
                                                        		<div class='row-fluid'>
		                                                        	<div class='span3'>
		                                                        		<label class='text-center'><font face="微软雅黑"><sp:message code="processMining.miningalgorithm"></sp:message></font></label>
		                                                        	</div>
		                                                        	<div class='span6'>
		                                                        		<div class='text-left'>
		                                                                    <select id='mining_algorithm_selected'>
		                                                                        <option value='alpha'>Alpha</option>
		                                                                        <option value='alphaPlusPlus'>Alpha++</option>
		                                                                        <option value='alphaSharp'>Alpha#</option>
		                                                                        <option value='genetic'>Genetic</option>
		                                                                        <option value='dtGenetic'>DTGenetic</option>
		                                                                        <option value='heuristic'>Heuristic</option>
		                                                                        <option value='region'>Region</option>
		                                                                    </select>
	                                                                    </div>
		                                                            </div>
		                                                            <div class='span3'>
		                                                            	<div class='text-center'>
			                                                            	<div class='btn btn-primary' id='generate_mining_btn'><sp:message code="processMining.start"></sp:message></div>
		                                                                </div>
	                                                                </div>
                                                                </div>
                                                            </div>
                                                        </div>  
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class='modal hide fade' style="width:300px" data-backdrop="static" id='waitForResponse' role='dialog'>
									<div class='span12 box'>
										<div class='box-padding'>
											<div class='box-header'>
                                                <div class='title'>
                                                 	 <font color="#00acec" face="微软雅黑"><sp:message code="processMining.inprogress"></sp:message></font>
                                                </div>
                                            </div>
                                            <div class='box-content'>
												<img alt="1" src="assets/images/ajax-loaders/19.gif" />
											</div>
										</div>
									</div>
								</div>

                                <div class='row-fluid' id='minging_result'>
                                    <div class='span12 box'>
                                        <div class='box-content box-padding'>
                                            <div class='box-header'>
                                                <div class='title'><font face="微软雅黑"><sp:message code="processMining.result"></sp:message></font></div>
                                            </div>
                                            <div class='control-group'>
                                            	<div class="row-fluid">
		                                            <div class='span9'>
		                                                <div class='text-center' id='png_result'></div>
		                                            </div>
		                                            <div class='span3'>
	                                                    <div class='text-center' id='download_picture'></div>
	                                                    <hr class='hr-normal' />
	                                                    <div class='text-center' id='download_file'></div>
		                                            </div>
	                                            </div>
                                            </div>
                                        </div>
                                    </div>     
                                </div>
                                <!--这段的结束-->
                            </div>
                        </div>
                    </div>
                </nav>
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
        <!-- / fileupload -->
		<script src='assets/javascripts/plugins/fileupload/jquery.iframe-transport.min.js' type='text/javascript'></script>
		<script src='assets/javascripts/plugins/fileupload/jquery.fileupload.min.js' type='text/javascript'></script>
		<script src='assets/javascripts/plugins/fileupload//cors/jquery.xdr-transport.js' type='text/javascript'></script>
        <!-- / processmining -->
        <script src='assets/javascripts/processmining/mining.js' type='text/javascript'></script>
        <script src='assets/javascripts/processmining/processmining.js' type='text/javascript'></script>
    </body>
</html>