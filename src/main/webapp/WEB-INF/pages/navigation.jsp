<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags" %>
<script>

window.onload = function() { 
	//pageFlag
	
	
	
	var flag = pageflag.split('_');
	//toggle-nav-new-current
	var topnavid = "#"+"nav_top_"+flag[0];
	var ulid = "#"+ "ulleft_" + flag[0]
	//in
	var level1id = "level1id_"+flag[1];
	//active
	var level2id = "level2id_"+flag[2];
	
	//top navtigation
	$(topnavid).removeClass('toggle-nav-new');
	$(topnavid).addClass("toggle-nav-new-current");
	
	//leftnavigation
	$(ulid).css('display','block'); 
	//level1nacigation
	
	$(ulid +" "+ "ul[name=level1id_"+flag[1]+"]").addClass("in");
	$(ulid +" "+ "ul[name=level1id_"+flag[1]+"]"+" "+ "li[name=level2id_"+flag[2]+"]").addClass("active");
	
	//$(ulid [name=level1id] [name=level2id]).addClass("active");
	
	
} 	

						
						
</script>

<header>
    <div class='navbar'>
        <div class='navbar-inner'>
            <div class='container-fluid'>
                <a class='brand' href='#'>
                    <span class='hidden-phone'><sp:message code="navigation.title"></sp:message></span>
                </a>
                <a class='toggle-nav btn pull-left' href='#'>
                    <i class='icon-reorder'></i>
                </a>
                 <a class='brand' href='/bpmspace/ProcessManagerIndex'>
                 	
                    <span> </span>
                </a>
                <a id = "nav_top_0" class='toggle-nav-new btn pull-left' href='/bpmspace/ProcessManagerIndex'>
                  
                    <span><sp:message code="navigation.processmanagement"></sp:message></span>
                </a>
                <a id = "nav_top_1" class='toggle-nav-new btn pull-left' href='/bpmspace/ProcessAnalyzeIndex'>
                    <span><sp:message code="navigation.processanalyzer"></sp:message></span>
                </a>
                <a id = "nav_top_2" class='toggle-nav-new btn pull-left' href='../functionmanage/funtionmanageindex.html'>
                    <span><sp:message code="navigation.functionmanagement"></sp:message></span>
                </a>
                <a id = "nav_top_3" class='toggle-nav-new btn pull-left' href='../forum/forum.html'>
                    <span><sp:message code="navigation.technicalforum"></sp:message></span>
                </a>
               
                
                <ul class='nav pull-right'>
                
                	 <li class='dropdown medium only-icon widget'>
                        <a class='dropdown-toggle' data-toggle='dropdown' href='#'>
                           <sp:message code="navigation.language"></sp:message>
                           <b class='caret'></b>
                        </a>
                        <ul class='dropdown-menu'>
                            <li>
                                <a href='?locale=en_US'>
                                    <div class='widget-body'>
                                        <div class='pull-left icon'>
                                            <i class='flag flag-us'></i>
                                        </div>
                                        <div class='pull-left text'>
                                            <sp:message code="navigation.en_US"></sp:message>
                                        </div>
                                    </div>
                                </a>
                                <a href='?locale=zh_CN'>
                                    <div class='widget-body'>
                                        <div class='pull-left icon'>
                                            <i class='flag flag-cn'></i>
                                        </div>
                                        <div class='pull-left text'>
                                            <sp:message code="navigation.zh_CN"></sp:message>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li class='divider'></li>
                            
                        </ul>
                    </li>
                	
                    <li class='dropdown dark user-menu'>
                        <a class='dropdown-toggle' data-toggle='dropdown' href='#'>
                            <img alt='Mila Kunis' height='23' src='assets/images/avatar.jpg' width='23' />
                            <span class='user-name hidden-phone'><%=session.getAttribute("username")%></span>
                            <b class='caret'></b>
                        </a>
                        <ul class='dropdown-menu'>
                            <li>
                                <a href='user_profile.html'>
                                    <i class='icon-user'></i>
                                    <sp:message code="navigation.profile"></sp:message>
                                </a>
                            </li>
                            <li>
                                <a href='user_profile.html'>
                                    <i class='icon-cog'></i>
                                    <sp:message code="navigation.settings"></sp:message>
                                </a>
                            </li>
                            <li class='divider'></li>
                            <li>
                                <a href='user/logout'>
                                    <i class='icon-signout'></i>
                                    <sp:message code="navigation.signout"></sp:message>
                                </a>
                            </li>
                        </ul>
                    </li>
                   
                </ul>
                <form accept-charset="UTF-8" action="search_results.html" class="navbar-search pull-right hidden-phone" method="get" /><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
                    <button class="btn btn-link icon-search" name="button" type="submit"></button>
                    <input autocomplete="off" class="search-query span2" id="q_header" name="q" placeholder="<sp:message code="navigation.search"></sp:message>..." type="text" value="" />
                </form>
            </div>
        </div>
    </div>
