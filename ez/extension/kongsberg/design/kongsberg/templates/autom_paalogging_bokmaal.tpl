{set-block scope=root variable=cache_ttl}0{/set-block} 
	{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{def $current_user=fetch('user','current_user')}

		{if eq($ip['ip_address'], '85.221.60.70')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_numedal">        
				<input type="hidden" name="Password" class="textField2" value="numedal">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}

		{if eq($ip['ip_address'], '81.31.227.162')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_hjartdal">        
				<input type="hidden" name="Password" class="textField2" value="ansatth1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}

		{if eq($ip['ip_address'], '81.0.145.236')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >
				<input type="hidden" name="Login" class="textField" value="ansatt_ovre_eiker">
				<input type="hidden" name="Password" class="textField2" value="ansatto1">
				<noscript>
				<input type="submit" value="continue to login">
				</noscript>
				</form>
				</body>
            {/if}
		{/if}

		{if eq($ip['ip_address'], '81.0.188.42')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt_ovre_eiker">        
				<input type="hidden" name="Password" class="textField2" value="ansatto1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}

		{if eq($ip['ip_address'], '93.187.80.103')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt_ovre_eiker">        
				<input type="hidden" name="Password" class="textField2" value="ansatto1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}

		{if eq($ip['ip_address'], '81.31.234.140')}
				{if eq($httpAddress['firstchunkofhttpaddress'], 'www.tinn.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_tinn">        
				<input type="hidden" name="Password" class="textField2" value="ansattt1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
			{/if}
		{/if}

		
		{if eq($ip['ip_address'], '194.143.84.109')}	
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_kongsberg">        
				<input type="hidden" name="Password" class="textField2" value="ansattkk1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}

		{if eq($ip['ip_address'], '194.143.84.100')}	
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_kongsberg">        
				<input type="hidden" name="Password" class="textField2" value="ansattkk1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}
		
		{if eq($ip['ip_address'], '62.92.250.78')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_kongsberg">        
				<input type="hidden" name="Password" class="textField2" value="ansattkk1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}
		
		{if eq($ip['ip_address'], '93.187.86.36')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_notodden">        
				<input type="hidden" name="Password" class="textField2" value="ansattn1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}
		
		{if eq($ip['ip_address'], '93.187.80.101')}
			{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
				<input type="hidden" name="Login" class="textField" value="ansatt_notodden">        
				<input type="hidden" name="Password" class="textField2" value="ansattn1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
			{/if}
		{/if}

{attribute_view_gui attribute=$node.object.data_map.body}

<body onLoad="document.kurslista.submit();">	
	<form method="post" action={"fkp/exthttp?url=/listCourses.html"|ezurl}  name="kurslista">	 
	<noscript>
	<input type="submit" value="continue to login"> 
	</noscript>
	</form>
</body><!--Debug-logginn 3-->
{undef}
{undef}
{undef}