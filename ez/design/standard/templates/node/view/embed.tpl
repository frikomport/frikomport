{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{default node_name=$node.name node_url=$node.url_alias}{if $node_url}<a href={$node_url|ezurl}>{/if}{$node_name|wash}{if $node_url}</a>{/if}{/default}
