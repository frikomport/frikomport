{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{if $attribute.content.is_valid}
  {pdf(text, concat($attribute.content.year,'.',$attribute.content.month,'.',$attribute.content.day,' ',$attribute.content.hour,':',$attribute.content.minute)|wash(pdf))}
{/if}
