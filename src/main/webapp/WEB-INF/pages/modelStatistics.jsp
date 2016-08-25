<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<%@ page language="java" import="com.chinamobile.bpmspace.core.util.FileUtil" %>
<%@ page language="java" import="java.lang.*" %>


<!DOCTYPE html>
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

<jsp:include page="navigation.jsp" flush="true"/>
<section id='content'> </nav><!-- 内容页面 -->
<div class='container-fluid'>
<div class='row-fluid' id='content-wrapper'>
<div class='span12'>
<div class='page-header'>
    <h1 class='pull-left'>
        <i class='icon-dashboard'></i>
        <span>模型校验</span>
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
                    <li>流程模型分析</li>
                    <li class='separator'>
                        <i class='icon-angle-right'></i>
                    </li>
                    <li class='active'>模型校验</li>
                </ul>
            </div>
</div>
<div class='alert alert-info'>
    <a class='close' data-dismiss='alert' href='#'>&times;</a>
    Welcome to
    <strong>Process Space 流程管理</strong>
    在这个页面中 你可以 balabal....
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
                        <button class='btn btn-mini btn-prev'><i class='icon-arrow-left'></i>Prev
                        </button>
                        <button class='btn btn-mini btn-success btn-next' data-last='Finish'>
                            Next
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
                                         		<label class='text-center'><font face="微软雅黑"><big>上传模型</big></label>
                                         		<label class='text-center'><font face="微软雅黑"><big>(.pnml)</big></font></label>
                                         	</div>
                                         <div class='row-fluid'>
										<div class='span10 box'>
											<div class='row-fluid'>
													<div class='span10'>
														 <div class='pull-left'>
										                	<a class='btn btn-success fullfill-items' id="btnselectfromDB1"  data-toggle='modal' href='#showModelsInDB1' role='button'>库中导入</a>
										                    <a class='btn btn-success fullfill-items' data-toggle='modal' href='#uploadModelFile1' role='button'>本地上传</a>
										                </div>
										                <div class='pull-right'>
										                	<a class='btn btn-success fullfill-items' id='btnremoveprocesses' role='button'>删除选中</a>
										                </div>
														
														<div class='clearfix'></div>
														 <hr class='hr-normal' />
									           
									            		<div id='tree2'>要计算的模型</div>
															
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
                                          					<font face="微软雅黑">拓扑规模</font>
                                          				</div>
                                          				<div class='actions'>
                                          					<a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                          				</div>
                                          			</div>
                                          			<div class='box-content'>
                                          				<div class='control-group'>
	                                          				<div class='text-left'>
		                                                		
		                                                		<font face="微软雅黑"><p>*&nbsp;拓扑规模的一些属性</p></font>
		                                                		
	                                             			</div>
                                          					
                                          				</div>
                                          				<hr class='hr-normal' />
                                           			<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="alltuopu" type='checkbox' id='check_0_all' value='0' />
		                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
                                             				</div>
                                             			</div>
                                              			</div>
                                             		</div>
                                           			<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="tuopu" type='checkbox' id='check_0' value='0' />
                                                		<b><big>&nbsp;变迁数量</big></b>
                                             				</div>
                                             			</div>
                                            				<div class='span12 box'>
                                               		<div class='box-content box-padding'>
                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>变迁</font>的数量</p></font>
                                               		</div>
                                              		</div>
                                              	</div>
                                             	</div>
                                             	<hr class='hr-normal' />
                                             	<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="tuopu" type='checkbox' id='check_1' value='1' />
                                                		<b><big>&nbsp;库所数量</big></b>
                                             				</div>
                                             			</div>
                                            				<div class='span12 box'>
                                               		<div class='box-content box-padding'>
                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>库所</font>的数量</p></font>
                                               		</div>
                                              		</div>
                                              	</div>
                                             	</div>
                                             	<hr class='hr-normal' />
                                             	<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="tuopu"  type='checkbox' id='check_2' value='2' />
                                                		<b><big>&nbsp;边数量</big></b>
                                             				</div>
                                             			</div>
                                            				<div class='span12 box'>
                                               		<div class='box-content box-padding'>
                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>边</font>的数量</p></font>
                                               		</div>
                                              		</div>
                                              	</div>
                                             	</div>
                                             	<hr class='hr-normal' />
                                             	<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="tuopu"  type='checkbox' id='check_3' value='3' />
                                                		<b><big>&nbsp;边密度</big></b>
                                             				</div>
                                             			</div>
                                            				<div class='span12 box'>
                                               		<div class='box-content box-padding'>
                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>边</font>的密度</p></font>
                                               		</div>
                                              		</div>
                                              	</div>
                                             	</div>
                                             	<hr class='hr-normal' />
                                             	<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="tuopu"  type='checkbox' id='check_4' value='4' />
                                                		<b><big>&nbsp;最大入度</big></b>
                                             				</div>
                                             			</div>
                                            				<div class='span12 box'>
                                               		<div class='box-content box-padding'>
                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>最大入度</font></p></font>
                                               		</div>
                                              		</div>
                                              	</div>
                                             	</div>
                                             	<hr class='hr-normal' />
                                             	<div class='control-group'>
                                           				<div class='row-fluid'>
                                           					<div class='span2'>
                                             				<div class='text-left'>
                                                		<input name="tuopu"  type='checkbox' id='check_5' value='5' />
                                                		<b><big>&nbsp;最大出度</big></b>
                                             				</div>
                                             			</div>
                                            				<div class='span12 box'>
                                               		<div class='box-content box-padding'>
                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>最大出度</font></p></font>
                                               		</div>
                                              		</div>
                                              	</div>
                                             	</div>
                                             	<hr class='hr-normal' />
                                          			</div>
                                          		</div>
                                          	</div>
                                         	</div>
                                                      <div class='control-group'>
                                                          <div class='row-fluid'>
                                                              <div class='span12 box bordered-box blue-border'>
                                                                  <div class='box-header orange-background'>
                                                                      <div class='title'>
                                                                          <font face="微软雅黑">And-Split</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*统计And-Split类型的结构</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allandsplit" type='checkbox' id='check_1_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andsplit"  type='checkbox' id='check_7' value='7' />
						                                                		<b><big>&nbsp;数量</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Split数量</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andsplit"  type='checkbox' id='check_8' value='8' />
						                                                		<b><big>&nbsp;最小度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Split的最小度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andsplit"   type='checkbox' id='check_9' value='9' />
						                                                		<b><big>&nbsp;最大度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Split的最大度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andsplit"  type='checkbox' id='check_10' value='10' />
						                                                		<b><big>&nbsp;平均度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Split的平均度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andsplit"  type='checkbox' id='check_11' value='11' />
						                                                		<b><big>&nbsp;度的标准差</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Split的度的标准差</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                  </div>
                                                                  </div>
                                                              </div>
                                                      	</div>
                                                      </div>
                                                       <div class='control-group'>
                                                          <div class='row-fluid'>
                                                              <div class='span12 box bordered-box blue-border'>
                                                                  <div class='box-header purple-background'>
                                                                      <div class='title'>
                                                                          <font face="微软雅黑">And-Join</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*统计And-Join类型的结构</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allandjoin" type='checkbox' id='check_2_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andjoin"  type='checkbox' id='check_12' value='12' />
						                                                		<b><big>&nbsp;数量</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Join数量</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andjoin"  type='checkbox' id='check_13' value='13' />
						                                                		<b><big>&nbsp;最小度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Join的最小度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andjoin"  type='checkbox' id='check_14' value='14' />
						                                                		<b><big>&nbsp;最大度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Join的最大度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andjoin"  type='checkbox' id='check_15' value='15' />
						                                                		<b><big>&nbsp;平均度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Join的平均度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="andjoin"  type='checkbox' id='check_16' value='16' />
						                                                		<b><big>&nbsp;度的标准差</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>And-Join的度的标准差</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                  </div>
                                                                  </div>
                                                              </div>
                                                      	</div>
                                                      </div>
                                                      
                                                      
                                                       <div class='control-group'>
                                                          <div class='row-fluid'>
                                                              <div class='span12 box bordered-box blue-border'>
                                                                  <div class='box-header green-background'>
                                                                      <div class='title'>
                                                                          <font face="微软雅黑">Xor-Split</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*统计Xor-Split类型的结构</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allxorsplit" type='checkbox' id='check_3_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorsplit"  type='checkbox' id='check_17' value='17' />
						                                                		<b><big>&nbsp;数量</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Split数量</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorsplit" type='checkbox' id='check_18' value='18' />
						                                                		<b><big>&nbsp;最小度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Split的最小度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorsplit"  type='checkbox' id='check_19' value='19' />
						                                                		<b><big>&nbsp;最大度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Split的最大度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorsplit"  type='checkbox' id='check_20' value='20' />
						                                                		<b><big>&nbsp;平均度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Split的平均度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorsplit"  type='checkbox' id='check_21' value='21' />
						                                                		<b><big>&nbsp;度的标准差</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Split的度的标准差</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
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
                                                                          <font face="微软雅黑">Xor-Join</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*统计Xor-Join类型的结构</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allxorjoin" type='checkbox' id='check_4_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorjoin"  type='checkbox' id='check_22' value='22' />
						                                                		<b><big>&nbsp;数量</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Join数量</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorjoin"  type='checkbox' id='check_23' value='23' />
						                                                		<b><big>&nbsp;最小度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Join的最小度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorjoin"  type='checkbox' id='check_24' value='24' />
						                                                		<b><big>&nbsp;最大度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Join的最大度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorjoin"  type='checkbox' id='check_25' value='25' />
						                                                		<b><big>&nbsp;平均度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Join的平均度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="xorjoin"  type='checkbox' id='check_26' value='26' />
						                                                		<b><big>&nbsp;度的标准差</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Xor-Join的度的标准差</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                  </div>
                                                                  </div>
                                                              </div>
                                                      	</div>
                                                      </div>
                                                      
                                                      
                                                       <div class='control-group'>
                                                          <div class='row-fluid'>
                                                              <div class='span12 box bordered-box blue-border'>
                                                                  <div class='box-header purple-background'>
                                                                      <div class='title'>
                                                                          <font face="微软雅黑">连接器信息</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*统计连接器的信息</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allconnection" type='checkbox' id='check_5_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_28' value='28' />
						                                                		<b><big>&nbsp;AND-XOR Mis</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中连接器<font color='#49bf67'>AND-XOR Mis</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_29' value='29' />
						                                                		<b><big>&nbsp;Sequentiality</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中连接器<font color='#49bf67'>Sequentiality</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_30' value='30' />
						                                                		<b><big>&nbsp;TS</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中连接器<font color='#49bf67'>TS</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_31' value='31' />
						                                                		<b><big>&nbsp;CH</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>CH</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_32' value='32' />
						                                                		<b><big>&nbsp;CFC</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>CFC</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_38' value='38' />
						                                                		<b><big>&nbsp;最大度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>最大度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_39' value='39' />
						                                                		<b><big>&nbsp;平均度</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>平均度</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="connection"  type='checkbox' id='check_40' value='40' />
						                                                		<b><big>&nbsp;Depth</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Depth</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	
                                                                  </div>
                                                                  </div>
                                                              </div>
                                                      	</div>
                                                      </div>
                                                      
                                                      
                                                       <div class='control-group'>
                                                          <div class='row-fluid'>
                                                              <div class='span12 box bordered-box blue-border'>
                                                                  <div class='box-header green-background'>
                                                                      <div class='title'>
                                                                          <font face="微软雅黑">结构行为</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*结构行为</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allstructure" type='checkbox' id='check_6_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="structure"  type='checkbox' id='check_33' value='33' />
						                                                		<b><big>&nbsp;CYC</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>CYC</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="structure"  type='checkbox' id='check_6' value='6' />
						                                                		<b><big>&nbsp;tars的数量</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>tars的数量</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="structure"  type='checkbox' id='check_35' value='35' />
						                                                		<b><big>&nbsp;separability</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>separability</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="structure"  type='checkbox' id='check_36' value='36' />
						                                                		<b><big>&nbsp;stucturedness</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>stucturedness</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="structure"  type='checkbox' id='check_37' value='37' />
						                                                		<b><big>&nbsp;CNC</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>CNC</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="structure"  type='checkbox' id='check_27' value='27' />
						                                                		<b><big>&nbsp;状态数量</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>状态数量</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	
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
                                                                          <font face="微软雅黑">子结构</font>
                                                                      </div>
                                                                      <div class='actions'>
                                                                          <a href="#" class="btn box-collapse btn-mini btn-link"><i></i></a>
                                                                      </div>
                                                                  </div>
                                                                  <div class='box-content'>
                                                                      <div class='control-group'>
                                                                          <font face="微软雅黑"><p>*统计子结构的信息</p></font>
                                                                      </div>
                                                                      <hr class='hr-normal' />
                                                                      <div class='control-group'>
					                                           				<div class='row-fluid'>
					                                           					<div class='span2'>
					                                             				<div class='text-left'>
					                                                		<input name="allchild" type='checkbox' id='check_7_all' value='0' />
							                                                		<b><big><font color='#3a87ad'>&nbsp;全选</font></big></b>
					                                             				</div>
					                                             			</div>
					                                              			</div>
					                                             		</div>
                                                                      <div class='control-group'>
                                                                         <div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_41' value='41' />
						                                                		<b><big>&nbsp;不可见任务</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>不可见任务</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
                                                                      
                                                                      	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_42' value='42' />
						                                                		<b><big>&nbsp;重复任务数</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>重复任务数</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_43' value='43' />
						                                                		<b><big>&nbsp;非自由选择数</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>非自有选择数</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_44' value='44' />
						                                                		<b><big>&nbsp;绝对循环数</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>绝对循环数</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_45' value='45' />
						                                                		<b><big>&nbsp;Or-Join数</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>Or-Join数</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_46' value='46' />
						                                                		<b><big>&nbsp;简单循环数</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>简单循环数</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_47' value='47' />
						                                                		<b><big>&nbsp;嵌套循环数</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>嵌套循环数</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
						                                             	<div class='control-group'>
						                                           				<div class='row-fluid'>
						                                           					<div class='span2'>
						                                             				<div class='text-left'>
						                                                		<input name="child"  type='checkbox' id='check_34' value='34' />
						                                                		<b><big>&nbsp;直径</big></b>
						                                             				</div>
						                                             			</div>
						                                            				<div class='span12 box'>
						                                               		<div class='box-content box-padding'>
						                                                 	<font face="微软雅黑"><p>选中后将会统计模型中<font color='#49bf67'>直径</font></p></font>
						                                               		</div>
						                                              		</div>
						                                              	</div>
						                                             	</div>
						                                             	<hr class='hr-normal' />
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
                                        	
                                        	
                                            <div class='span3 offset4'>
                                            	<div class='text-center'>
                                             	<div class='btn btn-primary' id='btn_caclulate'>开始</div>
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
                                         	 <font color="#00acec" face="微软雅黑">努力校验中...</font>
                                        </div>
                                    </div>
                                    <div class='box-content'>
				<img alt="1" src="assets/images/ajax-loaders/19.gif" />
			</div>
		</div>
	</div>
