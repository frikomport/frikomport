{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{if $attribute.has_content}
{$attribute.content.name|wash( xhtml )}
{else}
{'None'|i18n( 'design/standard/content/datatype' )}
{/if}
