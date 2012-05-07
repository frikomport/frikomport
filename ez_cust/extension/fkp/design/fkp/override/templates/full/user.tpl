{* User - Full view *}
<div class="content-view-full">

  <h1>{$node.name|wash}</h1>

<div class="attribute">
Fornavn: {attribute_view_gui attribute=$node.object.data_map.first_name}
</div>            

<div class="attribute">
Etternavn: {attribute_view_gui attribute=$node.object.data_map.last_name}
</div>            

<div class="attribute">
Kommune: {attribute_view_gui attribute=$node.object.data_map.kommune}
</div>            

  {section show=$node.object.data_map.image.content.is_valid}
		<div class="attribute">
			 {attribute_view_gui attribute=$node.object.data_map.image}
		</div>
  {/section}


</div>
