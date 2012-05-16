#?ini charset="iso-8859-1"?
# eZ publish configuration file for connection and authentication of users via LDAP
#

[LDAPSettings]
# Set LDAP version number
LDAPVersion=3
# Set to true if use LDAP server
LDAPEnabled=true
# LDAP host
#LDAPServer=ldaps://hostname
LDAPServer=$LDAPSERVER$
# Port nr for LDAP, default is 389, LDAPS is 636
LDAPPort=$LDAPPORT$
# Specifies the base DN for the directory.
LDAPBaseDn=$LDAPBASEDN$
# If the server does not allow anonymous bind, specify the user name for the bind here. AD requires the form username@domain
LDAPBindUser=$LDAPUSER$
# If the server does not allow anonymous bind, specify the password for the bind here.
LDAPBindPassword=$LDAPPASSWORD$
# Could be sub, one, base.
LDAPSearchScope=sub
# Use the equla sign to replace "=" when specify LDAPBaseDn or LDAPSearchFilters
LDAPEqualSign=--
# Add extra search requirment. Uncomment it if you don't need it.
# Example LDAPSearchFilters[]=objectClass--inetOrgPerson
#LDAPSearchFilters[]=objectClass--organizationalPerson
#LDAPSearchFilters[]=memberOf--*
#LDAPSearchFilters[]=!(objectClass--computer)

# LDAP attribute for login. Normally, uid. AD uses samaccountname
LDAPLoginAttribute=samaccountname
# Could be id or name
LDAPUserGroupType=name
# Default place to store LDAP users. Could be content object id or group name for LDAP user group,
# depends on LDAPUserGroupType.
LDAPUserGroup[]=Ansatte

# Group mapping settings:
# Possible values: UseGroupAttribute (old style group assignig using LDAPUserGroupAttribute setting),
# SimpleMapping (using LDAPUserGroupMap array for name-to-name group mapping) or GetGroupsTree
LDAPGroupMappingType=SimpleMapping
# Base LDAP dn which should be used to fetch user group objects from LDAP
LDAPGroupBaseDN=$LDAPGROUPBASEDN$
# LDAP user group class
LDAPGroupClass=group
# Attribute which should be used to obtain name of an LDAP group
# Required then 'LDAPGroupMappingType' is set to 'GetGroupsTree' or 'SimpleMapping'
LDAPGroupNameAttribute=name
# Attribute of LDAP user which should be used to obtain groups which user(group) belongs to.
# Required then 'LDAPGroupMappingType' is set to 'GetGroupsTree' or 'SimpleMapping'
LDAPGroupMemberAttribute=member
# Attribute which contain description of LDAP group, optional
LDAPGroupDescriptionAttribute=description
# Group names map (from LDAP to ezpublish user-groups),
# used then 'LDAPGroupMappingType' is set to 'SimpleMapping'
#LDAPUserGroupMap[Domain Admins]=Administratorer
#LDAPUserGroupMap[Kurs-Admin]=Opplæringsansvarlige
#LDAPUserGroupMap[Domain Users]=Ansatte

# LDAP attribute type for user group. Could be name or id
LDAPUserGroupAttributeType=name
# LDAP attribute for user group. For example, employeetype. If specified, LDAP users
# will be saved under the same group as in LDAP server.
LDAPUserGroupAttribute=member
# LDAP attribute for First name. Normally, givenname
LDAPFirstNameAttribute=givenname
# LDAP attribute for Last name. Normally, sn
LDAPLastNameAttribute=sn
# LDAP attribute for email. Normally, mail
LDAPEmailAttribute=mail
# LDAP encoding is utf-8 or not
Utf8Encoding=true
# if 'enabled' you can move LDAP users to a different group and they will not
# be automatically moved back (to the group they are configured to be placed in)
# when the user logs in again.
KeepGroupAssignment=enabled