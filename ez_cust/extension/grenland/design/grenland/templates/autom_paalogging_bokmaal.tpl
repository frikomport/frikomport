{set-block scope=root variable=cache_ttl}0{/set-block} 
	{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{default current_user=fetch('user','current_user')}


	{if eq($ip['ip_address'], '193.214.158.22')}
			
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="Siljan">        
					<input type="hidden" name="Password" class="textField2" value="Siljan">	
					<noscript>
						<input type="submit" value="continue to login"> 
					</noscript>
				</form>
			</body>
		{/if}
	{/if}

	{if eq($ip['ip_address'], '159.130.65.40')}
			
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="Skien">        
					<input type="hidden" name="Password" class="textField2" value="Skien">	
					<noscript>
						<input type="submit" value="continue to login"> 
					</noscript>
				</form>
			</body>
		{/if}
	{/if}

	{if eq($ip['ip_address'], '159.130.65.33')}
			
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="Bamble">        
					<input type="hidden" name="Password" class="textField2" value="Bamble">	
					<noscript>
						<input type="submit" value="continue to login"> 
					</noscript>
				</form>
			</body>
		{/if}
	{/if}

	{if eq($ip['ip_address'], '81.167.214.2')}
			
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="Kragerø">        
					<input type="hidden" name="Password" class="textField2" value="Kragerø">	
					<noscript>
						<input type="submit" value="continue to login"> 
					</noscript>
				</form>
			</body>
		{/if}
	{/if}

	{if eq($ip['ip_address'], '80.239.85.206')}
			
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="Porsgrunn">        
					<input type="hidden" name="Password" class="textField2" value="Porsgrunn">	
					<noscript>
						<input type="submit" value="continue to login"> 
					</noscript>
				</form>
			</body>
		{/if}
	{/if}



	{if eq($ip['ip_address'], '81.31.228.18')}
			
		{if $current_user.is_logged_in|not}
			<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
					<input type="hidden" name="Login" class="textField" value="Drangedal">        
					<input type="hidden" name="Password" class="textField2" value="Drangedal">	
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


