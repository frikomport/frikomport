{set $custom_attribute_classes = $custom_attribute_classes|append('email')}
<input type="text" name="{$custom_attribute}" id="{$custom_attribute_id}_source" value="{$custom_attribute_default|wash}"{if $custom_attribute_disabled} disabled="disabled"{/if} class="{$custom_attribute_classes|implode(' ')}" title="{$custom_attribute_title|wash}" />
