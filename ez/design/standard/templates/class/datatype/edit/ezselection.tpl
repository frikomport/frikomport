{* DO NOT EDIT THIS FILE! Use an override template instead. *}

<div class="block">
<label>{'Style'|i18n( 'design/standard/class/datatype' )}:</label>
<select name="ContentClass_ezselection_ismultiple_value_{$class_attribute.id}">
<option value="0" {if not( $class_attribute.data_int1 )}selected="selected"{/if}>{'Single choice'|i18n( 'design/standard/class/datatype' )}</option>
<option value="1" {if $class_attribute.data_int1}selected="selected"{/if}>{'Multiple choice'|i18n( 'design/standard/class/datatype' )}</option>
</select>
</div>

<div class="block">
<fieldset>
<legend>{'Options'|i18n( 'design/standard/class/datatype' )}</legend>
{section show=$class_attribute.content.options}
<table class="list" cellspacing="0">
<tr>
    <th class="tight">&nbsp;</th>
    <th>{'Option'|i18n( 'design/standard/class/datatype' )}</th>
</tr>
{section name=Option loop=$class_attribute.content.options}
<tr>
    {* Remove. *}
    <td><input type="checkbox" name="ContentClass_ezselection_option_remove_array_{$class_attribute.id}[{$Option:item.id}]" value="1" title="{'Select option for removal.'|i18n( 'design/standard/class/datatype' )}" /></td>

    {* Option. *}
    <td><input class="box" type="text" name="ContentClass_ezselection_option_name_array_{$class_attribute.id}[{$Option:item.id}]" value="{$Option:item.name|wash}" /></td>
</tr>
{/section}
</table>
{section-else}
<p>{'There are no options.'|i18n( 'design/standard/class/datatype' )}</p>
{/section}

{* Buttons. *}
{if $class_attribute.content.options}
<input class="button" type="submit" name="ContentClass_ezselection_removeoption_button_{$class_attribute.id}" value="{'Remove selected'|i18n( 'design/standard/class/datatype' )}" title="{'Remove selected options.'|i18n( 'design/standard/class/datatype' )}" />
{else}
<input class="button-disabled" type="submit" name="ContentClass_ezselection_removeoption_button_{$class_attribute.id}" value="{'Remove selected'|i18n( 'design/standard/class/datatype' )}" disabled="disabled" />
{/if}

<input class="button" type="submit" name="ContentClass_ezselection_newoption_button_{$class_attribute.id}" value="{'New option'|i18n( 'design/standard/class/datatype' )}" title="{'Add a new option.'|i18n( 'design/standard/class/datatype' )}" />
</fieldset>
</div>
