<?php /* #?ini charset="iso-8859-1"?

[Session]
SessionNameHandler=custom
SessionNamePerSiteAccess=disabled
SessionTimeout=7200 

[UserSettings]
LogoutRedirect=/
LoginHandler[]=standard
#LoginHandler[]=LDAP

[SiteSettings]
DefaultAccess=fkp_nob
SiteList[]=fkp_nob
SiteList[]=fkp_nno
SiteName=FriKomPort
MetaDataArray[copyright]=FriKomPort
MetaDataArray[description]=Fri kompetanseportal
MetaDataArray[keywords]=fri, programvare, kurs, kompetanse, portal
SiteURL=/index.php

[SiteAccessSettings]
CheckValidity=false
AvailableSiteAccessList[]=fkp_nob
AvailableSiteAccessList[]=fkp_nno
AvailableSiteAccessList[]=fkp_oppl
AvailableSiteAccessList[]=fkp_admin
RelatedSiteAccessList[]=fkp_nob
RelatedSiteAccessList[]=fkp_nno
RelatedSiteAccessList[]=fkp_oppl
RelatedSiteAccessList[]=fkp_admin
MatchOrder=uri
HostMatchMapItems[]

[MailSettings]
Transport=SMTP
AdminEmail=frikomport@gmail.no
EmailSender=

[RegionalSettings]
ContentObjectLocale=eng-GB

[ContentSettings]
TranslationList=nno-NO;nor-NO;eng-GB

[ExtensionSettings]
ActiveExtensions[]=ezdhtml
ActiveExtensions[]=fkp
ActiveExtensions[]=fkp_oppl
ActiveExtensions[]=nusoap


[DatabaseSettings]
DatabaseImplementation=ezmysql
Server=$SERVER$
User=$USER$
Password=$PASSWORD$
Database=$DATABASE$
Charset=
Socket=disabled
*/ ?>