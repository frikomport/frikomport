<?php

include_once( 'lib/ezutils/classes/ezhttptool.php' );
include_once( "lib/ezfile/classes/ezlog.php" );

class FetchURL
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
   function fetchURL()
   {
       $xmlini = eZINI::instance( 'fkp.ini' );
       $extURL = $xmlini->variable( 'ExternalHTTPFetch', 'URL' );
 
       $result = array( );
//        $fetchedHTML = file_get_contents($extURL);

//       $tmpRes = FetchURL::getTagContent($fetchedHTML, 'body');
//       $tmpScript = FetchURL::getTagContent($fetchedHTML, 'head');

       //Substitutes URL's.

		 $tmpRes='';  //Temp "solution"

//       $tmpRes =& changeAttributeValue($tmpRes, 'a', 'href', '\/soak\/', '/ez/index.php/fkp_nob/fkp/');
//       $tmpRes =& changeAttributeValue($tmpRes, 'link', 'href', '\/soak\/', '/ez/index.php/fkp_nob/fkp/');
//       $tmpRes =& changeAttributeValue($tmpRes, 'img', 'src', '\/soak\/', '/ez/extension/fkp/design/standard/');
//       $tmpRes =& changeAttributeValue($tmpRes, 'script', 'location.href', '\/soak\/', '/ez/index.php/fkp_nob/fkp/');
//       $tmpRes =& changeAttributeValue($tmpRes, 'script', 'src', '\/soak\/', '/ez/extension/fkp/design/standard/');
//       $tmpRes =& changeAttributeValue($tmpRes, 'form', 'action', '\/soak\/', '/ez/index.php/fkp_nob/fkp/');

       $result['exthttp_result'] =& $tmpRes;
/*
_SERVER["REQUEST_METHOD"]
_SERVER["REQUEST_URI"]

*/
       return array( 'result' => $result );
   }

	function getURL_alias()
	{
       $xmlini =& eZINI::instance( 'fkp.ini' );
       $result = array( );

       $result['url_alias'] =& $xmlini->variable( 'URLs', 'extentionURLFragment' );

       $result['course_listURL'] =& $xmlini->variable( 'URLs', 'Course_list_URLParam' );
       $result['historic_course_listURL'] =& $xmlini->variable( 'URLs', 'HistoricCourse_list_URLParam' );
       $result['Instructor_listURL'] =& $xmlini->variable( 'URLs', 'Instructor_listURLParam' );
       $result['Responsible_listURL'] =& $xmlini->variable( 'URLs', 'Responsible_listURLParam' );
       $result['Commune_listURL'] =& $xmlini->variable( 'URLs', 'Commune_listURLParam' );
       $result['ConfigurationURL'] =& $xmlini->variable( 'URLs', 'ConfigurationParam' );
       $result['Locations_listURL'] =& $xmlini->variable( 'URLs', 'Locations_listURLParam' );
       $result['Fields_of_service_listURL'] =& $xmlini->variable( 'URLs', 'Fields_of_service_listURLParam' );
       $result['attendee_listURL'] =& $xmlini->variable( 'URLs', 'attendee_listURLParam' );
       $result['user_listURL'] =& $xmlini->variable( 'URLs', 'User_list_URLParam' );
       $result['editProfile_URL'] =& $xmlini->variable( 'URLs', 'EditProfile_URLParam' );
       $result['profile_URL'] =& $xmlini->variable( 'URLs', 'Profile_URLParam' );

		return array( 'result' => $result );
	}

	function getFrameSrc()
	{
      $xmlini = eZINI::instance( 'fkp.ini' );
      $extensionURL = $xmlini->variable( 'ExternalHTTPFetch', 'URL' );
      
      if (isset($_GET['url'])) {
          $extensionURL .= $_GET['url'];
      }
      if (isset($_GET['hash'])) {
          $extensionURL .= "&hash=" . $_GET['hash'];
      }
      
		$result = array( );
		$result['url_alias'] =& $extensionURL;
		return array( 'result' => $result );
	}

	function getIpAddress()
	{
      
       
		
		
      		
		//	Use REMOTE_ADDR when FriKomPort is hosted by LinuxLabs	
		//if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
		//	$extensionURL .= $_SERVER['HTTP_X_FORWARDED_FOR'];
	  	if (isset($_SERVER['REMOTE_ADDR'])) {
	       	$extensionURL .= $_SERVER['REMOTE_ADDR'];
			$hostname = $_SERVER['REMOTE_HOST'];
		}

 
		$result = array( );
		$result['ip_address'] =& $extensionURL;
		$result['hostname'] =& $hostname;

		// eZLog::write( 'getIpAddress() ip_address=['. $result['ip_address'] .']', "fkp.log" );
		
		return array( 'result' => $result );
	}


	function getFormData()
	{
		$result = array();
		$nyinnlogget = $_POST["Nyinnlogget"];
		$result['nyinnlogget']=& $nyinnlogget;
	} //end function getFormData()

	function getHttpReferer()
	{
     	$result = array( );
       	$dividedurl = array();
		$userurl = array();	
		$extensionURL = array();
		$copyofurl = "";


		if (isset($_SERVER['HTTP_REFERER'])) 
		{
          	$extensionURL .= $_SERVER['HTTP_REFERER'];
		} //end of if (isset($_SERVER['HTTP_REFERER'])) 
		$userurl = parse_url($extensionURL);


		if ($userurl['scheme'] == 'http'){
		$fullurl = eregi_replace("http://", "", $extensionURL);
		$copyofurl= &$fullurl;
		
		}
		else if (($userurl['scheme'] == 'https'))
		{
		$fullurl = eregi_replace("https://", "", $extensionURL);
		$copyofurl= &$fullurl;
		

		}
		
		$result['copyofurl'] = &$fullurl;
		$request_part =  stristr($fullurl, "/");
		$result['request_part'] = &$request_part;
		$dividedurl = split("/", $copyofurl);
		
		if ($dividedurl[0])
		{
		$result['firstchunkofhttpaddress']= &$dividedurl[0];	
		}
		else{
		$result['firstchunkofhttpaddress']= &$copyofurl;
		}
		
		$result['http_referer'] = &$extensionURL;
		
		// eZLog::write( 'getHttpReferer() http_referer=['.$result['http_referer'].'] firstchunk...=['.$result['firstchunkofhttpaddress'].']', "fkp.log" );
		return array( 'result' => $result );
		
		} //end function



}
include_once( 'common.php' );

?>