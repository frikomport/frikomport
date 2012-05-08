{* DO NOT EDIT THIS FILE! Use an override template instead. *}

<div class="block">
  <label>{'Ini file'|i18n( 'design/standard/class/datatype' )}:</label>
  <input class="box" type="text" name="ContentClass_ezinisetting_file_{$class_attribute.id}" value="{$class_attribute.data_text1|wash}" size="30" maxlength="50">
</div>

<div class="block">
  <label>{'Ini Section'|i18n( 'design/standard/class/datatype' )}:</label>
  <input class="box" type="text" name="ContentClass_ezinisetting_section_{$class_attribute.id}" value="{$class_attribute.data_text2|wash}" size="30" maxlength="50">
</div>

<div class="block">
  <label>{'Ini Parameter'|i18n( 'design/standard/class/datatype' )}:</label>
  <input class="box" type="text" name="ContentClass_ezinisetting_parameter_{$class_attribute.id}" value="{$class_attribute.data_text3|wash}" size="30" maxlength="50">
</div>

<div class="block">
  <label>{'Ini file location'|i18n( 'design/standard/class/datatype' )}:</label>
  <select name="ContentClass_ezinisetting_ini_instance_{$class_attribute.id}[]" size="5" multiple="multiple" >
    {let selectedSiteAccess=$class_attribute.data_text4|explode( ';' )}
      {section name=SiteAccess loop=$class_attribute.data_text5|explode( ';' )}
        <option value="{$:index}" {if $selectedSiteAccess|contains( $:index )}selected="selected"{/if}>
          {$:item|wash}
        </option>
      {/section}
    {/let}
  </select>
</div>

<div class="element">
  <label>{'Ini setting type'|i18n( 'design/standard/class/datatype' )}:</label>
  <select name="ContentClass_ezinisetting_type_{$class_attribute.id}">
    <option value="1" {if $class_attribute.data_int1|eq( 1 )}selected="selected"{/if}>{'Text'|i18n( 'design/standard/class/datatype' )}</option>
    <option value="2" {if $class_attribute.data_int1|eq( 2 )}selected="selected"{/if}>{'Enable/Disable'|i18n( 'design/standard/class/datatype' )}</option>
    <option value="3" {if $class_attribute.data_int1|eq( 3 )}selected="selected"{/if}>{'True/False'|i18n( 'design/standard/class/datatype' )}</option>
    <option value="4" {if $class_attribute.data_int1|eq( 4 )}selected="selected"{/if}>{'Integer'|i18n( 'design/standard/class/datatype' )}</option>
    <option value="5" {if $class_attribute.data_int1|eq( 5 )}selected="selected"{/if}>{'Float'|i18n( 'design/standard/class/datatype' )}</option>
    <option value="6" {if $class_attribute.data_int1|eq( 6 )}selected="selected"{/if}>{'Array'|i18n( 'design/standard/class/datatype' )}</option>
  </select>
</div>

</div>
