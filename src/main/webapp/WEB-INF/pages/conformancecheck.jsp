<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title><sp:message code="confCheck.page"></sp:message></title>
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
       <jsp:include page="navigation.jsp" flush="true"/>
            <section id='content'>
                <nav><!-- 内容页面 -->
                    <div class='container-fluid'>
                        <div class='row-fluid' id='content-wrapper'>
                            <div class='span12'>
                                <div class='page-header'>
                                    <h1 class='pull-left'>
                                        <i class='icon-dashboard'></i>
                                        <span><sp:message code="confCheck.confcheck"></sp:message></span>
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
	                                        <li><sp:message code="confCheck.instanceanalyzer"></sp:message></li>
	                                        <li class='separator'>
	                                            <i class='icon-angle-right'></i>
	                                        </li>
	                                        <li class='active'><sp:message code="confCheck.confcheck"></sp:message></li>
	                                    </ul>
	                                </div>
                                </div>
                                <div class='alert alert-info'>
                                    <a class='close' data-dismiss='alert' href='#'>&times;</a>
                                    <sp:message code="confCheck.welcome"></sp:message>
                                    <strong><sp:message code="confCheck.confcheck"></sp:message></strong>
                              		<sp:message code="confCheck.description"></sp:message>
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
                                                        <li data-target='#step3'>
                                                            <span class='step'>3</span>
                                                        </li>
                                                    </ul>
                                                    <div class='actions'>
                                                        <button class='btn btn-mini btn-prev'><i class='icon-arrow-left'></i><sp:message code="confCheck.prevstep"></sp:message>
                                                        </button>
                                                        <button class='btn btn-mini btn-success btn-next' data-last='<sp:message code="confCheck.finish"></sp:message>'>
                                                            <sp:message code="confCheck.nextstep"></sp:message>
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
                                                                <div class='row-fluid'>
	                                                                <div class='span2'>
                                                                		<label class='text-center'><font face="微软雅黑"><big><sp:message code="confCheck.uploadlog"></sp:message></big></label>
                                                                		<label class='text-center'><font face="微软雅黑"><big><sp:message code="confCheck.uploadtype"></sp:message></big></font></label>
                                                                	</div>
	                                                                <div class='span8'>
	                                                               		<div class='text-left'>
		                                                               		<span class='btn btn-success fileinput-button'>
		                                                               			<i class='icon-plus icon-white'></i>
		                                                               			<span><sp:message code="confCheck.addfiles"></sp:message></span>
		                                                               			<input id="logfileuploadcc" data-bfi-disabled='' name='files[]' type='file' multiple />
		                                                               		</span>
		                                                                    <div id="progress" class="progress">
																		   		<div class="bar" style="width: 0%;"></div>
																			</div>
																			<table id="uploaded-files" class="table">
																				<tr>
																					<th><sp:message code="confCheck.filename"></sp:message></th>
																					<th><sp:message code="confCheck.filesize"></sp:message></th>
																					<th><sp:message code="confCheck.filetype"></sp:message></th>
																					<th><sp:message code="confCheck.download"></sp:message></th>
																				</tr>
																			</table>
																		</div>
	                                                                </div>
	                                                                <div class='span1'></div>
                                                               	</div>
                                                            </div>
                                                        </div>
                                                        <div class='step-pane' id='step2'>
                                                        	<div class='control-group'>
                                                        		<div class='row-fluid'>
                                                        			<div class='span1'></div>
                                                        			<div class='span10'>
                                                        				<div class='control-group'>
	                                                        				<div class='row-fluid'>
				                                                        		<div class='span12 box bordered-box blue-border'>
				                                                        			<div class='box-header green-background'>
				                                                        				<div class='title'>
				                                                        					<font face="微软雅黑">Fitness</font>
				                                                        				</div>
				                                                        				<div class='actions'>
				                                                        					<a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
				                                                        				</div>
				                                                        			</div>
				                                                        			<div class='box-content'>
				                                                        				<div class='control-group'>
				                                                        					<font face="微软雅黑"><p>*&nbsp;Fitness evaluates whether the observed process <i>complies with</i> the control flow specified by the process.</p></font>
				                                                        					<font face="微软雅黑"><p>*&nbsp;One way to investigate the fitness is to replay the log in the Petri net. The log replay is carried out in a non-blocking way, i.e., if there are tokens missing to fire the transition in question they are created artificially and replay proceeds. While doing so, diagnostic data is collected and can be accessed afterwards.</p></font>
				                                                        				</div>
				                                                        				<hr class='hr-normal' />
					                                                        			<div class='control-group'>
					                                                        				<div class='row-fluid'>
					                                                        					<div class='span1'>
							                                                        				<div class='text-center'>
										                                                        		<input type='checkbox' id='f_check' checked='checked' value='f' />
										                                                        		<b><big>&nbsp;f</big></b>
							                                                        				</div>
							                                                        			</div>
						                                                        				<div class='span12 box'>
									                                                        		<div class='box-content box-padding'>
											                                                        	<font face="微软雅黑"><p>The token-based <b><font color='#49bf67'>fitness</font></b> metric <i>f</i> relates the amount of missing tokens during log replay with the amount of consumed ones and the amount of remaining tokens with the produced ones. If the log could be replayed correctly, that is, there were no tokens missing nor remaining, it evaluates to 1.</p></font>
									                                                        		</div>
								                                                        		</div>
								                                                        	</div>
							                                                        	</div>
							                                                        	<hr class='hr-normal' />
							                                                        	<div class='control-group'>
							                                                        		<div class='row-fluid'>
							                                                        			<div class='span1'>
							                                                        				<div class='text-center'>
							                                                        					<label class='text-center'>
																												<input type='checkbox' id='pSE_check' checked='checked' value='pSE' />
																												<b><big>&nbsp;pSE</big></b>
																										</label>
							                                                        				</div>
							                                                        			</div>
							                                                        			<div class='span12 box'>
							                                                        				<div class='box-content box-padding'>
									                                                        			<font face="微软雅黑"><p>The <b><font color='#49bf67'>successful execution</font></b> metric <i>p<sub>SE</sub></i> determines the fraction of successfully executed process instances (taking the number of occurrences per trace into account).</p></font>
									                                                        		</div>
									                                                        	</div>
							                                                        		</div>
							                                                        	</div>
							                                                        	<hr class='hr-normal' />
							                                                        	<div class='control-group'>
							                                                        		<div class='row-fluid'>
							                                                        			<div class='span1'>
							                                                        				<div class='text-center'>
							                                                        					<label class='text-center'>
																												<input type='checkbox' id='pPC_check' checked='checked' value='pPC' />
																												<font face="微软雅黑"><b><big>&nbsp;pPC</big></b></font>
																										</label>
							                                                        				</div>
							                                                        			</div>
							                                                        			<div class='span12 box'>
									                                                        		<div class='box-content box-padding'>
									                                                        			<font face="微软雅黑"><p>The <b><font color='#49bf67'>proper completion</font></b> metric <i>p<sub>PC</sub></i> determines the fraction of properly completed process instances (taking the number of occurrences per trace into account).</p></font>
									                                                        		</div>
									                                                        	</div>
							                                                        		</div>
							                                                        	</div>
				                                                        			</div>
				                                                        		</div>
				                                                        	</div>
			                                                        	</div>
                                                                        <div class='control-group'>
                                                                            <div class='row-fluid'>
                                                                                <div class='span12 box bordered-box blue-border'>
                                                                                    <div class='box-header orange-background'>
                                                                                        <div class='title'>
                                                                                            <font face="微软雅黑">Precision</font>
                                                                                        </div>
                                                                                        <div class='actions'>
                                                                                            <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class='box-content'>
                                                                                        <div class='control-group'>
                                                                                            <font face="微软雅黑"><p>*&nbsp;Precision, or Behavioral Appropriateness, evaluates <i>how precisely</i> the model describes the observed process.</p></font>
                                                                                        </div>
                                                                                        <hr class='hr-normal' />
                                                                                        <div class='control-group'>
                                                                                            <div class='row-fluid'>
                                                                                                <div class='span1'>
                                                                                                    <div class='text-center'>
                                                                                                        <label class='text-center'>
                                                                                                            <input type='checkbox' id='saB_check' checked='checked' value='saB' />
                                                                                                            <font face="微软雅黑"><b><big>&nbsp;saB</big></b></font>
                                                                                                        </label>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class='span12 box'>
                                                                                                    <div class='box-content box-padding'>
                                                                                                        <font face="微软雅黑"><p>The <b><font color='#f8a326'>simple behavioral appropriateness</font></b> metric <i>sa<sub>B</sub></i> is based on the mean number of enabled transitions during log replay (the greater the value the less behavior is allowed by the process model and the more precisely the behavior observed in the log is captured). </p></font>
                                                                                                        <font face="微软雅黑"><p>*&nbsp;Note that this metric should only be used as a comparative means for models without alternative duplicate tasks. </p></font>
                                                                                                        <font face="微软雅黑"><p>*&nbsp;Note further that in order to determine the mean number of enabled tasks in the presence of invisible tasks requires to build the state space from the current marking after each replay step. Since this may greatly decrease the performance of the computational process, you might want to swich this feature off.</p></font>
                                                                                                </div>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <hr class='hr-normal' />
                                                                                        <div class='control-group'>
                                                                                            <div class='row-fluid'>
                                                                                                <div class='span1'>
                                                                                                    <div class='text-center'>
                                                                                                        <label class='text-center'>
                                                                                                            <input type='checkbox' id='aaB_check' checked='checked' value='aaB' />
                                                                                                            <font face="微软雅黑"><b><big>&nbsp;aaB</big></b></font>
                                                                                                        </label>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class='span12 box'>
                                                                                                    <div class='box-content box-padding'>
                                                                                                        <font face="微软雅黑"><p>The <b><font color='#f8a326'>advanced behavioral appropriateness</font></b> metric <i>aa<sub>B</sub></i> is based on successorship relations among activities with respect the event relations observed  in the log (the greater the value the more precisely the behavior observed in the log is captured).</p></font>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                        	</div>
                                                                        </div>
                                                                        <div class='control-group'>
                                                                            <div class='row-fluid'>
                                                                                <div class='span12 box bordered-box blue-border'>
                                                                                    <div class='box-header red-background'>
                                                                                        <div class='title'>
                                                                                            <font face="微软雅黑">Structure</font>
                                                                                        </div>
                                                                                        <div class='actions'>
                                                                                            <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class='box-content'>
                                                                                        <div class='control-group'>
                                                                                            <font face="微软雅黑"><p>*&nbsp;Structural Appropriateness evaluates whether the model describes the observed process in a <i>structurally suitable</i> way.</p></font>
                                                                                        </div>
                                                                                        <hr class='hr-normal' />
                                                                                        <div class='control-group'>
                                                                                            <div class='row-fluid'>
                                                                                                <div class='span1'>
                                                                                                    <div class='text-center'>
                                                                                                        <label class='text-center'>
                                                                                                            <input type='checkbox' id='saS_check' checked='checked' value='saS' />
                                                                                                            <font face="微软雅黑"><b><big>&nbsp;saS</big></b></font>
                                                                                                        </label>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class='span12 box'>
                                                                                                    <div class='box-content box-padding'>
                                                                                                        <font face="微软雅黑"><p>The <b><font color='#f34541'>simple structural appropriateness</font></b> metric <i>sa<sub>S</sub></i> is a simple metric based on the graph size of the model (the greater the value the more compact is the model). </p></font>
                                                                                                        <font face="微软雅黑"><p>*&nbsp;Note that this metric should only be used as a comparative means for models allowing for the same amount of behavior.</p></font>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <hr class='hr-normal' />
                                                                                        <div class='control-group'>
                                                                                            <div class='row-fluid'>
                                                                                                <div class='span1'>
                                                                                                    <div class='text-center'>
                                                                                                        <label class='text-center'>
                                                                                                            <input type='checkbox' id='aaS_check' checked='checked' value='aaS' />
                                                                                                            <font face="微软雅黑"><b><big>&nbsp;aaS</big></b></font>
                                                                                                        </label>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class='span12 box'>
                                                                                                    <div class='box-content box-padding'>
                                                                                                        <font face="微软雅黑"><p>The <b><font color='#f34541'>advanced structural appropriateness</font></b> metric <i>aa<sub>S</sub></i> is based on the detection of redundant invisible tasks (simply superfluous) and alternative duplicate tasks (list alternative behavior rather than expressing it in a meaningful way).</p></font>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
			                                                        </div>
	                                                        		<div class='span1'></div>
		                                                        </div>
	                                                       	</div>
                                                        </div>
                                                        <div class='step-pane' id='step3'>
                                                        	<div class='control-group'>
                                                        		<div class='row-fluid'>
		                                                        	<div class='span3'>
		                                                        		<label class='text-center'><font face="微软雅黑"><sp:message code="confCheck.miningalgo"></sp:message></font></label>
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
			                                                            	<div class='btn btn-primary' id='conformancecheck_btn'><sp:message code="confCheck.start"></sp:message></div>
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
                                                 	 <font color="#00acec" face="微软雅黑"><sp:message code="confCheck.inprogress"></sp:message></font>
                                                </div>
                                            </div>
                                            <div class='box-content'>
												<img alt="1" src="assets/images/ajax-loaders/19.gif" />
											</div>
										</div>
									</div>
								</div>

                               <div class='row-fluid' id='checking_result'>
                                    <div class='span12 box'>
                                        <div class='box-content box-padding'>
                                            <div class='box-header'>
                                                <div class='title'><font face="微软雅黑"><sp:message code="confCheck.result"></sp:message></font></div>
                                            </div>
                                            <div class='control-group'>
                                            	<div class="row-fluid">
	                                            	<div class='span1'></div>
		                                            <div class='span3 box bordered-box blue-border'>
	                                            		<div class='box-header green-background'>
                                                			<div class='title'>
                                                				<font face="微软雅黑">Fitness results</font>
                                                			</div>
                                                		</div>
                                               			<div class='box-content'>
                                               				<div class='row-fluid'>
                                               					<div class='span1'></div>
                                                   				<div class='span10 box'>
                                                   					<div class='title'><font face="微软雅黑">Fitness:</font></div>
		                                                      		<div class='box-content box-padding' id='f_fitness'></div>
	                                                     		</div>
                                               				</div>
                                               			</div>
		                                            </div>
		                                            <div class='span3 box bordered-box blue-border'>
	                                            		<div class='box-header orange-background'>
                                                			<div class='title'>
                                                				<font face="微软雅黑">Precision results</font>
                                                			</div>
                                                		</div>
                                               			<div class='box-content'>
                                               				<div class='row-fluid'>
                                               					<div class='span1'></div>
                                                   				<div class='span10 box'>
                                                   					<div class='title'><font face="微软雅黑">Simple Behavioral Appropriateness:</font></div>
		                                                      		<div class='box-content box-padding' id='p_sBA'>
		                                                      		</div>
	                                                     		</div>
	                                                     	</div>
	                                                     	<div class='row-fluid'>
	                                                     		<div class='span1'></div>
	                                                     		<div class='span10 box'>
                                                   					<div class='title'><font face="微软雅黑">Advanced Behavioral Appropriateness:</font></div>
		                                                      		<div class='box-content box-padding' id='p_aBA'>
		                                                      		</div>
	                                                     		</div>
	                                                     	</div>
	                                                     	<div class='row-fluid'>
	                                                     		<div class='span1'></div>
	                                                     		<div class='span10 box'>
                                                   					<div class='title'><font face="微软雅黑">Degree of Model Flexibility:</font></div>
		                                                      		<div class='box-content box-padding' id='p_dMF'>
		                                                      		</div>
	                                                     		</div>
                                               				</div>
                                               			</div>
		                                            </div>
		                                            <div class='span3 box bordered-box blue-border'>
	                                            		<div class='box-header red-background'>
                                                			<div class='title'>
                                                				<font face="微软雅黑">Structure results</font>
                                                			</div>
                                                		</div>
                                               			<div class='box-content'>
                                               				<div class='row-fluid'>
                                               					<div class='span1'></div>
                                                   				<div class='span10 box'>
                                                   					<div class='title'><font face="微软雅黑">Simple Structure Appropriateness:</font></div>
		                                                      		<div class='box-content box-padding' id='s_sSA'>
		                                                      		</div>
	                                                     		</div>
	                                                     	</div>
	                                                     	<div class='row-fluid'>
	                                                     		<div class='span1'></div>
	                                                     		<div class='span10 box'>
                                                   					<div class='title'><font face="微软雅黑">Advanced Structure Appropriateness:</font></div>
		                                                      		<div class='box-content box-padding' id='s_aSA'>
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
                                </div>
                                <!--这段的结束-->
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
        <!-- / conformancecheck -->
        <script src='assets/javascripts/conformancecheck/check.js' type='text/javascript'></script>
        <script src='assets/javascripts/conformancecheck/conformancecheck.js' type='text/javascript'></script>
    </body>
</html>