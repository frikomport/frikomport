{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{if $attribute.content.is_valid}
{if $attribute.contentclass_attribute.data_int2|eq(1)}
{$attribute.content.timestamp|l10n(time)}
{else}
{$attribute.content.timestamp|l10n(shorttime)}
{/if}
{/if}
