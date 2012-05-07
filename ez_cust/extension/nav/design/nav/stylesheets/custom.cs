/******************************************************
/* framework.css
/* Developed by Objectnet AS
/* 
/* By using relative measure % (or em) instead of absolute
/* measure px for font-size, it is possible to resize text size in IE.
/******************************************************/



/***********************************************/
/* HTML tag styles                             */
/***********************************************/

body {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	color: #323232;
	margin: 0;
	font-size: 0.6em;
	padding-top: 0px;
	background-color: #f0f0ef;
	text-align: center;
}
table {
	clear: both;
}


/******* hyperlink and anchor tag styles *******/

a {
	color: #1b74b2;
	text-decoration: underline;
}
a:hover {
	text-decoration: none;
}
.searchBox {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
	color: #242424;
	width: 80px;
	margin-right: 6px;
}

a img {
  border: 0;
}

/************** header tag styles **************/

h1 {
	margin: 0;
	font-size: 1.2em;
	font-weight: bold;
	color: #323232;
}
h2 {
	margin: 0;
	font-size: 10px;
	font-weight: bold;
	color: #323232;
}
p {
	margin: 0;
	padding: 0;
}


/********* form and related tag styles *********/

table {
	font-size: 100%; //1.0em; /* IE5.x fix */
}

td {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	color: #323232;
	vertical-align: top;
	font-size: 100%; //1em; //10px;
}
.searchForm{
	position: absolute;
	top: 8px;
	right: 10px;
}
.searchField {
	position: absolute;
	width: 80px;
	top: 8px;
	right: 50px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 1.1em; //11px;
}
.btnSearch {
	position: absolute;
	top: 8px;
	right: 10px;
}


/***********************************************/
/* Layout Divs and Tables                      */
/***********************************************/

#allcontent {
	position: relative; /* Activate absolute */
	text-align: left; /* If text-align = center in body */
	width: 1024px; /*/915px; /* Total width of webpages */
	margin: auto;
	
}


/***********************************************/
/* Component Divs (Boxes)                      */
/***********************************************/

.leftmenu {
	position: relative;
	width: 180px;
	border-right: 5px solid #FFF;
}
.leftmenu a {
	font-size: 100%; //1em; //10px;
	color: #363636;
	font-weight: bold;
	text-decoration: none;
	padding-left: 1px; /*/41px;*/
}
.leftmenu a:hover {
	text-decoration: underline;
}

#login-design {
	position: relative;
	width: 155px;
	height: 92px;
	/*background-image: url(../images/login_bg.gif);*/
	background-position: top;
	background-repeat: no-repeat;
	padding-top: 2px;
	padding-left: 5px;
	padding-right: 5px;
	font-weight: bold;
	color: #7a6e43;
}
#login-design label {
    float: left;
}
#login-design .label {
    width: 100%;
    float: left;
    margin-top: 2px;
    padding-top: 2px;
}
#login-design .textField {
	margin-top: 0px;
	padding-top: 2px;
	padding-bottom: 4px;
	padding-left: 6px;
	font-size: 11px;
	color: black;
	font-weight: normal;
	width: 72px;
    float: right;
	border: 1px solid #8a8a8a;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
#login-design .textField2 {
	margin-top: 0px;
	margin-bottom: 0px;
	padding-top: 2px;
	padding-bottom: 4px;
	padding-left: 6px;
	font-size: 11px;
	color: black;
	font-weight: normal;
	width: 72px;
    float: right;
	border: 1px solid #8a8a8a;	
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
#login-design .loginButton {
	float: right;
	margin-left: 4px;
	margin-top: 0px;
}
#registerLink {
    visibility: visible;
}

#register {
	position: absolute;
	top: 83px;
	left: 10px;
}
#register a {
	font-weight: normal;
	text-decoration: underline;
	color: #7a6e43;
	margin: 0;
	padding: 0;
	text-align: left;
}
#register a:hover {
	text-decoration: none;
}

.menuContainer-level-0, .menuContainer-level-1, .menuContainer-level-2, .menuContainer-level-3 , .menuContainer-level-4 {
	position: relative;
	width: 165px;
}

.menuContainer-level-0 {
	height: 18px;
	margin-top: 5px;
	background-color: #f0efed;
	font-size: 100%; //1em; //10px;
	text-align: left;
	background-image: url(../images/menu_bg.gif);
	background-position: left;
	background-repeat: no-repeat;
	padding-top: 5px;
	padding-bottom: 3px;
}
.menuContainer-level-0 a {
	font-weight: bold;
	color: #7a786e;
	text-decoration: none;
	padding-left: 20px;
}
.menuContainer-level-0 a:hover {
	text-decoration: underline;
}
.menuContainer-level-1 {
	height: 14px;
	font-size: 100%; //1em; //10px;
	text-align: left;
	padding-top: 3px;
	padding-bottom: 2px;
	margin-top: 3px;
}
.menuContainer-level-1 a {
	font-weight: normal;
	color: #7a786e;
	text-decoration: none;
	padding-left: 20px;
}
.menuContainer-level-1 a:hover {
	text-decoration: underline;
}
.menuContainer-level-2 {
	height: 14px;
	font-size: 100%; //1em; //10px;
	text-align: left;
	padding-top: 1px;
	padding-bottom: 2px;
	margin-top: 1px;
}
.menuContainer-level-2 a {
	font-weight: normal;
	color: #7a786e;
	text-decoration: none;
	padding-left: 30px;
}
.menuContainer-level-2 a:hover {
	text-decoration: underline;
}

