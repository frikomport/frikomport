{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{section name=Author loop=$attribute.content.author_list }
{$Author:item.name|wash(xhtml)} - ( {$Author:item.email|wash(email)} )
{delimiter},{/delimiter}
{/section}
