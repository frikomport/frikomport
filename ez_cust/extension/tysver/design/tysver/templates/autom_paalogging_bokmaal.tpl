{set-block scope=root variable=cache_ttl}0{/set-block} 

	{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{default current_user=fetch('user','current_user')}

	{if eq($ip['ip_address'], '192.168.0.10')}
				
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="ez_login">
					<input type="hidden" name="Password" class="textField2" value="ez_password">	
					<noscript>
						<input type="submit" value="continue to login"> 
					</noscript>
				</form>
			</body>
		{/if}
	{/if}


	{attribute_view_gui attribute=$node.object.data_map.body}

	<body onLoad="document.kurslista.submit();">	
		<form method="post" action={"fkp/exthttp?url=/listCourses.html"|ezurl}  name='kurslista'  >	 
 			<noscript>
				<input type="submit" value="continue to login"> 
			</noscript>
		</form>
	</body>

{undef}
{undef}


