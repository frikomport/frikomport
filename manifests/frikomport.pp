group { 'puppet': ensure => 'present' }

$soak_db = "fdb"
$soak_user = "friuser"
$soak_password = "friuser"
$ez_db = "fezdb"
$ez_user = "fezuser"
$ez_password = "fezuser"


class tools {
  exec { "update-package-list":
    command => "/usr/bin/sudo /usr/bin/apt-get update",
  }
  package { "vim":
    ensure => present,
  }
  package { "nmap":
    ensure => present,
  }
}

class mysql {
  require tools

  package { "mysql-server-5.1":
    ensure => present,
    require => Exec["update-package-list"],
  }->
  
  service { "mysql":
    ensure => running, 
    require => Package["mysql-server-5.1"]
  }->

  exec { "delete-old-dbs":
    command => "/usr/bin/mysql -uroot -e \"drop database if exists ${soak_db}; drop database if exists ${ez_db};\"",
    require => Service["mysql"]
  }->
  
  exec { "create-dbs":
    command => "/usr/bin/mysql -uroot -e \"create database ${soak_db}; create database ${ez_db};\"",
    require => Service["mysql"]
  }->  
  
  exec { "create-dbusers":
    command => "/usr/bin/mysql -uroot -e \"create user ${soak_user}@'%' identified by '${soak_password}'; create user ${ez_user}@'%' identified by '${ez_password}';flush privileges;\"",
    require => Service["mysql"]
  }->  
  
  exec { "grant-to-users":
    command => "/usr/bin/mysql -uroot -e \"grant all on ${soak_db}.* to ${soak_user}@'%'; grant all on ${ez_db}.* to ${ez_user}@'%';flush privileges;\"",
    require => Service["mysql"]
  }->  
  
  exec { "import-ezdb":
    command => "/usr/bin/mysql -uroot ${ez_db} < /vagrant/files/ezdbcontent.sql",
    path    => "/usr/local/bin/:/bin/",
	require => Service["mysql"]
  }
}

class jdk {
  require tools
  package { "openjdk-6-jre-headless":
    ensure => present,
  }
}

class tomcat {
  require jdk
  require mysql
  package { "tomcat6":
    ensure => present,
  }
}


class tomcat_config {
  require tomcat
  
  service { "tomcat6":
    ensure => running, 
    require => Package["tomcat6"]
  }
  
  file {
	"soak.xml":
	mode => 644,
	owner => tomcat6,
	group => tomcat6,
	path => "/etc/tomcat6/Catalina/localhost/soak.xml",
	source => '/vagrant/files/soak.xml',
  } 
  file {
	"server.xml":
	mode => 644,
	owner => tomcat6,
	group => tomcat6,
	path => "/etc/tomcat6/server.xml",
	source => '/vagrant/files/server.xml',
  }
  file {
	"soak.war":
	mode => 644,
	owner => root,
	group => root,
	path => "/var/lib/tomcat6/webapps/soak.war",
	source => '/vagrant/soak/build/soak.war',
  } 
  exec { 
    "tomcat-log-link":
    command => "/bin/ln -s /var/log/tomcat6/ /home/vagrant/tomcat_logs"
  }
  exec { 
    "tomcat-enable-debug":
    command => "/bin/sed -i '/-Xdebug -Xrunjdwp:transport=dt_socket/ s/# *//' /etc/default/tomcat6",
	notify => Service['tomcat6']
  }
}

class php {
  require imagemagick
  package { "php5":
    ensure => present,
  }  
  package { "php5-ldap":
    ensure => present,
  }  
  package { "php5-mysql":
    ensure => present,
  }
  package { "php5-imagick":
    ensure => present,
  }
  package { "php5-cli":
    ensure => present,
  }  
}

class apache {
  require php
  require tomcat
  
  package { "apache2.2-common":
    ensure => present,
  }-> 
  package { "apache2.2-bin":
    ensure => present,
  }-> 
  package { "apache2-mpm-itk":
    ensure => present,
  }-> 
  package { "apache2-utils":
    ensure => present,
  }-> 
  package { "libapache2-mod-php5":
    ensure => present,
  }
}

class apache_config {
  require apache
  
  service {
	apache2:
	ensure => true,
	enable => true,
	require => Package["apache2.2-bin"]
  } 

  file {
	"httpd.conf":
	mode => 644,
	owner => root,
	group => root,
	path => "/etc/apache2/httpd.conf",
	source => '/vagrant/files/httpd.conf',
  }->
  file {
	"default":
	mode => 644,
	owner => root,
	group => root,
	path => "/etc/apache2/sites-available/default",
	source => '/vagrant/files/default',
	notify => Service['apache2'],
  }  
  file {
	"ezcache_delete.sh":
	mode => 755,
	owner => root,
	group => root,
	path => "/home/vagrant/ezcache_delete.sh",
	source => '/vagrant/files/ezcache_delete.sh',
  }
  exec { 
    "apache-log-link":
    command => "/bin/ln -s /var/log/apache2/ /home/vagrant/apache_logs"
  }
  exec { 
    "merge-ez-project-files":
    command => "/bin/mkdir /vagrant/eztest; /bin/cp -r /vagrant/ez/* /vagrant/eztest; cp -r /vagrant/ez_cust/* /vagrant/eztest;"
  }
}

class imagemagick {
  require tools
  package { "imagemagick":
    ensure => present,
  }  
}

include tools
include jdk
include tomcat
include tomcat_config
include mysql
include php
include imagemagick
include apache
include apache_config
