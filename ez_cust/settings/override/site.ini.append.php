<?php /* #?ini charset="iso-8859-1"?

[Session]
SessionNameHandler=custom
SessionNamePerSiteAccess=disabled
SessionTimeout=7200 

[UserSettings]
LogoutRedirect=/user/login
LoginHandler[]=standard
LoginHandler[]=LDAP

[SiteSettings]
DefaultAccess=fkp_nob
SiteList[]=fkp_nob
SiteList[]=fkp_nno
SiteName=FriKomPort
MetaDataArray[copyright]=FriKomPort
MetaDataArray[description]=Fri kompetanseportal
MetaDataArray[keywords]=fri, programvare, kurs, kompetanse, portal
SiteURL=$SERVER$

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

[ExtensionSettings]
ActiveExtensions[]=ezdhtml
ActiveExtensions[]=fkp
ActiveExtensions[]=fkp_oppl
ActiveExtensions[]=ezoe
ActiveExtensions[]=ezjscore

[FileSettings]
VarDir=var/intranet

[ContentSettings]
ViewCaching=enabled

[MailSettings]
Transport=SMTP
AdminEmail=ikke-svar@frikomport.no
EmailSender=ikke-svar@frikomport.no

[RegionalSettings]
ContentObjectLocale=eng-GB
TextTranslation=enabled
TranslationCache=enabled

[TemplateSettings]
Debug=disabled
TemplateCache=enabled
TemplateCompile=enabled
ShowXHTMLCode=disabled
ShowUsedTemplates=disabled

[DebugSettings]
DebugOutput=disabled
DebugRedirection=disabled

[ContentSettings]
TranslationList=nno-NO;nor-NO;eng-GB

[DatabaseSettings]
DatabaseImplementation=ezmysql
Server=$SERVER$
User=$USER$
Password=$PASSWORD$
Database=$DATABASE$
Charset=
Socket=disabled
*/ ?>