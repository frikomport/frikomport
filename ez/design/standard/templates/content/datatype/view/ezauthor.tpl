{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{section var=Authors loop=$attribute.content.author_list}
{$Authors.item.name|wash( xhtml )}&nbsp;&lt;{$Authors.item.email|wash( email )}&gt;{delimiter},{/delimiter}
{/section}
