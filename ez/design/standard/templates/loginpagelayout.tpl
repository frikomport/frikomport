{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{*?template charset=latin1?*}
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="{$site.http_equiv.Content-language|wash}" lang="{$site.http_equiv.Content-language|wash}">

<head>

{section name=JavaScript loop=ezini( 'JavaScriptSettings', 'JavaScriptList', 'design.ini' ) }
    <script language="JavaScript" type="text/javascript" src={concat( 'javascript/',$:item )|ezdesign}></script>
{/section}

    <link rel="stylesheet" type="text/css" href={"stylesheets/core.css"|ezdesign} />
    <link rel="stylesheet" type="text/css" href={"stylesheets/debug.css"|ezdesign} />
<style type="text/css">
{section var=css_file loop=ezini( 'StylesheetSettings', 'CSSFileList', 'design.ini' )}
    @import url({concat( 'stylesheets/',$css_file )|ezdesign});
{/section}
</style>

{include uri="design:page_head.tpl"}

</head>

<body style="background: url(/design/standard/images/grid-background.gif);">

<table class="layout" width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td class="topline" width="40%" colspan="2">
    <img src={"ezpublish-logo.gif"|ezimage} width="210" height="60" alt="" />
   </td>
</tr>
<tr>
    <td class="pathline" colspan="2">

{* Main path START *}

{include uri="design:page_toppath.tpl"}

{* Main path END *}

   </td>
</tr>
<tr>
    <td colspan="2">

<table width="100%" border="0" cellspacing="0" cellpadding="0">

{* Header *}
<tr>
    <td>

<table width="100%"  cellspacing="0" cellpadding="4">
{if $warning_list}
<tr>
  <td colspan="3">
    {include uri="design:page_warning.tpl"}
  </td>
</tr>
{/if}
<tr>
    {* This is the main content *}
    <td width="20%" bgcolor="#ffffff" valign="top">
    </td>
    <td width="30%" bgcolor="#ffffff">
    {$module_result.content}
    </td>
    <td width="50%" bgcolor="#ffffff" valign="top">
    <h2>{"Welcome to eZ Publish administration"|i18n("design/standard/layout")}</h2>
    <p>{"To log in enter a valid login and password."|i18n("design/standard/layout")}</p>
    </td>
</tr>
</table>

    </td>
</tr>
</table>

{include uri="design:page_copyright.tpl"}

<!--DEBUG_REPORT-->

</body>
</html>

