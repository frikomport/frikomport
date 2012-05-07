#!/usr/bin/env php
<?php

include_once( 'lib/ezutils/classes/ezcli.php' );
include_once( 'kernel/classes/ezscript.php' );

$cli =& eZCLI::instance();
$script =& eZScript::instance( array( 'description' => 'HelloWorld SOAP client',
                                      'use-session' => false,
                                      'use-modules' => false,
                                      'use-extensions' => true ) );

$script->startup( );

$options = $script->getOptions( '[show-request][show-response]', '[WSDL_URI][NAME]', array( 'show-request' => 'show the SOAP HTTP request', 'show-response' => 'show the SOAP HTTP response' ) );

$script->initialize( );

// check argument count
if ( count( $options['arguments'] ) < 2 )
{
    $script->shutdown( 1, 'wrong argument count' );
}

$wsdlUri =& $options['arguments'][0];
$name =& $options['arguments'][1];

ext_class( 'nusoap', 'nusoap' );

$client = new soapclient( $wsdlUri, true );

$err = $client->getError( );
    
if ( $err )
{
    $script->shutdown( 1, $err );
}

$result = $client->call( 'hello', array( 'name' => $name ) );

if ( $options['show-request'] )
{
    $cli->output( 'SOAP request:' );
    $cli->output( $client->request );
}

if ( $options['show-response'] )
{
    $cli->output( 'SOAP response:' );
    $cli->output( $client->request );
}
        
if ( $client->fault )
{
    $script->shutdown( 1, $result );
}


$err = $client->getError( );

if ( $err )
{
    $script->shutdown( 1, $err );
}
    
$cli->output( $result );

$script->shutdown( 0 );

?>