</header>

<div id='main-nav-bg'></div>
<nav class='' id='main-nav'>
<div class='navigation'>
<div class='search'>
    <form accept-charset="UTF-8" action="search_results.html" method="get" /><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
        <div class='search-wrapper'>
            <input autocomplete="off" class="search-query" id="q" name="q" placeholder="Search..." type="text" value="" />
            <button class="btn btn-link icon-search" name="button" type="submit"></button>
        </div>
    </form>
</div>
<ul id = "ulleft_0" style ='display:none' class='nav nav-stacked'>
<li class='active'>
    <a href='/bpmspace/ProcessManagerIndex'>
        <i class='icon-dashboard'></i>
        <span><sp:message code="navigation.moduleoverview"></sp:message></span>
    </a>
</li>
<li class=''>
    <a class='dropdown-collapse' href='#'>
        <i class='icon-edit'></i>
        <span><sp:message code="navigation.modelmanagement"></sp:message></span>
        <i class='icon-angle-down angle-down'></i>
    </a>
    <ul name="level1id_0" class='nav nav-stacked'>
        
        <li  name='level2id_1' class=''>
            <a href='#'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.autogeneratemodel"></sp:message></span>
            </a>
        </li>
        <li name='level2id_2' class=''>
            <a href='modelDB'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelrepository"></sp:message></span>
            </a>
        </li>
        
        <li name='level2id_4' class=''>
            <a href='modelquery'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelretrival"></sp:message></span>
            </a>
        </li>
    </ul>
</li>
<li>
    <a class='dropdown-collapse ' href='#'>
        <i class='icon-tint'></i>
        <span><sp:message code="navigation.instancemanagement"></sp:message></span>
        <i class='icon-angle-down angle-down'></i>
    </a>
    	<ul name="level1id_1"  class='nav nav-stacked'>
        <li  name='level2id_0' class=''>
            <a href='#'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.createinstance"></sp:message></span>
            </a>
        </li>
        <li name='level2id_1' class=''>
            <a href='loggenerate'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.autogenerateinstance"></sp:message></span>
            </a>
        </li>
        <li  name='level2id_2' class=''>
            <a href='/bpmspace/instanceDB'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.instancerepository"></sp:message></span>
            </a>
        </li>
       
        <li name='level2id_4' class=''>
            <a href='/bpmspace/instanceSearch'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.instanceretrival"></sp:message></span>
            </a>
        </li>
    </ul>
</li>

<li>
    <a class='dropdown-collapse ' href='#'>
        <i class='icon-cogs'></i>
        <span><sp:message code="navigation.indexmanagement"></sp:message></span>
        <i class='icon-angle-down angle-down'></i>
    </a>
    <ul name="level1id_2"  class='nav nav-stacked'>
        <li name='level2id_0' class=''>
            <a href='modelindex'>
                <i class='icon-bar-chart'></i>
                <span><sp:message code="navigation.modelindex"></sp:message></span>
            </a>
        </li>
        <li name='level2id_1' class=''>
            <a href='caseindex'>
                <i class='icon-envelope'></i>
                <span><sp:message code="navigation.instanceindex"></sp:message></span>
            </a>
        </li>
    </ul>
