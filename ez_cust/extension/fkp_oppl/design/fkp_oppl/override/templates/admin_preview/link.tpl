{* Link - Admin preview *}
<div class="content-view-full">
    <div class="class-link">

    <h1>{$node.name|wash}</h1>

    {* Description. *}
    {section show=$node.object.data_map.description.has_content}
        <div class="attribute-long">
            {attribute_view_gui attribute=$node.object.data_map.description}
        </div>
    {/section}

    {* URL/Link. *}
    {section show=$node.object.data_map.location.has_content}
        <div class="attribute-link">
            {section show=$node.object.data_map.location.data_text}
            <p><a href="{$node.object.data_map.location.content}">{$node.object.data_map.location.data_text}</a></p>
            {section-else}
            <p><a href="{$node.object.data_map.location.content}">{$node.object.data_map.location.content}</a></p>
            {/section}
        </div>
    {/section}

    </div>
</div>
