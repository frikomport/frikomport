{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{def $dynamic_csm_disabled = ezini('TreeMenu','Dynamic','contentstructuremenu.ini')|ne('enabled')}

{if or( $dynamic_csm_disabled, $#admin_left_width, $#hide_right_menu )}
<style type="text/css">

{if $hide_right_menu}
    div#maincontent {ldelim} margin-right: 0.4em; {rdelim}
{/if}

{if $admin_left_width}
    {def $left_menu_widths = ezini( 'LeftMenuSettings', 'MenuWidth', 'menu.ini')
         $left_menu_width=$left_menu_widths[$admin_left_width]}
    div#leftmenu {ldelim} width: {$left_menu_width|int}em; {rdelim}
    div#maincontent {ldelim} margin-left: {$left_menu_width|int}.5em; {rdelim}
    {undef $left_menu_widths $left_menu_width}
{/if}

{if $dynamic_csm_disabled}
    div#contentstructure ul#content_tree_menu ul li {ldelim} padding-left: 0; {rdelim}
    div#contentstructure ul#content_tree_menu ul ul {ldelim} margin-left: 20px; {rdelim}
{/if}

</style>
{/if}

{literal}
<!--[if lt IE 8.0]>
<style type="text/css">
div#leftmenu div.box-bc, div#rightmenu div.box-bc { border-bottom: 1px solid #bfbeb6; /* Strange IE bug fix */ }
div#contentstructure { overflow-x: auto; overflow-y: hidden; } /* hide vertical scrollbar in IE */
div.menu-block li { width: 16.66%; } /* Avoid width bug in IE */
div.notranslations li { width: 19%; } /* Avoid width bug in IE */
div.context-user div.menu-block li { width: 14%; } /* Avoid width bug in IE */
input.button, input.button-disabled { padding: 0 0.5em 0 0.5em; overflow: visible; }
input.box, textarea.box { width: 98%; }
td input.box, td textarea.box { width: 97%; }
div#search p.select { margin-top: 0; }
div#search p.advanced { margin-top: 0.3em; }
div.content-navigation div.mainobject-window div.fixedsize { float: none; overflow: scroll; }
div.fixedsize input.box, div.fixedsize textarea.box, div.fixedsize table.list { width: 95%; }
a.openclose img, span.openclose img { margin-right: 4px; }
div#fix { overflow: hidden; }
</style>
<![endif]-->
<!--[if lt IE 6.0]>
<style type="text/css">
div#maincontent div.context-block { width: 100%; } /* Avoid width bug in IE 5.5 */
div#maincontent div#maincontent-design { width: 98%; } /* Avoid width bug in IE 5.5 */
</style>
<![endif]-->
<!--[if IE 6.0]>
<style type="text/css">
div#maincontent div.box-bc { border-bottom: 1px solid #bfbfb7; /* Strange IE bug fix */ }
div#leftmenu-design { margin: 0.5em 4px 0.5em 0.5em; }
</style>
<![endif]-->
{/literal}

<!--[if lt IE 7.0]>
<script type="text/javascript">
    var emptyIcon16 = {'16x16.gif'|ezimage};
    var emptyIcon32 = {'32x32.gif'|ezimage};
</script>
<script type="text/javascript" src={'javascript/tools/eziepngfix.js'|ezdesign}></script>
<![endif]-->
