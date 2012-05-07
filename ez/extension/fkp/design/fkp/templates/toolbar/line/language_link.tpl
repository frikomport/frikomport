{*
module_params()|attribute( show )
$module_result|attribute( show )
*}

{if eq($first, 1)}
{else}
 | 
{/if}
<a href={concat('index.php/', $lang_siteaccess, '/', $module_result.content_info.url_alias)|ezroot}>{$text|wash}</a>
{if eq($last, 1)}
{/if}