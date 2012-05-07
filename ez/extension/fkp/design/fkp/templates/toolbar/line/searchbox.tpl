{section show=$first}
<ul>
{/section}
<li class="toolbar-item {$placement}"><div class="label">{"Search: "|i18n("design/standard/toolbar/search")}</div><div class="search-line"><form action={"/content/search/"|ezurl} method="get"><input class="searchinput" type="text" size="10" name="SearchText" value="" />
<input type="image" class="searchimage" src={"1x1.gif"|ezimage} />
{section show=$relative_check|eq( 'yes' )}
{section show=is_set( $module_result.content_info.node_id )}
    {let node_id=$module_result.content_info.node_id}
    {section show=$node_id|ne( 2 )}
    <div class="optionblock">
        <input type="radio" class="radiobutton" name="SubTreeArray[]" value="" /><label for="search-global">{'Global'|i18n( 'design/standard/toolbar/search' )}</label>
        <input type="radio" class="radiobutton" name="SubTreeArray[]" value="{$node_id}" checked="checked" /><label for="search-here">{'From here'|i18n( 'design/standard/toolbar/search' )}</label>
    </div>
    {/section}
    {/let}
{/section}
{/section}
</form></div>
</li>
{section show=$last}
</ul>
{/section}
