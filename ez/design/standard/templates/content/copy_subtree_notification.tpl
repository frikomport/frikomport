{* DO NOT EDIT THIS FILE! Use an override template instead. *}

<div class="context-block">
{* DESIGN: Header START *}<div class="box-header"><div class="box-tc"><div class="box-ml"><div class="box-mr"><div class="box-tl"><div class="box-tr">

<h1 class="context-title">{'Copy subtree Notification'|i18n( 'design/standard/content/copy_subtree_notification')}</h1>

{* DESIGN: Mainline *}<div class="header-mainline"></div>

{* DESIGN: Header END *}</div></div></div></div></div></div>

{* DESIGN: Content START *}<div class="box-ml"><div class="box-mr"><div class="box-content">

<div class="block">
{section show=$notifications.Warnings|count|gt(0)}
<h4>Warnings:</h4>{section loop=$notifications.Warnings}<br/>{$item}{/section}
<hr/>
{/section}

{section show=$notifications.Errors|count|gt(0)}
<h4>Errors:</h4>{section loop=$notifications.Errors}<br/>{$item}{/section}
<hr/>
{/section}

{section show=$notifications.Notifications|count|gt(0)}
{section loop=$notifications.Notifications}<br/>{$item}{/section}
{/section}

</div>

{* DESIGN: Content END *}</div></div></div>

<form action={$redirect_url|ezurl} method="post">

<div class="controlbar">
{* DESIGN: Control bar START *}<div class="box-bc"><div class="box-ml"><div class="box-mr"><div class="box-tc"><div class="box-bl"><div class="box-br">
<div class="block">
    <input class="button" type="submit" value="{'OK'|i18n( 'design/standard/content/copy_subtree_notification' )}" />
</div>
{* DESIGN: Control bar END *}</div></div></div></div></div></div>
</div>

</form>
</div>
