{* Article - List item view *}
<div class="content-view-listitem">
    <div class="class-article">
	  <h3><a href={$node.url_alias|ezurl}>{$node.object.data_map.kursnavn.content|wash}</a></h3>

	{if is_set( $node.object.data_map.image )}
    {section show=$node.object.data_map.image.content}
        <div class="attribute-image">
            {attribute_view_gui alignment=right image_class=small attribute=$node.object.data_map.image.content.data_map.image}
        </div>
    {/section}
	{/if}
   </div>
</div>