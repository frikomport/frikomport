{* DO NOT EDIT THIS FILE! Use an override template instead. *}
{*?template charset=latin1?*}

<div align="center">
  <h1>{"Finished"|i18n("design/standard/setup/init")}</h1>
</div>

<p>
  {"eZ Publish has been installed with your select site setup. You will find the username mentioned in the details below."|i18n("design/standard/setup/init")}
</p>

<blockquote class="note">
<p>
 <b>{"Note"|i18n("design/standard/setup/init")}:</b>
 {"The first time the user or admin site is accessed it will take some time (30 to 60 seconds). This is because eZ Publish prepares the site for your machine."|i18n("design/standard/setup/init")}
</p>
{if $custom_text}
    {foreach $custom_text as $text}
        <p>{$text.note}</p>
    {/foreach}
{/if}
</blockquote>

<fieldset>
<legend>{'Site details'|i18n( 'design/standard/setup/init' )}:</legend>
{*  <table border="0" cellspacing="3" cellpadding="0">
    <tr>

      <td class="setup_site_templates">*}
        <div>
          <a href="{$site_type.url|wash}" target="_other">
            <img class="site-type" src={"design/standard/images/setup/eZ_setup_template_default.png"|ezroot} alt="{$site_type.title|wash}" />
          </a>
        </div>
        <div>
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>{"Title"|i18n("design/standard/setup/init")}:&nbsp;</td>
          <td>{$site_type.title|wash}</td>
        </tr>
        <tr>
          <td>{"URL"|i18n("design/standard/setup/init")}:&nbsp;</td>
          <td><a href="{$site_type.url|wash}" target="_blank" class="setup_final">{"User site"|i18n('design/standard/setup/init')}</a>, <a href="{$site_type.admin_url|wash}" target="_blank" class="setup_final">{"Admin site"|i18n('design/standard/setup/init')}</a></td>
        </tr>
        <tr>
          <td>{"Username"|i18n("design/standard/setup/init")}:&nbsp;</td>
          <td>admin</td>
        </tr>
      </table>
        </div>
{*      </td>

    </tr>

  </table>*}
</fieldset>
