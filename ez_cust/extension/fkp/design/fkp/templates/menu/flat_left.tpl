<div id="leftmenu">
<div id="leftmenu-design">


{def $item=fetch('content', 'node', hash('node_id', 2))}

{let docs=treemenu( $module_result.path,
                    is_set( $module_result.node_id )|choose( 93, $module_result.node_id ),
                    ezini( 'MenuContentSettings', 'LeftIdentifierList', 'menu.ini' ),
                    0 , 5 )
                    depth=1
                    last_level=1}
{*
<br/>node_id=[{$module_result.node_id}]<br/>
{$docs|attribute('show', 3)}
<br/>exini=[{ezini( 'MenuContentSettings', 'LeftIdentifierList', 'menu.ini' )|attribute('show')}]
*}


{if $docs|count|gt(0)}
	  {section var=menu loop=$:docs last-value}
			{set last_level=$menu.last|is_array|choose( $menu.level, $menu.last.level )}
			<div class="menuContainer-level-{$menu.level}">
			<a {$menu.is_selected|choose( '', 'class="menuSelected"' )} href={$menu.url_alias|ezurl}>{$menu.text|shorten( 25 )}</a>
			</div>
			{set depth=$menu.level}
	  {/section}
{else}
			<div class="menuContainer-level-0">
			<a href={$item.url_alias|ezurl}>{$item.name|shorten( 25 )}</a>
			</div>
{/if}
    
{def $user=fetch( 'user', 'current_user' )}
{def $roles=fetch( 'user', 'member_of', hash( 'id', $user.contentobject_id ) )}
{foreach $roles as $role}
	{if array( 'Administrator' )|contains( $role.name )}
		{def $isAdmin=true}
	{/if}
	{if array( 'Opplæringsansvarlig','Opplaringsansvarlig' )|contains( $role.name )}
		{def $isOppl=true}
	{/if}
	{if array( 'Kursansvarlig' )|contains( $role.name )}
		{def $isKursansv=true}
	{/if}
	{if array( 'Kommuneansatt', 'Ansatt' )|contains( $role.name )}
		{def $isAnsatt=true}
	{/if}
{/foreach}
{def $notAnonymous = eq(1,$user.login|ne('anonymous'))}

{def $urls=fetch( 'fkp', 'url_alias' )}

	{if $isAdmin}
		<div class="menuContainer-level-0"><a href={'index.php/fkp_admin'|ezroot}>{"Edit news"|i18n("extension/fkp/menu")}</a></div> 
	{/if}
	{if $isOppl}
		<div class="menuContainer-level-0"><a href={'index.php/fkp_oppl'|ezroot}>{"Edit news"|i18n("extension/fkp/menu")}</a></div> 
	{/if}    
    {if or($isAdmin, $isOppl, $isKursansv, and($isAnsatt,$notAnonymous))}
        <div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.profile_URL)|ezurl}>{"My profile"|i18n("extension/fkp/menu")}</a></div> 
	{/if}
	
	<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.course_listURL)|ezurl}>{"Course list"|i18n("extension/fkp/menu")}</a></div> 
	
    {if or($isAdmin, $isOppl, $isKursansv)}
		<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.attendee_listURL)|ezurl}>{"attendee list"|i18n("extension/fkp/menu")}</a></div> 
		<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.Instructor_listURL)|ezurl}>{"Instructor list"|i18n("extension/fkp/menu")}</a></div>
        <div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.Responsible_listURL)|ezurl}>{"Responsible list"|i18n("extension/fkp/menu")}</a></div>  
    {/if}
	
	{if or($isAdmin, $isOppl, $isKursansv)}
		<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.Locations_listURL)|ezurl}>{"Locations list"|i18n("extension/fkp/menu")}</a></div> 
	{/if}
	
	{if or($isAdmin)}
		<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.Commune_listURL)|ezurl}>{"Commune list"|i18n("extension/fkp/menu")}</a></div> 
		<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.Fields_of_service_listURL)|ezurl}>{"Fields of service list"|i18n("extension/fkp/menu")}</a></div> 
	{/if}
	
	{if $isAdmin}
		<div class="menuContainer-level-0"><a href={"index.php/fkp_admin/content/view/full/5"|ezroot}>{"User management"|i18n("extension/fkp/menu")}</a></div> 
	{/if}
	{if $isOppl}
		<div class="menuContainer-level-0"><a href={"index.php/fkp_oppl/content/view/full/5"|ezroot}>{"User management"|i18n("extension/fkp/menu")}</a></div> 
	{/if}  
	{if or($isAdmin, $isOppl)}
        <div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.user_listURL)|ezurl}>{"User list"|i18n("extension/fkp/menu")}</a></div> 
	{/if}

	{if or($isAdmin, $isOppl, $isKursansv)}
		<div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.statistics_url)|ezurl}>{"Statistics"|i18n("extension/fkp/menu")}</a></div>
	{/if}

    {if or($isAdmin)}
        <div class="menuContainer-level-0"><a href={concat($urls.url_alias, $urls.ConfigurationURL)|ezurl}>{"Configuration"|i18n("extension/fkp/menu")}</a></div> 
    {/if}


{/let}

{*
{Tester tilgang til funksjon "fetchURL": [{def $rates=fetch( 'fkp', 'fetchURL' )}{$rates.exthttp_result}]
{undef}
*}

</div>
</div>