</li>
</ul>

<ul id="ulleft_1" style ='display:none' class='nav nav-stacked'>
<li class='active'>
    <a href='ProcessManagerIndex.html'>
        <i class='icon-dashboard'></i>
        <span><sp:message code="navigation.moduleoverview"></sp:message></span>
    </a>
</li>
<li class=''>
    <a class='dropdown-collapse' href='#'>
        <i class='icon-edit'></i>
        <span><sp:message code="navigation.processmodelanalyzer"></sp:message></span>
        <i class='icon-angle-down angle-down'></i>
    </a>
    <ul name="level1id_0"  class='nav nav-stacked'>
        
        <li name='level2id_0' class=''>
            <a href='modelFragmentation'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelfragmentation"></sp:message></span>
            </a>
        </li>
        <li name='level2id_1'  class=''>
            <a href='modelSimilarity'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelsimilarity"></sp:message></span>
            </a>
        </li>
        <li name='level2id_2'  class=''>
            <a href='#'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelsimilarityeva"></sp:message></span>
            </a>
        </li>
        <li name='level2id_3'  class=''>
            <a href='modelDifferentiation'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modeldifferentiation"></sp:message></span>
            </a>
        </li>
         <li name='level2id_4'  class=''>
            <a href='modelMerge'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelmerge"></sp:message></span>
            </a>
        </li>
         <li name='level2id_5'  class=''>
            <a href='modelStatistics'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelstatistics"></sp:message></span>
            </a>
        </li>
        <li name='level2id_6'  class=''>
            <a href='modelCheck'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelcheck"></sp:message></span>
            </a>
        </li>
        <li  name='level2id_7'  class=''>
            <a href='#'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelconversion"></sp:message></span>
            </a>
        </li>
        <li  name='level2id_8'  class=''>
            <a href='modelCluster'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.modelcluster"></sp:message></span>
            </a>
        </li>
    </ul>
</li>
<li>
    <a class='dropdown-collapse ' href='#'>
        <i class='icon-tint'></i>
        <span><sp:message code="navigation.processbehaviormonitoring"></sp:message></span>
        <i class='icon-angle-down angle-down'></i>
    </a>
    <ul name="level1id_1" class='nav nav-stacked'>
        <li name='level2id_0'  class=''>
            <a href='#'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.processstatustracking"></sp:message></span>
            </a>
        </li>
        <li name='level2id_1'  class=''>
            <a href='executeefficiencyanalyze'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.executingefficiencyanalysis"></sp:message></span>
            </a>
        </li>
       
    </ul>
</li>

<li>
    <a class='dropdown-collapse ' href='#'>
        <i class='icon-cogs'></i>
        <span><sp:message code="navigation.processinstanceanalyzer"></sp:message></span>
        <i class='icon-angle-down angle-down'></i>
    </a>
    <ul  name="level1id_2" class='nav nav-stacked'>
    	 <li name="level2id_0" class=''>
            <a href='processmining'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.processmining"></sp:message></span>
            </a>
        </li>
        <li name="level2id_1"  class=''>
            <a href='miningalgorithmevaluate'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.miningalgoeva"></sp:message></span>
            </a>
        </li>
        <li name="level2id_2" class=''>
            <a href='socialnetworkmining'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.socialbehavioranalysis"></sp:message></span>
            </a>
        </li>
        <li name="level2id_5" class=''>
            <a href='conformancecheck'>
                <i class='icon-caret-right'></i>
                <span><sp:message code="navigation.conformancecheck"></sp:message></span>
            </a>
        </li>
        <li name="level2id_3" class=''>
            <a href='#'>
                <i class='icon-bar-chart'></i>
                <span><sp:message code="navigation.operationbehavior"></sp:message></span>
            </a>
        </li>
        <li name="level2id_4" class=''>
            <a href='#'>
                <i class='icon-envelope'></i>
                <span><sp:message code="navigation.instancestatistics"></sp:message></span>
            </a>
        </li>
    </ul>
</li>
</ul>
</div>
</nav><!-- 导航结束 -->