</div>
<div class='row-fluid'>
	<div class='span12 box bordered-box' style='margin-bottom:0;'>
		<div class='box-header'>
    		<div class='title'><font color="#00acec" face="微软雅黑">计算结果</font></div>
   				<div class='actions'>
        		<a href="#" class="btn box-remove btn-mini btn-link"><i class='icon-remove'></i>
        		</a>
        		<a href="#" class="btn box-collapse btn-mini btn-link"><i></i>
        		</a>
    		</div>
		</div>
		<div class='box-content box-no-padding'>
			<div id="calculateResult">
				<h1>等待数据</h1>
			</div>
		</div>
	</div>
</div>



<!-- 从模型库中导入代码 -->
<div class='modal hide fade' id='showModelsInDB1' role='dialog' tabindex='-1'>
		<div class='modal-header'>
			<button class='close' data-dismiss='modal' type='button'>&times;</button>
			<h3>选择模型</h3>
		</div>
		<div class='modal-body'>
			
			<div class='control-group'>
				<label class='control-label' for='inputText1'>选择模型库</label>
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
			<button class='btn' data-dismiss='modal'>Close</button>
			<button id='btConfirmMove1' class='btn btn-primary' onClick="confirmQueryByModelInDB1()">Confirm</button>
		</div>
 </div>
 
<!--  本地上传代码 -->
 <div class='modal hide fade' id='uploadModelFile1' role='dialog' tabindex='-1'>
	<div class='modal-header'>
		<button class='close' data-dismiss='modal' type='button'>&times;</button>
		<h3>请上传查询的模型</h3>
	</div>
	<div class='modal-body'>
		<form accept-charset="UTF-8" action="#" class="form form-horizontal" enctype="multipart/form-data" method="post" style="margin-bottom: 0;">
		<div class='span10'>
			<span class='btn btn-success fileinput-button'> 
			<i class='icon-plus icon-white'></i> <span>Add files...</span> 
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
		<button class='btn' data-dismiss='modal'>Close</button>
		<button class='btn btn-primary'  data-dismiss='modal'>Confirm</button>
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


<!-- / page script -->
<script src='assets/javascripts/modelStatistics/modelStatistics.js' type='text/javascript'></script>
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