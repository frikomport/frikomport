{* Article - List item view *}

    <p><a href={$node.url_alias|ezurl}>{$node.object.data_map.title.content|wash}</a><br/></p>
{attribute_view_gui attribute=$node.object.data_map.intro}
	 {section show=$node.object.data_map.image.content}
        <div class="attribute-image">
            {attribute_view_gui alignment=right image_class=small attribute=$node.object.data_map.image.content.data_map.image}
        </div>
    {/section}

    {section show=$node.object.data_map.body.content.is_empty|not}
	 <p><div class="newsLink"><a href={$node.url_alias|ezurl}>{"Read more..."|i18n("design/base")}</a></div>
	 </p>
    {/section}
  <p>&nbsp;</p>
