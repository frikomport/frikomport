{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{default attribute_parameters=array()}
{if $object.main_node_id|null|not}
    <a href={$object.main_node.url_alias|ezurl}>{$object.name|wash}</a>
{else}
    {$object.name|wash}
{/if}
{/default}
