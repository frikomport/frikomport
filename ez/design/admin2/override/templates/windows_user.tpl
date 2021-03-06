{* Preview window *}
{if $preview_enabled}
<div id="node-tab-preview-content" class="tab-content{if $node_tab_index|ne('preview')} hide{else} selected{/if}">
    {include uri='design:preview.tpl'}
<div class="break"></div>
</div>
{/if}

{* Details window *}
<div id="node-tab-details-content" class="tab-content{if $node_tab_index|ne('details')} hide{else} selected{/if}">
    {include uri='design:details.tpl'}
<div class="break"></div>
</div>

{* Translations window *}
<div id="node-tab-translations-content" class="tab-content{if $node_tab_index|ne('translations')} hide{else} selected{/if}">
    {include uri='design:translations.tpl'}
<div class="break"></div>
</div>

{* Locations window *}
<div id="node-tab-locations-content" class="tab-content{if $node_tab_index|ne('locations')} hide{else} selected{/if}">
    {include uri='design:locations.tpl'}
<div class="break"></div>
</div>

{* Relations window *}
<div id="node-tab-relations-content" class="tab-content{if $node_tab_index|ne('relations')} hide{else} selected{/if}">
    {include uri='design:relations.tpl'}
<div class="break"></div>
</div>

{* Member of roles window *}
<div id="node-tab-roles-content" class="tab-content{if $node_tab_index|ne('roles')} hide{else} selected{/if}">
    {include uri='design:roles.tpl'}
<div class="break"></div>
</div>

{* Policy list window *}
<div id="node-tab-policies-content" class="tab-content{if $node_tab_index|ne('policies')} hide{else} selected{/if}">
    {include uri='design:policies.tpl'}
<div class="break"></div>
</div>
