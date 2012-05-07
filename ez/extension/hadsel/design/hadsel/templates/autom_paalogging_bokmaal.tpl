{set-block scope=root variable=cache_ttl}0{/set-block} 

	{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{default current_user=fetch('user','current_user')}

        {def $all_ips=$ip['ip_address']|explode( '.' )}
	        {def $ip_one=$all_ips[0]}
		        {def $ip_two=$all_ips[1]}
			        {def $ip_three=$all_ips[2]}
				        {def $ip_four=$all_ips[3]}

					{if and(eq($ip_one, 159), eq($ip_two, 171), ge($ip_three, 128)) }
						{if $current_user.is_logged_in|not}
							<body onLoad="document.postIt.submit();">
							<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >
								<input type="hidden" name="Login" class="textField" value="ansatt">
								<input type="hidden" name="Password" class="textField2" value="9006tromso">
								<noscript>
									<input type="submit" value="continue to login">
								</noscript>
							</form>
							</body>
						{/if}
					{/if}

		{if eq($ip['ip_address'], '62.63.16.1')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.2')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.3')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.4')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.5')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.6')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.7')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.8')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.9')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.10')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.11')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.12')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.13')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.14')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.15')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.16')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.17')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.18')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.19')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.20')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.21')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.22')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.23')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.24')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.25')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		{if eq($ip['ip_address'], '62.63.16.26')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tromso.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt">        
				<input type="hidden" name="Password" class="textField2" value="9006tromso">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
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


