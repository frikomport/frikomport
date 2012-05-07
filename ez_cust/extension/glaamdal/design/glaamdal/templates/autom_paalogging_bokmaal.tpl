{set-block scope=root variable=cache_ttl}0{/set-block} 

	{def $httpAddress=fetch('fkp','get_http_referer')}
	{def $ip=fetch('fkp','get_ip_address')}
	{default current_user=fetch('user','current_user')}

                {if eq($ip['ip_address'], '195.18.140.163')}
                       {if $current_user.is_logged_in|not}
                               <body onLoad="document.postIt.submit();">
                               <form method="post" action={"/user/login/"|ezurl}  name='postIt'  >
                               <input type="hidden" name="Login" class="textField" value="kongsvinger">
                               <input type="hidden" name="Password" class="textField2" value="kongsvinger">
                               <noscript>
	                                <input type="submit" value="continue to login">
                               </noscript>
                                </form>
                                </body>
                       {/if}
                {/if}


		{if eq($ip['ip_address'], '195.18.140.174')}
				
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="kongsvinger">        
				<input type="hidden" name="Password" class="textField2" value="kongsvinger">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}



		{if eq($ip['ip_address'], '217.77.40.161')}
				
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="asnes">        
				<input type="hidden" name="Password" class="textField2" value="asnes">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
				{/if}
		{if eq($ip['ip_address'], '217.77.40.234')}
				
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="asnes">        
				<input type="hidden" name="Password" class="textField2" value="asnes">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}

		{if or(eq($ip['ip_address'], '78.24.151.250'),eq($ip['ip_address'], '78.24.151.251'))}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="grue">        
				<input type="hidden" name="Password" class="textField2" value="grue">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}

		{if eq($ip['ip_address'], '217.77.40.202')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="grue">        
				<input type="hidden" name="Password" class="textField2" value="grue">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}
{if eq($ip['ip_address'], '195.18.128.194')}
{if $current_user.is_logged_in|not}
<body onLoad="document.postIt.submit();">
<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >
<input type="hidden" name="Login" class="textField" value="sorodal">
<input type="hidden" name="Password" class="textField2" value="sorodal">
<noscript>
<input type="submit" value="continue to login">
</noscript>
</form>
</body>
{/if}
{/if}

		{if eq($ip['ip_address'], '193.215.249.110')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="nordodal">        
				<input type="hidden" name="Password" class="textField2" value="nordodal">	
 				<noscript>
				<input type="submit" value="continue to login"> 
				</noscript>
				</form>
				</body>
				{/if}
		{/if}

			{if eq($ip['ip_address'], '194.248.106.113')}
				{if $current_user.is_logged_in|not}
				<body onLoad="document.postIt.submit();">	
				<form method="post" action={"/user/login/"|ezurl}  name='postIt'  >	
          			<input type="hidden" name="Login" class="textField" value="eidskog">        
				<input type="hidden" name="Password" class="textField2" value="eidskog">	
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


