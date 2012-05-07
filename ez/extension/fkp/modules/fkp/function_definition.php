<?php

$FunctionList = array();

 

$FunctionList['url_alias'] = array(
'name' => 'url_alias',
'call_method' => array( 
	'include_file' => 'extension/fkp/modules/fkp/classes/fetchURL.php',
	'class' => 'FetchURL',
	'method' => 'getURL_alias' ),
'parameter_type' => 'standard',
'parameters' => array() );

$FunctionList['frame_src'] = array(
'name' => 'frame_src',
'call_method' => array( 
	'include_file' => 'extension/fkp/modules/fkp/classes/fetchURL.php',
	'class' => 'FetchURL',
	'method' => 'getFrameSrc' ),
'parameter_type' => 'standard',
'parameters' => array() );

$FunctionList['get_ip_address'] = array(
'name' => 'get_ip_address',
'call_method' => array( 
	'include_file' => 'extension/fkp/modules/fkp/classes/fetchURL.php',
	'class' => 'FetchURL',
	'method' => 'getIpAddress' ),
'parameter_type' => 'standard',
'parameters' => array() );

$FunctionList['get_http_referer'] = array(
'name' => 'get_http_referer',
'call_method' => array( 
	'include_file' => 'extension/fkp/modules/fkp/classes/fetchURL.php',
	'class' => 'FetchURL',
	'method' => 'getHttpReferer' ),
'parameter_type' => 'standard',
'parameters' => array() );

$FunctionList['get_form_data'] = array(
'name' => 'get_form_data',
'call_method' => array( 
	'include_file' => 'extension/fkp/modules/fkp/classes/fetchURL.php',
	'class' => 'FetchURL',
	'method' => 'getFormData' ),
'parameter_type' => 'standard',
'parameters' => array() );


?>