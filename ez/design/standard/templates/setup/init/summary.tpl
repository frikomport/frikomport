{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{*?template charset=latin1?*}
{include uri='design:setup/setup_header.tpl' setup=$setup}

<form method="post" action="{$script}">

<p>
 {"Here you will see a summary of the basic settings for your site. If you are satisfied with the settings you can click the"|i18n("design/standard/setup/init")} <i>{"Setup Database"|i18n("design/standard/setup/init")}</i> {"button."|i18n("design/standard/setup/init")}
</p>
<p>{"However if you want to change your settings click the"|i18n("design/standard/setup/init")} <i>{"Start Over"|i18n("design/standard/setup/init")}</i> {"button which will restart the collecting of information (Existing settings are kept)."|i18n("design/standard/setup/init")}</p>

<fieldset>
<table cellspacing="0" cellpadding="0">
<tr>
  <td colspan="3" class="normal">
    <b>{"Database settings"|i18n("design/standard/setup/init")}</b>
  </td>
</tr>
<tr>
  <td class="normal">
    {"Database"|i18n("design/standard/setup/init")}
  </td>
  <td rowspan="3" class="normal">&nbsp;&nbsp;</td>
  <td class="normal">
    {$database_info.info.name}
  </td>
</tr>
<tr>
  <td class="normal">
    {"Driver"|i18n("design/standard/setup/init")}
  </td>
  <td class="normal">
    {$database_info.info.driver}
  </td>
</tr>
<tr>
  <td class="normal">
    {"Unicode support"|i18n("design/standard/setup/init")}
  </td>
  <td class="normal">
    {$database_info.info.supports_unicode|choose("no"|i18n("design/standard/setup/init"),"yes"|i18n("design/standard/setup/init"))}
  </td>
</tr>

<tr>
  <td colspan="3" class="normal">
    &nbsp;
  </td>
</tr>

<tr>
  <td colspan="3" class="normal">
    <b>{"Language settings"|i18n("design/standard/setup/init")}</b>
  </td>
</tr>
<tr>
  <td class="normal">
    {"Language type"|i18n("design/standard/setup/init")}
  </td>
  <td rowspan="2" class="normal">&nbsp;&nbsp;</td>
  <td class="normal">
  {switch match=$regional_info.language_type}
  {case match=1}
    {"Monolingual"|i18n("design/standard/setup/init")}
  {/case}
  {case match=2}
    {"Multilingual"|i18n("design/standard/setup/init")}
  {/case}
  {case match=3}
    {"Multilingual"|i18n("design/standard/setup/init")}
  {/case}
  {/switch}
  </td>
</tr>
<tr>
  <td class="normal">
    {"Languages"|i18n("design/standard/setup/init")}
  </td>
  <td class="normal">
    {section name=Language loop=$variation_list}
         {if eq($:item.locale_code,$regional_info.primary_language)}
           <b>{$:item.language_name}</b>
         {else}
           {$:item.language_name}
         {/if}
         {if $:item.country_variation}
           [{$:item.language_comment}]
         {/if}
         {delimiter}, {/delimiter}
        {/section}
  </td>
</tr>
</fieldset>
</div>

  <div class="buttonblock">
    <input type="hidden" name="ChangeStepAction" value="" />
    <input class="defaultbutton" type="submit" name="StepButton_8" value="{'Setup Database'|i18n('design/standard/setup/init')} >>" />
    <input class="button" type="submit" name="StepButton_4" value="<< {'Start Over'|i18n('design/standard/setup/init')}" />
  </div>
  {include uri='design:setup/persistence.tpl'}
</form>