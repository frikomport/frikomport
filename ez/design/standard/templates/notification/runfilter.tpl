{* DO NOT EDIT THIS FILE! Use an override template instead. *}
<form method="post" action={"/notification/runfilter/"|ezurl}>

<h1>{'Notification admin'|i18n('design/standard/notification')}</h1>

{if $filter_proccessed}
<div class="feedback">
{'Notification filter processed all available notification events'|i18n('design/standard/notification')}
</div>
{/if}

{if $time_event_created}
<div class="feedback">
{'Time event was spawned'|i18n('design/standard/notification')}
</div>
{/if}

<h2>{'Run notification filter'|i18n('design/standard/notification')}</h2>

<input type="submit" name="RunFilterButton" value="{'Run'|i18n('design/standard/notification')}" />

<h2>{'Spawn time event'|i18n('design/standard/notification')}</h2>

<input type="submit" name="SpawnTimeEventButton" value="{'Spawn'|i18n('design/standard/notification')}" />
</form>
