	{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{default current_user=fetch('user','current_user')}

	
		{if eq($ip['ip_address'], '80.203.197.206')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'mail.unified.no')}
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
		{/if}



		{if eq($ip['ip_address'], '80.203.197.206')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'amstel.id.local')}
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



		{if eq($ip['ip_address'], '85.221.60.70')}
				{if eq($httpAddress['firstchunkofhttpaddress'], 'www.nore-og-uvdal.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt_noreuvdal">        
				<input type="hidden" name="Password" class="textField2" value="ansattnu1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
				{/if}
		{/if}

		{if eq($ip['ip_address'], '85.221.60.70')}
				{if eq($httpAddress['firstchunkofhttpaddress'], 'www.rollag.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt_rollag">        
				<input type="hidden" name="Password" class="textField2" value="ansattr1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
				{/if}
		{/if}

			{if eq($ip['ip_address'], '85.221.60.70')}
				{if eq($httpAddress['firstchunkofhttpaddress'], 'www.flesberg.kommune.no')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="ansatt_flesberg">        
				<input type="hidden" name="Password" class="textField2" value="ansattf1">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
				{/if}
		{/if}

		{if eq($ip['ip_address'], '81.0.188.42')}
				{if eq($httpAddress['firstchunkofhttpaddress'], 'intranett.ovre-eiker.kommune.no')}
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


		{if eq($ip['ip_address'], '81.0.145.236')}
				{if eq($httpAddress['firstchunkofhttpaddress'], 'intranett.ovre-eiker.kommune.no')}
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
		{/if}


		{if eq($ip['ip_address'], '194.143.84.109')}
			{if eq($httpAddress['firstchunkofhttpaddress'], '32.54.17.3')}
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
		{/if}

		{if eq($ip['ip_address'], '194.143.84.109')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'amstel.id.local')}
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
		{/if}

		{if eq($ip['ip_address'], '192.168.20.3')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'amstel.id.local')}
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
		{/if}



		{if eq($ip['ip_address'], '62.73.233.98')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.notodden.kommune.no')}
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
		{/if}

		{if eq($ip['ip_address'], '62.73.233.99')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.notodden.kommune.no')}
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
		{/if}



		{if eq($ip['ip_address'], '62.73.233.100')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.notodden.kommune.no')}
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
		{/if}
	
		
		{if eq($ip['ip_address'], '62.73.233.101')}
			{if eq($httpAddress['firstchunkofhttpaddress'], 'www.notodden.kommune.no')}
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
		{/if}


{attribute_view_gui attribute=$node.object.data_map.body}
<!-- 
		<body onLoad="document.kurslista.submit();">	
				<form method="post" action={"fkp/exthttp?url=/listCourses.html"|ezurl}  name='kurslista'  >	 
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
		</body>
-->