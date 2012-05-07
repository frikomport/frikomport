<?php /* #?ini charset="iso-8859-1"?
# eZ publish configuration file.
#
# NOTE: It is not recommended to edit this files directly, instead
#       a file in siteaccess should be created for setting the
#       values that is required for your site. Create
#       a file called settings/siteaccess/mysite/menu.ini.append.php.

[MenuSettings]
AvailableMenuArray[]
AvailableMenuArray[]=TopOnly
AvailableMenuArray[]=LeftOnly
AvailableMenuArray[]=DoubleTop
AvailableMenuArray[]=LeftTop

[SelectedMenu]
CurrentMenu=TopOnly
TopMenu=flat_top
LeftMenu=

[TopOnly]
TitleText=Only top menu
MenuThumbnail=menu/top_only.jpg
TopMenu=flat_top
LeftMenu=

[LeftOnly]
TitleText=Left menu
MenuThumbnail=menu/left_only.jpg
TopMenu=
LeftMenu=flat_left

[DoubleTop]
TitleText=Double top menu
MenuThumbnail=menu/double_top.jpg
TopMenu=double_top
LeftMenu=

[LeftTop]
TitleText=Left and top
MenuThumbnail=menu/left_top.jpg
TopMenu=flat_top
LeftMenu=sub_left


[MenuContentSettings]
# This list contains the identifiers of the classse
# that are allowed to be shown in top menues
TopIdentifierList[]
TopIdentifierList[]=folder
TopIdentifierList[]=feedback_form
TopIdentifierList[]=gallery
TopIdentifierList[]=forum

# This list contains the identifiers of the classse
# that are allowed to be shown in left menues
LeftIdentifierList[]
LeftIdentifierList[]=folder
LeftIdentifierList[]=feedback_form
LeftIdentifierList[]=gallery
LeftIdentifierList[]=forum

# A list of the current navigation parts and their names
# Each entry consists of the identifier as key and the name
# as value.
# Note: If you wish to have the name translatable you will
#       need to create a dummy PHP file with the following in it
# ezi18n( 'kernel/navigationpart', 'Custom navigation', 'Navigation part' );
# This will cause the ezlupdate to include it in the .ts file.
[NavigationPart]
Part[ezcontentnavigationpart]=Content structure
Part[ezmedianavigationpart]=Media library
Part[ezusernavigationpart]=User accounts
Part[ezshopnavigationpart]=Webshop
Part[ezvisualnavigationpart]=Design
Part[ezsetupnavigationpart]=Setup
Part[ezmynavigationpart]=My account

# Example of a custom navigation part
# See TopMenuAdminMenu for how you can combine this with a
# new top menu
# Part[ezcustomnavigationpart]=Custom navigation


[TopAdminMenu]
# This list contains menuitems of the top menu in admin interface
Tabs[]
Tabs[]=content
Tabs[]=media
Tabs[]=users
Tabs[]=shop
Tabs[]=design
Tabs[]=setup
Tabs[]=my_account

# Example of adding a custom topmenu entry
# See the commented Topmenu_custom entry at the bottom
# Tabs[]=custom

# Here are the settings which controls behavour of each
# menuitem in top admin menu.
# URL['ui_context'] - which view item points to in different contextes.
# NavigationPartIdentifier  - Navigation part identifier of the menu item.
# Name - name which is shown on the menu item, if it is not set the default value 
#        is used. Note!!! When you use the not default value the text translation
#        of a menu item is done. 
# Tooltip - tooltip which is shown for the menu item, if it is not set the default value 
#           is used. Note!!! When you use the not default value the text translation
#           of a menu item is done. 
# Enabled[ui_context] - sets if menuitem is clickable in current ui_context.
# Shown[ui_context]  - sets if menuitem is clickable in current ui_context
[Topmenu_content]
URL[]
URL[default]=content/view/full/2
URL[browse]=content/browse/2
NavigationPartIdentifier=ezcontentnavigationpart
#Name=Content structure
#Tooltip=Manage the main content structure of the site.
Enabled[]
Enabled[default]=true
Enabled[browse]=true
Enabled[edit]=false
Shown[]
Shown[default]=true
Shown[navigation]=true
Shown[browse]=true

[Topmenu_media]
NavigationPartIdentifier=ezmedianavigationpart
#Name=Media library
#Tooltip=Manage images, files, documents, etc.
URL[]
URL[default]=content/view/full/43
URL[browse]=content/browse/43
Enabled[]
Enabled[default]=true
Enabled[browse]=true
Enabled[edit]=false
Shown[]
Shown[default]=true
Shown[navigation]=true
Shown[browse]=true

[Topmenu_users]
NavigationPartIdentifier=ezusernavigationpart
#Name=User accounts
#Tooltip=Manage users, user groups and permission settings.
URL[]
URL[default]=content/view/full/5
URL[browse]=content/browse/5
Enabled[]
Enabled[default]=true
Enabled[browse]=true
Enabled[edit]=false
Shown[]
Shown[default]=true
Shown[navigation]=true
Shown[browse]=true

[Topmenu_shop]
NavigationPartIdentifier=ezshopnavigationpart
#Name=Webshop
#Tooltip=Manage customers, orders, discounts and VAT types; view sales statistics.
URL[]
URL[default]=shop/orderlist
Enabled[]
Enabled[default]=true
Enabled[browse]=false
Enabled[edit]=false
Shown[]
Shown[navigation]=true
Shown[default]=false
Shown[browse]=true

[Topmenu_design]
NavigationPartIdentifier=ezvisualnavigationpart
#Name=Design
#Tooltip=Manage templates, menus, toolbars and other things related to appearence.
URL[]
URL[default]=visual/menuconfig
Enabled[]
Enabled[default]=true
Enabled[browse]=false
Enabled[edit]=false
Shown[]
Shown[navigation]=true
Shown[default]=true
Shown[browse]=true

[Topmenu_setup]
NavigationPartIdentifier=ezsetupnavigationpart
#Name=Setup
#Tooltip=Configure settings and manage advanced functionality.
URL[]
URL[default]=setup/cache
Enabled[]
Enabled[default]=true
Enabled[browse]=false
Enabled[edit]=false
Shown[]
Shown[default]=true
Shown[navigation]=true
Shown[browse]=true

[Topmenu_my_account]
NavigationPartIdentifier=ezmynavigationpart
#Name=My account
#Tooltip=Manage items and settings that belong to your account.
URL[]
URL[default]=content/draft
Enabled[]
Enabled[default]=true
Enabled[browse]=false
Enabled[edit]=false
Shown[]
Shown[default]=true
Shown[navigation]=true
Shown[browse]=true

# Definition of the custom topmenu
# Note: This is just an example
#
# [Topmenu_custom]
# # Uses a custom navigation part (See list NavigationPart group above)
# NavigationPartIdentifier=ezcustomnavigationpart
# Name=Custom
# Tooltip=A custom tab for eZ publish
# URL[]
# URL[default]=custom/list
# Enabled[]
# Enabled[default]=true
# Enabled[browse]=false
# Enabled[edit]=false
# Shown[]
# Shown[default]=true
# Shown[edit]=true
# Shown[navigation]=true
# # We don't show it in browse mode
# Shown[browse]=false


# Controls the display of the left menu for the administration interface.
# Width setting can be used to setup the widths (the measure is em) for the
# different menu sizes (small/medium/large).
[LeftMenuSettings]
MenuWidth[]
MenuWidth[small]=13
MenuWidth[medium]=19
MenuWidth[large]=25

*/ ?>
