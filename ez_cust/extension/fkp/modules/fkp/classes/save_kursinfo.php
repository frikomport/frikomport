<?php

include_once( 'lib/ezxml/classes/ezxml.php' );
include_once( 'lib/ezutils/classes/ezhttptool.php' );

class Save_kursinfo
{

   //static
   function getTagContent($fetchedHTML, $tag) {
       //Fetches body content only.
       $tmpRes =& substr( stristr( $fetchedHTML, '<'.$tag ), 5);
       $tmpRes =& substr( stristr( $tmpRes, '>' ), 1);

       // To be used when PHP 5.0 gets available with eZp:
       //       $tmpRes = substr($tmpRes, 0, strripos ( $tmpRes, '</'.$tag' ));
       //Since strripos is not available in PHP 4.4.X...
       $tmpRes =& substr( stristr( strrev( $tmpRes ), strrev($tag).'/<'), 6 );

       $tmpRes =& substr( stristr( $tmpRes, '>'), 1 );
       $tmpRes =& strrev( $tmpRes );  // Since strsipos is unavilable.
       return $tmpRes;
   }


   // static
   function save_kursinfo()
   {
       $xmlini =& eZINI::instance( 'fkp.ini' );
       $extURL = $xmlini->variable( 'ExternalHTTPFetch', 'URL' );


 
       $result = array( );
       $result['saved'] = 'Saved.(?)...';
/*
_SERVER["REQUEST_METHOD"]
_SERVER["REQUEST_URI"]
*/
       return array( 'result' => $result );
   }

}


include_once( 'common.php' );

?>