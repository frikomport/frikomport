<?php

include_once( 'lib/ezdb/classes/ezdb.php' );
include_once( 'lib/ezutils/classes/ezdebug.php' );
include_once( 'kernel/classes/datatypes/ezuser/ezuser.php');
include_once( 'kernel/classes/ezcontentobject.php');
include_once( 'kernel/classes/ezcontentobjectattribute.php');
include_once( 'kernel/classes/ezcontentclass.php');
include_once( 'kernel/classes/ezrole.php');
include_once( "lib/ezfile/classes/ezlog.php" );

$server->wsdl->addComplexType(
    'FkpRole',
    'complexType',
    'struct',
    'all',
    '',
    array(
        'name' => array('name'=>'role','type'=>'xsd:string'),
    )
);

$server->wsdl->addComplexType(
    'FkpRoles',
    'complexType',
    'array',
    '',
    'SOAP-ENC:Array',
    array(),
    array(
        array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:FkpRole[]')
    ),
    'tns:FkpRole'
);

$server->wsdl->addComplexType(
    'FkpUser',
    'complexType',
    'struct',
    'all',
    '',
    array(
        'id' => array('name'=>'id','type'=>'xsd:string'),
        'login' => array('name'=>'login','type'=>'xsd:string'),
        'email' => array('name'=>'email','type'=>'xsd:string'),
        'name' => array('name'=>'name','type'=>'xsd:string'),
        'first_name' => array('name'=>'first_name','type'=>'xsd:string'),
        'last_name' => array('name'=>'last_name','type'=>'xsd:string'),
        'organization' => array('name'=>'organization','type'=>'xsd:string'),
    )
);

$server->wsdl->addComplexType(
    'FkpUsers',
    'complexType',
    'array',
    '',
    'SOAP-ENC:Array',
    array(),
    array(
        array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:FkpUser[]'),
    ),
	'tns:FkpUser'
    // Her skulle det vore arraytype 'tns:frkUser' men då fungerer det ikkje. Problemet er at xml ikkje er komplett
);

$server->register(  'getUser', 
                    array( 'key' => 'xsd:string' ),
                    array( 'user' => 'tns:FkpUser'),
                    'urn:fkpuserwsdl', 
                    'urn:fkpuserwsdl#getUser',
                    'rpc',
                    'literal',
                    'Fetches user based on ezsessid'
                );

$server->register(  'getUsers', 
                    array( 'roles' => 'xsd:string'),
                    array( 'users' => 'tns:FkpUsers'),
                    'urn:fkpuserwsdl', 
                    'urn:fkpuserwsdl#getUsers',
                    'rpc',
                    'literal',
                    'Fetches users based on roles'
                );

$server->register(  'getRoles', 
                    array( 'userid' => 'xsd:string' ),
                    array( 'roles' => 'tns:FkpRoles'),
                    'urn:fkpuserwsdl', 
                    'urn:fkpuserwsdl#getRoles',
                    'rpc',
                    'literal',
                    'Fetches roles for a user'
                );

$server->register(  'getAllRoles', 
                    array(),
                    array( 'roles' => 'tns:FkpRoles'),
                    'urn:fkpuserwsdl', 
                    'urn:fkpuserwsdl#getAllRoles',
                    'rpc',
                    'literal',
                    'Fetches all roles'
                );

/*
 Henter en bruker basert på ezsession
 */
function getUser( $key )
{
    $ezdb =& eZDB::instance();
    $ezdebug =& eZDebug::instance();

    $sessionRes = $ezdb->arrayQuery( "SELECT user_id FROM ezsession WHERE session_key='$key'" );
    $userid = $sessionRes[0]['user_id'];
    
    return getUserByID($userid);
}

function getUserByID( $userid ){
    $ezUser = ezUser::fetch($userid);
    $fkpUser = array();
    if($ezUser){
        $userObject = $ezUser->attribute('contentobject');
        $userAttributes = $userObject->fetchDataMap($userObject->version());
        
        $classAttributes = eZContentClass::fetchAttributes($userObject->attribute('contentclass_id'));
      
        $id = $ezUser->attribute('contentobject_id');
        $login = $ezUser->attribute('login');
        $email = $ezUser->attribute('email');
        $first_name = $userAttributes['first_name']->attribute('data_text');
        $last_name = $userAttributes['last_name']->attribute('data_text');
        if($userAttributes['kommune']){$organization = $userAttributes['kommune']->attribute('data_text');}
        if($userAttributes['organization']){$organization = $userAttributes['organization']->attribute('data_text');}
        $name = $userObject->attribute('name');
    
        // Må hente ut meir enn dette
        $fkpUser = array( 'id' => $id,
                         'login' => $login,
                         'email' => $email,
                         'name' => $name,
                         'first_name' => $first_name,
                         'last_name' => $last_name,
                         'organization' => $organization,
                          );        
    } else {
        $fkpUser = ezUser::fetchBuiltin(10);
    }
    return $fkpUser;
}

/*
 Henter alle roller for en gitt bruker
*/
function getRoles( $userid ){
    $roles = eZRole::fetchByUser(array($userid), true);
    
    $rolesArray = array();
    $i = 0;

    foreach( $roles as $role )
    {
		$name = $role->attribute('name');
		$rolesArray[] = array( 'name' => $name);
		$i++;
    }

    return $rolesArray;
}

/*
 Henter alle roller i systemet
*/
function getAllRoles(){
    $roles = eZRole::fetchList();
    
    $rolesArray = array();
    $i = 0;

    foreach( $roles as $role )
    {
		$name = $role->attribute('name');
		$rolesArray[] = array( 'name' => $name);
		$i++;
    }

    return $rolesArray;
}

/*
 Henter alle brukere med gitte roller
 Parameter er kommaseparert liste med roller
*/
function getUsers( $nameArray ){
    $names = explode(',',$nameArray);
    
    $userArray = array();
    $i = 0;

    $users = ezUser::fetchContentList();
    foreach($users as $user){
        $userid = $user['id'];
        $ezuser = eZUser::fetch($userid);
        
        $roles = $ezuser->roles();
        foreach($roles as $role){
            foreach($names as $name){
                if($role->attribute('name') == $name){
                    $userArray[] = getUserByID($userid);
                    $i++;
                    break 2;
                }                    
            }
        }
    }
    return $userArray;
}

?>
