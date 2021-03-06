	 {section show=or($show_subtree|count_chars()|eq(0),
                     fetch(content, node, hash( node_id, $module_result.node_id ) ).path_string|contains( concat( '/', $show_subtree, '/' ) ),
                     $requested_uri_string|begins_with( $show_subtree ))}
    {default current_user=fetch('user','current_user')}
    {cache-block keys=array($tool_id, $current_user.role_id_list|implode( ',' ), $current_user.limited_assignment_value_list|implode( ',' ))}
    {default limit=5}
    {section show=$sort_by|count|eq( 0 )}{set sort_by='published'}{/section}
    {let node_list=cond( $treelist_check|eq( 'yes' ),
                             fetch( content, tree, hash( parent_node_id, $parent_node,
                                    limit, $limit,
                                    class_filter_type, include,
                                    class_filter_array, array( 'article', 'kursinfo' ),
                                    sort_by, array( $sort_by, false() ) ) ),
                         fetch( content, list, hash( parent_node_id, $parent_node,
                                limit, $limit,
                                class_filter_type, include,
                                class_filter_array, array( 'article', 'kursinfo' ),
                                sort_by, array( $sort_by, false() ) ) ) )}
<div id="newsHeader">{$title}</div>
        <div id="newsContainer">
        {section name=Node loop=$node_list sequence=array(bglight,bgdark)}
            {node_view_gui view=listitem content_node=$Node:item}
        {/section}
        </div><!--newsContainer-->

    {/let}
    {/default}
    {/cache-block}
    {/default}
    {/section}
