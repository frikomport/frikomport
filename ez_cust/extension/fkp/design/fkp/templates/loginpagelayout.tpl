<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//NO"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="no" lang="no">

<head>

<style type="text/css">
    @import url({"stylesheets/frikomport.css"|ezdesign});
    @import url({"stylesheets/frikomlogo.css"|ezdesign});
    @import url({"stylesheets/custom.css"|ezdesign});
</style>

{section name=JavaScript loop=ezini( 'JavaScriptSettings', 'JavaScriptList', 'design.ini' ) 
}
    <script language="JavaScript" type="text/javascript" src={concat( 'javascript/',$:item 
)|ezdesign}></script>
{/section}

{literal}
<!--[if IE 6.0]>
<style>
.maincontent-design-full{ float: right; }
</style>
<![endif]-->
{/literal}

{include uri="design:page_head.tpl"}

</head>

<body>
	
{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{default current_user=fetch('user','current_user')}
		
<div id="allcontent">
    <div id="topcontent">
    {section show=ezini('Toolbar_top','Tool','toolbar.ini')|count}
        <div id="toolbar-top">
	<a href={"/"|ezurl}>{'Main page'|i18n( 'extension/fkp/layout')}</a> 
	{include uri="design:toolbar.tpl"}
	| {tool_bar name=top view=line}
        </div>{* id="toolbar-top" *}
    {/section}
{cache-block keys=$uri_string}
        {let pagedesign=fetch_alias(by_identifier,hash(attr_id,sitestyle_identifier))}

    <div id="toolbar-design">
	<div id="toolbar-design-left"><div id="toolbar-design-right"></div></div>
	 <a href={"/"|ezroot}><img src={"logo.gif"|ezimage} border="0" class="logo" 
alt="Company logo" /></a>

{include uri="design:navigation.tpl"}

{include uri="design:logo.tpl"}

<form action={"/content/search/"|ezurl} method="get" class="searchForm">
	<input class="searchField" type="text" size="10" name="SearchText" value="" />
	<input type="image" class="btnSearch" src={"btn_search.gif"|ezimage} />
</form>	 
 </div>{* toolbar-design *}
	 <div id="bar"></div>
</div>{* topcontent *}					 

        {/let}

{/cache-block}






    



    {cache-block keys=array($uri_string, $current_user.role_id_list|implode( ',' ), 
$current_user.limited_assignment_value_list|implode( ',' ))}
        <div class="break"></div>
    {/cache-block}
{def $current_user_link=$current_user.contentobject.main_node.url_alias|ezurl}

  <div id="path-design">Du er her: {include uri="design:parts/path.tpl"}
      <div id="user-design">
{if $current_user}
{"You are logged in as"|i18n("extension/fkp/layout")} {*<a href={$current_user_link}>*}
{$current_user.contentobject.name|wash}{*</a>*} 
	{if $current_user.is_logged_in}
	[<a href={"/user/logout"|ezurl}>{"Logout"|i18n("design/standard/toolbar")}</a>]
	{/if}

{/if}
		</div>{* user-design *}
  </div>{* id="path-design" *}

  <div id="maincontent">
    {cache-block keys=array($uri_string, $current_user.role_id_list|implode( ',' ), 
$current_user.limited_assignment_value_list|implode( ',' ))}
        {menu name=LeftMenu}
    {/cache-block}

	
	{if $uri_string|begins_with('fkp/')|not}
	<div id="rightContainer" class="rightContainer">
	  {section show=ezini( 'Toolbar_right', 'Tool', 'toolbar.ini' )|count}
			 {tool_bar name=right view=full}
	  {/section}
	</div>
	{/if}
	

	{if $uri_string|begins_with('fkp/exthttp')}
	{def $maincontentstyle_styleattrib = '-full'}
	{/if}
	
	<div id="maincontent-design" class="maincontent-design{if is_set($maincontentstyle_styleattrib)}{$maincontentstyle_styleattrib}{/if}">
	{section show=$warning_list}
		{include uri="design:page_warning.tpl"}
	{/section}
	{$module_result.content}
	</div>

  </div>{* maincontent *}
  
  {include uri="design:footer.tpl"}
<!--DEBUG_REPORT-->
  {include uri="design:tracker.tpl"}
</body>
</html>