.menuContainer-level-3 {
	height: 14px;
	font-size: 100%; //1em; //10px;
	text-align: left;
	padding-top: 1px;
	padding-bottom: 2px;
	margin-top: 1px;
}
.menuContainer-level-3 a {
	font-weight: normal;
	color: #7a786e;
	text-decoration: none;
	padding-left: 40px;
}
.menuContainer-level-3 a:hover {
	text-decoration: underline;
}

.maincontent-design {
	position: relative;
	width: 690px; /*/535px;*/
}
.maincontent-design-full {
	position: relative;
	width: 100%; //770px;
}

a.menuSelected {
  color: #00676e;  /*/#363636; #323232; #1b74b2; */
}

.rightContainer {
	position: relative;
	width: 180px;
	border-left: 5px solid #FFF;
}
#newsHeader {
	position: relative;
	width: 170px;
	height: 17px;
	padding-left: 10px;
	padding-top: 3px;
	background-color: #2d7aa7;
	color: #FFF;
	font-weight: bold;
	font-size: 100%; //1em; //10px;
}
#newsContainer {
	position: relative;
	width: 158px;
	color: #000;
	padding: 10px;
	font-size: 100%; //1em; //10px;
	border-left: 1px solid #2d7aa7;
	border-right: 1px solid #2d7aa7;
	border-bottom: 1px solid #2d7aa7;
}
#newsContainer a {
	font-weight: bold;
	text-decoration: none;
}
#newsContainer a:hover {
	text-decoration: underline;
}
.newsLink {
	background-image: url(../images/news_arrow.gif);
	background-position: left;
	background-repeat: no-repeat;
	padding-left: 10px;
	padding-top: 3px;
}
.newsLink a {
	font-weight: normal;
	text-decoration: underline;
}
.newsLink a:hover {
	text-decoration: none;
}
#topcontent {
	position: relative;
	width: 100%; /*/915px;*/
	height: 260px;
	text-align: left;
}
#toolbar-top {
	position: relative;	
	width: 100%; /*/915px;*/
	height: 19px;
	text-align: right;
	padding-top: 11px;
	color: #bababa;
}
#toolbar-top a {
	color: #000;
	text-decoration: none;
}
#toolbar-top a:hover {
	text-decoration: underline;
}
#toolbar-design {
	position: relative;
	height: 217px;
	width: 100%; /*/915px;*/
	background-image: url(../images/logobar_bg_ny.gif);
	background-position: top;
	background-repeat: repeat-x;
}
#bar {
	position: relative;
	width: 100%; /*/915px;*/
	height: 19px;
	background-image: url(../images/bar_bg.gif);
	background-position: top;
	background-repeat: repeat-x;
	margin-top: 2px;
}
#path-design {
	position: relative;
	height: 17px;
	color: #4b4a4a;
	background-color: #f8f8f7;
	margin-top: 3px;
	padding-top: 3px;
	padding-left: 10px;
}
#path-design a {
	text-decoration: none;
}
#path-design a:hover {
	text-decoration: underline;
}
#user-design {
	position: absolute;
	width: 350px;
	right: 10px;
	top: 3px;
	text-align: right;
}
#user-design a {
	text-decoration: none;
}
#user-design a:hover {
	text-decoration: underline;
}
#maincontent {
	position: relative;
	margin-top: 3px;
	padding: 5px;
	background-color: #FFF;
}
#footer {
	position: relative;
	width: 100%; /*/915px;*/
	height: 20px;
	background-color: #f8f8f7;
	text-align: center;
	margin-top: 5px;
	font-size: 1em; //10px;
	color: #4b4a4a;
	padding-top: 5px;
}

/***** logo styles *****/

.logo {
	position: absolute;
	top: 8px;
	left: 13px;
}

/***********************************************/
/* Other styles                                */
/***********************************************/


/***** Article styles *****/
.attribute {
  margin-top: 0.6em;
}

.attribute-long {
  margin-top: 6px;
  font-size: 1.0em;
}

.attribute-byline{
	position: relative;
	width: 590px; /*/535px;*/
}

.attribute-pdf {
  margin-top: 1em;
}

.attribute-short{
  margin-top: 0.9em;
  font-weight: bold;
}

.author {
    text-align: left;
	margin-top: 1px;
	padding-top: 1px;
}

.date{
	position: absolute;
	width: 250px;
	right: 10px;
	top: 1px;
  text-align: right;
}
