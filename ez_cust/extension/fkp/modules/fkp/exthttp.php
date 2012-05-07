<?php



include_once( 'kernel/common/template.php' );



$tpl =& templateInit();



$Result = array();

$Result['content'] =& $tpl->fetch( 'design:exthttp/exthttp.tpl' );

$Result['path'] = array( array( 'url' => false,

                               'text' => 'Kursmodulen' ) );

$tmpParam = array();
$tmpParam['url_alias'] = 'fkp/exthttp';
$tmpParam['node_id'] = '';
$tmpParam['object_id'] = '';

$Result['content_info'] = $tmpParam;
?>
