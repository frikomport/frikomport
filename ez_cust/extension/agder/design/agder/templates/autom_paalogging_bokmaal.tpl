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
								<input type="hidden" name="Password" class="textField2" value="48Aren09Dal">
								<noscript>
									<input type="submit" value="continue to login">
								</noscript>
							</form>
							</body>
						{/if}
					{/if}

               {if eq($ip['ip_address'], '85.19.89.238')}
                   {if $current_user.is_logged_in|not}
                         <body onLoad="document.postIt.submit();">
                               <form method="post" action={"/user/login/"|ezurl}  name='postIt'  >
                                     <input type="hidden" name="Login" class="textField" value="ansatt">
                                     <input type="hidden" name="Password" class="textField2" value="48Aren09Dal">
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


