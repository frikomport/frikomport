// For samspill med andre javascriptrammeverk //
var $j = jQuery.noConflict();

//---------------------------------------------------------------------------------------
//
//  Set active CSS
// 
//---------------------------------------------------------------------------------------

function changeFontsize() {
	if(finnesPaaSiden("body.fontsizeLargest")){
		 $j("body").removeClass("fontsizeLargest");
		 $j("body").addClass("fontsizeNormal");
	}
	else if(finnesPaaSiden("body.fontsizeNormal")){
		 $j("body").removeClass("fontsizeNormal");
		 $j("body").addClass("fontsizeLarge");
	}
	else {
		 $j("body").removeClass("fontsizeLarge");
		 $j("body").addClass("fontsizeLargest");
	}
	
}
	
function setFontsize(size){
	if(size == "normal"){
		$j("body").removeClass("fontsizeLarge");
		$j("body").removeClass("fontsizeLargest");
		$j("body").addClass("fontsizeNormal");
	}else if(size == "large"){
		$j("body").removeClass("fontsizeLargest");
		$j("body").removeClass("fontsizeNormal");
		$j("body").addClass("fontsizeLarge");
	}else{
		$j("body").removeClass("fontsizeNormal");
		$j("body").removeClass("fontsizeLarge");
		$j("body").addClass("fontsizeLargest");
	}

}

function getActiveFontsize(){
	if(finnesPaaSiden("body.fontsizeLarge")){
		return 'large'
	}else if(finnesPaaSiden("body.fontsizeLargest")){
		return 'largest'
	}else{
		return 'normal'
	}
}

function setContrastText(contrast) {
	
	var contrastText;
	var path = window.location.pathname;
	
	if(path.indexOf("/en/") != -1){ 
		contrastText = {
			empty: "High contrast",
			contrast: "Regular contrast"
		}
	}else{
		contrastText = {
			empty: "Høykontrast",
			contrast: "Standard kontrast"
		}
	}
	$j("#changeContrast").text(contrastText[contrast]);
}


function setContrast(contrast) {
	if (contrast == "empty") {
	    /*change css src*/
	    var oldHrefCss = $j("#contrast").attr("href");
		var newHrefCss = oldHrefCss.replace('contrast.css','empty.css');
		$j("#contrast").attr("href", newHrefCss);
		
		/*change css src for external css (used when EVS decorates another application)*/
		if(finnesPaaSiden("#contrast_external") ){
			var oldHrefCssExt = $j("#contrast_external").attr("href");
			var newHrefCssExt = oldHrefCssExt.replace('contrast.css','empty.css');
			$j("#contrast_external").attr("href", newHrefCssExt);
		}
		
		/*change print-icon*/
		if(finnesPaaSiden(".topbar a.contentprint img") ){
			var oldHrefPrinter = $j(".topbar a.contentprint img").attr("src");
			var newHrefPrinter = oldHrefPrinter.replace('printerikon-hoykontrast.gif','printerikon.gif');
			$j(".topbar a.contentprint img").attr("src", newHrefPrinter);
		}
		$j("#PageContainer").removeClass("highContrast");
	} else {
		/*change css src*/
		var oldHrefCss = $j("#contrast").attr("href");
		var newHrefCss = oldHrefCss.replace('empty.css','contrast.css');
		$j("#contrast").attr("href", newHrefCss);
		
		/*change css src for external css (used when EVS decorates another application)*/
		if(finnesPaaSiden("#contrast_external") ){
			var oldHrefCssExt = $j("#contrast_external").attr("href");
			var newHrefCssExt = oldHrefCssExt.replace('empty.css','contrast.css');
			$j("#contrast_external").attr("href", newHrefCssExt);
		}
		
		/*change print-icon*/
		if(finnesPaaSiden(".topbar a.contentprint img") ){
			var oldHrefPrinter = $j(".topbar a.contentprint img").attr("src");
			var newHrefPrinter = oldHrefPrinter.replace('printerikon.gif','printerikon-hoykontrast.gif');
			$j(".topbar a.contentprint img").attr("src", newHrefPrinter);
		}
		$j("#PageContainer").addClass("highContrast");
	}
	setContrastText(contrast);
}

function eh_toggleContrast(e) {
	e.preventDefault();
	
	// Sjekk href-attributtet p? link-taggen for ? se om det er vanlig kontrast eller h?ykontrast som er gjeldende
	if(getActiveContrast() == 'empty') {
		setContrast("contrast");
	}	else {
		setContrast("empty");
	}
}

function getActiveContrast(){
	var contrast = document.getElementById('contrast').href;
	if(contrast.indexOf('empty') != -1) return 'empty'
	else return 'contrast'
}

function eh_toggleFontsize(e){
	e.preventDefault();
	changeFontsize();
}

function createCookie(name, value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else {
		var expires = "";
	}
	document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function saveContrast_eh() {
	var contrast = getActiveContrast();
	createCookie('contrast', contrast, 365);
}

function saveFontsize_eh() {
	var fontsize = getActiveFontsize();
	createCookie('fontsize', fontsize, 365);
}

function loadContrastAndFontsize() {
	var cookiec = readCookie("contrast");
	var contrast= cookiec ? cookiec : 'empty'
	setContrast(contrast);
	
	// Prepare saving of contrast when leaving page
	$j(window).unload(saveContrast_eh);
	
	var cookief = readCookie("fontsize");
  var fontsize = cookief ? cookief : 'normal'
  setFontsize(fontsize);
  
  // Prepare saving of fontsize when leaving page
	$j(window).unload(saveFontsize_eh);
}


//---------------------------------------------------------------------------------------
//
//  External links should open in a new window
// 
//---------------------------------------------------------------------------------------

function eh_openInNewWindow(e) {
	e.preventDefault();
	window.open(this.href, '', '');
}


//---------------------------------------------------------------------------------------
//
//  Auto-submit form when selecting element in drop down list
// 
//---------------------------------------------------------------------------------------

function eh_autoSubmitForm(e) {
	this.form.submit();
}


//---------------------------------------------------------------------------------------
//
//  S?k i vegprosjekter
// 
//---------------------------------------------------------------------------------------

/*turnOffVPmapSrc kan bli overskrevet i initVegprosjekter og avhenger av evt. valgt fylke*/
var turnOffVPmapSrc = "images/vegprosjekterKart.gif";

function nullstill() {
	var form = $j("#vpAvansertSokForm").get(0);
	form.fylke.selectedIndex = 0;
	form.kommune.selectedIndex = 0;
	form.veg.selectedIndex = 0;
	form.fase.selectedIndex = 0;
	form.vegpakke.selectedIndex = 0;
	form.taMedFullforteProsjekter.checked = false
}

function fjernSokeresultat() {
	$j("#vpSok #resultatDirekteSok").empty();
	$j("#vpSok .pagenavigation").hide();
}

function aktiverMenypunkt(id) {
	$j("#vpFritekstSokMenypunkt").removeClass("selected");
	$j("#vpSokIKartMenypunkt").removeClass("selected");
	$j("#vpAvansertSokMenypunkt").removeClass("selected");
	$j(id).addClass("selected");	
}

function showAvansertVPSok() {
	aktiverMenypunkt("#vpAvansertSokMenypunkt");
	$j("#velgFylke").hide();
	$j(".sokeMeny").hide();
	$j("#avansertSok").show();
	$j("#valgtMetodeHeader").text("Avansert søk");
	$j("#hiddenSokemetode").attr("value", "avansert");
}

function showFritekstVPSok() {
	aktiverMenypunkt("#vpFritekstSokMenypunkt");
	//$j("#velgFylke").hide();
	$j("#avansertSok").hide();
	$j(".sokeMeny").show();
	$j("#valgtMetodeHeader").text("Fritekstsøk");
	$j("#hiddenFritekstSokemetode").attr("value", "fritekst");
}

function showVPSokIKart() {
	aktiverMenypunkt("#vpSokIKartMenypunkt");
	$j("#avansertSok").hide();
	$j(".sokeMeny").show();
	$j("#velgFylke").show();
	$j("#valgtMetodeHeader").text("Velg fylke");
	$j("#hiddenSokemetode").attr("value", "kart");
}
	
function eh_showAvansertVPSok(e) {
	e.preventDefault();
	showAvansertVPSok();
	fjernSokeresultat();
	nullstill();
}

function eh_showFritekstVPSok(e) {
	e.preventDefault();
	showFritekstVPSok();
	fjernSokeresultat();
}

function eh_showVPSokIKart(e) {
	e.preventDefault();
	showVPSokIKart();
	fjernSokeresultat();
	/*nullstill kartmarkering.*/
	norgeskartImagemap_basisbilde = norgeskartImagemap_basisbilde_original;
	eh_norgeskartImagemap_fjernMarkering();
}

function initVegprosjekter(highlightArea) {
	
	$j("#vpSokIKartLink").click(eh_showVPSokIKart);
	$j("#vpAvansertSokLink").click(eh_showAvansertVPSok);
	$j("#vpFritekstSokLink").click(eh_showFritekstVPSok);

	if ($j("#hiddenSokemetode").attr("value") == "avansert") {
		showAvansertVPSok();
	} else if ($j("#hiddenSokemetode").attr("value") == "fritekst") {
		showFritekstVPSok();
	} else {
		showVPSokIKart();
	}
}



//--------------------------------------
// 
// Bildegalleri
//
//--------------------------------------

var valgtBilde = 0;

// Henter angitt tekst fra bildegalleridata-span'ene (eks bildetekst0)
function tekst(type, id) {
	return $j('#datagrunnlag #' + type + id).text()
}

function antallBilder() {
	return parseInt($j('#antallBilder').text());
}		

function byttTilBilde(id) {
	$j('#bilde').attr('src', '');
	$j('#bilde').hide();
	$j('#bilde').attr('src', tekst('bilde', id));
	$j('#bilde').attr('alt', tekst('alttekst', id));
	$j('#bilde').fadeIn('slow');
	$j('.bildetekst').html(tekst('bildetekst', id));														
	$j('#bildeIndex').html(id + 1 + ' '); // Ekstra space for IE6
	$j('#bildelenke').attr('href', tekst('orginalStorrelse', id));					
}

function skjulBilde(navn) {
	$j('#' + navn + 'Bilde').hide();
	$j('#' + navn + 'Pil').css("cursor", "default");
}

function visBilde(navn) {
	$j('#' + navn + 'Bilde').show();
	$j('#' + navn + 'Pil').css("cursor", "pointer");
}


function disableLeftArrow() {
	$j('.venstrePil a').addClass('disabled');
}

function enableLeftArrow() {
	$j('.venstrePil a').removeClass('disabled');
}

function disableRightArrow() {
	$j('.hoyrePil a').addClass('disabled');
}

function enableRightArrow() {
	$j('.hoyrePil a').removeClass('disabled');
}

function hoyre() {					
	visBilde('venstre');				
	if (valgtBilde < antallBilder() - 1) {
		$j('#venstreBilde').attr('src', tekst('thumbnail', valgtBilde));
		$j('#midtBilde').attr('src', tekst('thumbnail', valgtBilde + 1));
		if (valgtBilde == antallBilder() - 2) {
			skjulBilde('hoyre');						
		} else {
			$j('#hoyreBilde').attr('src', tekst('thumbnail', valgtBilde + 2));
		}
		valgtBilde++;
		byttTilBilde(valgtBilde);

		if (valgtBilde == (antallBilder() - 1)) {
			disableRightArrow();
		}
		if (valgtBilde == 1) {
			enableLeftArrow();
		}
	}
}

function venstre() {
	visBilde('hoyre');
	if (valgtBilde > 0) {
		$j('#hoyreBilde').attr('src', tekst('thumbnail', valgtBilde));
		$j('#midtBilde').attr('src', tekst('thumbnail', valgtBilde - 1));
		if (valgtBilde == 1) {
			skjulBilde('venstre');
		} else {
			$j('#venstreBilde').attr('src', tekst('thumbnail', valgtBilde - 2));
		}
		valgtBilde--;
		byttTilBilde(valgtBilde);

		if (valgtBilde == 0) {
			disableLeftArrow();
		}
		if (valgtBilde == (antallBilder() - 2)) {
			enableRightArrow();
		}
	}
}

function initBildegalleri() {
	// Setter opp initiell tilstand p? bildene
	$j('#midtBilde').attr('src', tekst('thumbnail', 0));
	$j('#hoyreBilde').attr('src', tekst('thumbnail', 1));
	
	byttTilBilde(0);
				
	// Laster alle thumbnails for myk overgang n?r man skifter bilde
	$j('#datagrunnlag .thumbnail').each( function() {
			var preloader = new Image();
			preloader.src = this.innerHTML;
		}
	);
	
	$j("#bildegalleri").show();
	$j("#bildeCounter").show();
}

//---------------------------------------------------------------------------------------
//
//  Diverse enkeltfunksjoner  
// 
//---------------------------------------------------------------------------------------

function eh_print(e){
	e.preventDefault();
	window.print();	
}


// Bruker for ? bytte bilder i imagemaps inkludert i artiklene
function byttImagemapBilde(hovedbildeId, mouseoverBildeUrl) {
	document.getElementById(hovedbildeId).src = mouseoverBildeUrl;
}


//Checks all elements named 'name' in 'form'
function checkAll(formId, name) {
	var form = $j("#" + formId)[0];
	for (var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if (e.name == name) {
			e.checked = true;
		}
	}
}

//Unchecks all elements named 'name' in 'form'
function uncheckAll(formId, name) {
	var form = $j("#" + formId)[0];
	for (var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if (e.name == name) {
			e.checked = false;
		}
	}
}



//---------------------------------------------------------------------------------------
//
//  Google Analytics for webstatistikk
// 
//---------------------------------------------------------------------------------------

var pageTracker;

function googleAnalytics(){
	pageTracker = _gat._getTracker("UA-2627771-1");
	pageTracker._trackPageview();
}


//---------------------------------------------------------------------------------------
//
//  Funksjon for ? sette s?ketekst i s?kefeltet, hovedmenyen og funksjon for ? fjerne
// 
//---------------------------------------------------------------------------------------

function initSokeboks() {
	var path = window.location.pathname;
	
	if(path.indexOf("/en/") != -1){
		$j("#searchquery").val("Search..");
	}else{
		$j("#searchquery").val("Søk..");
	}
	
	
	$j("#searchquery").bind("focus", function() {
		if(path.indexOf("/en/") != -1){
			if($j(this).val() == "Search.."){
				$j(this).val("");	
			}
		}else{
			if($j(this).val() == "Søk.."){
				$j(this).val("");	
			}
		}
	});
	
	$j("#searchquery").bind("blur", function() {
		if($j(this).val() == ""){
			if(path.indexOf("/en/") != -1){
				$j(this).val("Search..");	
			}else{
				$j(this).val("Søk..");	
			}
		}
	});
}

function initVegmeldingerPaaForsiden() {
	$j("#vmPaaForsidenLenke").show();
	$j("#venteTekst").show();
	var url = $j("#lenkeTilXmlKilde").text();
	var request = $j.get(url, null, function(data, status) {
		// skjul ventetekst
		$j("#venteTekst").hide();
		
		var error = $j(data).find("exception");
		if (error.length != 0) {
			$j("#feilmeldingTekst").show();
			return;
		}
		
		var messages = $j(data).find("message");
		if (messages.length == 0) {
			$j("#ingenFunnetTekst").show();
			return;
		}
		 
		messages.each(function() {
	        var message = $j(this);
	        var messageType = $j(message).children("messageType");
	        var heading = $j(message).children("heading");
	        var hash = $j(message).children("hash");
	        var validFrom = $j(message).children("validFrom");
	        
			// opprette listeelement		        
	        var listElem = $j("#vmPaaForsidenMal").clone().show();
	        var spans = $j(listElem).find("span");
	        spans.eq(0).html(messageType.text());
	        spans.eq(1).html(heading.text());
	        if(validFrom != null && validFrom != ''){
		        spans.eq(2).html(validFrom.text());
	        }
	        
	        // modifisere lenke
	        var link = listElem.find("a");
	        var newUrl = link.attr('href') + "&ekspander=" + hash.text() + "#" + hash.text();
	        link.attr('href', newUrl); 

			// legge li element til ul
	        listElem.appendTo("#vmPaaForsiden");
		});
	});
}

function initSkjemabygger() {
	// Disse feilmeldingene m? synkroniseres med feilmeldingene for server-side validering
	// i skjemabygger.xsl, displayErrorMessage
	$j.extend($j.validator.messages, {  
		required: "Påkrevet felt",  
		email: "Ugyldig epostadresse",
		number: "Ugyldig tall"
	});
		
    $j("#skjema").validate();
}

//---------------------------------------------------------------------------------------
//
//  Trafikkstasjoner kontakt side
// 
//---------------------------------------------------------------------------------------

function initTrafikkstasjoner() {
	initTrafikkstasjonerLightbox();
}

//---------------------------------------------------------------------------------------
//  Lightbox popup for trafikkstasjoner kontaktinformasjon
//---------------------------------------------------------------------------------------
/*
Må ta spesielt hensyn til IE7 pga ClearType / fading - bug:
Ref: http://blog.bmn.name/2008/03/jquery-fadeinfadeout-ie-cleartype-glitch/
*/
var lightboxPopupStatus = 0;
var lightboxPopupExpanded = 0;


function lightboxPopupClose(){
	if(lightboxPopupStatus==1){
		if (jQuery.browser.msie) {
			//disable ClearType for better animation
			$j("#lightboxPopup").css({ 'background-color': '#fff'});
		}
		$j("#lightboxPopup .kart").empty(); /*clear out the google map*/
		$j("#lightboxPopup").fadeOut(500, function () {
			lightboxPopupReset();
		});
		$j("#lightboxBackground").fadeOut(500);
		lightboxPopupStatus = 0;
	}
}

function lightboxPopupReset(){
	$j("#lightboxPopup").css({'left':'378px'});
	$j("#lightboxPopup #innhold").css({'width':'225px'});
	$j("#lightboxPopup #info").hide();
	$j("#lightboxPopup #liste").hide();
	$j("#lightboxPopup").css('width','auto');
	lightboxPopupExpanded = 0;
}


function lightboxLoadContent(url){
	if (jQuery.browser.msie) {
		//disable ClearType for better animation
		$j("#lightboxPopup #info").css({ 'background-color': '#fbfbfb'});
	}
	
	if(lightboxPopupExpanded==0){
		$j("#lightboxPopup").css('width','777px'); // Må sette bredde for å unngå wrap i IE6. 30 + 714 + 30 (+ 3 ekstra piksler for IE6) = 777.
		$j("#lightboxPopup #innhold").animate({width:'714px'},750, function () {
			$j("#lightboxPopup #info").load(url, function () {
				$j("#lightboxPopup #info").fadeIn(300 , function () { 
				$j("#lightboxPopup #trafikkstasjonKart").show();
				initTrafikkstasjonKart();
				if (jQuery.browser.msie) {
					$j("#lightboxPopup #info").css({ 'background-color': 'transparent'});
				}	
				});
			});
		} );
		$j("#lightboxPopup").animate({"left": "200px"},750);
		lightboxPopupExpanded = 1;
	}
	else{
		$j("#lightboxPopup #trafikkstasjonKart").hide();
		$j("#lightboxPopup #info").fadeOut(300, function () { 
			$j("#lightboxPopup #info").load(url, function () {
				$j("#lightboxPopup #info").fadeIn(300, function () { 
					$j("#lightboxPopup #trafikkstasjonKart").show();
					initTrafikkstasjonKart();
					if (jQuery.browser.msie) {
					$j("#lightboxPopup #info").css({ 'background-color': 'transparent'});
				}
				});
			});
		} );
	}
}



function lightboxPopupOpen(url){
	if(lightboxPopupStatus==0){
		if (jQuery.browser.msie) {
			//disable ClearType for better animation
			$j("#lightboxPopup").css({ 'background-color': '#fff'});
			$j("#lightboxPopup #liste").css({ 'background-color': '#fbfbfb'});
		}
		$j("#lightboxBackground").css({ "opacity": "0.8" });
		$j("#lightboxBackground").fadeIn(500);
		
		$j("#lightboxPopup").fadeIn(300, function () {
			if (jQuery.browser.msie) {
				$j("#lightboxPopup").css({ 'background-color': 'transparent'});
			}
			$j("#lightboxPopup #liste").load(url, function () {
				$j("#lightboxPopup #liste").fadeIn(300, function () {
					if (jQuery.browser.msie) {
					$j("#lightboxPopup #liste").css({ 'background-color': 'transparent'});
					}
				});
				$j("#lightboxPopup #liste #fylkesnavn").focus();

			});
		});
		lightboxPopupStatus = 1;
	}
}

function lightboxPopupPosition(){ // Fix for IE6 slik at #lightboxBackground strekker seg langt nok ned på skjermen.
	if (jQuery.browser.msie) {
		var windowWidth = document.documentElement.clientWidth;
		var windowHeight = 991;
		$j("#lightboxBackground").css({
			"height": windowHeight,
			"width": windowWidth
		});
	}
}

/*kalles fra html-siden*/
function showStationsInCounty(countyUrl) {
		lightboxPopupPosition();
		lightboxPopupOpen(countyUrl);
}
/*kalles fra html-siden*/
function showInfoForStation(stationUrl) {
		lightboxLoadContent(stationUrl);
}


function initTrafikkstasjonerLightbox() {
				
	$j("#closeButton").click(function(){
		lightboxPopupClose();
	});

	$j("#lightboxBackground").click(function(){
		lightboxPopupClose();
	});
	
	$j(document).keypress(function(e){
		if(e.keyCode==27 && lightboxPopupStatus==1){
			//Pressed "escape"
			lightboxPopupClose();
		}
	});
}


/*****************************/
/*****felles norgeskart imagemap*******/
/*****************************/

/*norgeskartImagemap_basisbilde kan bli overskrevet i initVegprosjekter og avhenger av evt. valgt fylke*/
var norgeskartImagemap_basisbilde = "_public/www.vegvesen.no/images/norgeskart/norgeskart.gif";
var norgeskartImagemap_basisbilde_original = "_public/www.vegvesen.no/images/norgeskart/norgeskart.gif";

/*for å unngå både mouseout og mouseover ved bytte mellom fylker, setter vi et flagg ved mouseover (highlightFylke). */
/*vi forsinker mouseout, og sørger for at denne sjekker flagger før den eventuelt utfører sin action. */
var highlightFylkeFlagg = 0;
function eh_norgeskartImagemap_markereFylke() {
	$j("#norgeskart").attr("src", "_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea" + this.id.substr(7) + ".gif");
	highlightFylkeFlagg = 1;
}

function eh_norgeskartImagemap_fjernMarkering() {
	highlightFylkeFlagg = 0; //alltid nullstill flagg.
	//fadeTo(100,1) => vent for 100ms
	$j("#norgeskart").fadeTo(100, 1, function(){
		//sjekk om flagg er blitt satt av mouseover i tiden vi sov
		if(highlightFylkeFlagg==0){	
			$j("#norgeskart").attr("src", norgeskartImagemap_basisbilde);
		}
	});
	highlightFylkeFlagg = 0; //alltid nullstill flagg.
}

function preloadNorgeskartImageMap() { // Trengs for å unngå flickering i Opera når man beveger musa over kartet
	var imagesrc, imagesload;
	if (document.images)
	{
		imagesrc = new Array(
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea1.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea2.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea3.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea4.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea5.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea6.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea7.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea8.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea9.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea10.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea11.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea12.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea13.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea14.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea15.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea16.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea17.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea18.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea19.gif",
			"_public/www.vegvesen.no/images/norgeskart/norgeskart.gif" );
	
		imagesload = new Array();
	
		for(counter in imagesrc)
		{
			imagesload[counter] = new Image();
			imagesload[counter].src = imagesrc[counter];
		}
	}
}


function initNorgeskartImagemap(valgtFylke) {
	
	preloadNorgeskartImageMap();

	for (var i=1; i<=19; i++) {
			$j("#mapArea" + i).mouseover(eh_norgeskartImagemap_markereFylke);
			$j("#mapArea" + i).mouseout(eh_norgeskartImagemap_fjernMarkering);
	}
	
	/*vis kart med markert fylke*/
	if(valgtFylke != "-1" && valgtFylke != undefined){
		norgeskartImagemap_basisbilde = "_public/www.vegvesen.no/images/norgeskart/norgeskart-" + valgtFylke + ".gif";
		$j("#norgeskart").attr("src", norgeskartImagemap_basisbilde);
	}

}



/*****************************/
/**regionskart for orgkart****/
/*****************************/

var regionskartImagemap_basisbilde = "_public/www.vegvesen.no/images/orgkart/kart-region-default.gif";

/*for å unngå både mouseout og mouseover ved bytte mellom regioner, setter vi et flagg ved mouseover (highlightRegion). */
/*vi forsinker mouseout, og sørger for at denne sjekker flagger før den eventuelt utfører sin action. */
var highlightRegionFlagg = 0;
function eh_regionskartImagemap_markereRegion() {
	$j("#regionskart").attr("src", "_public/www.vegvesen.no/images/orgkart/kart-region-" + this.id.substr(7,1) + ".gif");
	$j("#mapArea"+this.id.substr(7,1)+"_text").addClass("hover");
	highlightRegionFlagg = 1;
}

function eh_regionskartImagemap_fjernMarkering() {
	highlightRegionFlagg = 0; //alltid nullstill flagg.
	//fadeTo(100,1) => vent for 100ms
	$j("#regionskart").fadeTo(100, 1, function(){
		//sjekk om flagg er blitt satt av mouseover i tiden vi sov
		if(highlightRegionFlagg==0){	
			$j("#regionskart").attr("src", regionskartImagemap_basisbilde);
		}
	});
	$j("#regioner a").removeClass("hover");
	highlightRegionFlagg = 0; //alltid nullstill flagg.
}

function preloadRegionskartImageMap() { // Trengs for å unngå flickering i Opera når man beveger musa over kartet
	var imagesrc, imagesload;
	
	if (document.images)
	{
		imagesrc = new Array(
			"_public/www.vegvesen.no/images/orgkart/kart-region-1.gif",
			"_public/www.vegvesen.no/images/orgkart/kart-region-2.gif",
			"_public/www.vegvesen.no/images/orgkart/kart-region-3.gif",
			"_public/www.vegvesen.no/images/orgkart/kart-region-4.gif",
			"_public/www.vegvesen.no/images/orgkart/kart-region-5.gif",
			"_public/www.vegvesen.no/images/orgkart/kart-region-default.gif" );
	
		imagesload = new Array(); 
	
		for(counter in imagesrc)
		{
			imagesload[counter] = new Image();
			imagesload[counter].src = imagesrc[counter];
		}
	}
}


function initRegionskartImagemap() {
	    
	preloadRegionskartImageMap();
	
	for (var i=1; i<=5; i++) {
		$j("#mapArea" + i).mouseover(eh_regionskartImagemap_markereRegion);
		$j("#mapArea" + i).mouseout(eh_regionskartImagemap_fjernMarkering);
		$j("#mapArea" + i + "_text").mouseover(eh_regionskartImagemap_markereRegion);
		$j("#mapArea" + i + "_text").mouseout(eh_regionskartImagemap_fjernMarkering);
	}

}

/**********************************************/
/**regionskart for orgkart på regionssider ****/
/**********************************************/

var regkontakt_regionskartImagemap_basisbilde = "_public/www.vegvesen.no/images/regionskontakt/kart-region-default.gif";

/*for å unngå både mouseout og mouseover ved bytte mellom regioner, setter vi et flagg ved mouseover (highlightRegion). */
/*vi forsinker mouseout, og sørger for at denne sjekker flagger før den eventuelt utfører sin action. */
var highlightRegionFlagg = 0;
function eh_regkontakt_regionskartImagemap_markereRegion() {
	$j("#regionskart").attr("src", "_public/www.vegvesen.no/images/regionskontakt/kart-region-" + this.id.substr(7,1) + ".gif");
	highlightRegionFlagg = 1;
}

function eh_regkontakt_regionskartImagemap_fjernMarkering() {
	highlightRegionFlagg = 0; //alltid nullstill flagg.
	//fadeTo(100,1) => vent for 100ms
	$j("#regionskart").fadeTo(100, 1, function(){
		//sjekk om flagg er blitt satt av mouseover i tiden vi sov
		if(highlightRegionFlagg==0){	
			$j("#regionskart").attr("src", regkontakt_regionskartImagemap_basisbilde);
		}
	});
	highlightRegionFlagg = 0; //alltid nullstill flagg.
}

function regkontakt_preloadRegionskartImageMap() { // Trengs for å unngå flickering i Opera når man beveger musa over kartet
	var imagesrc, imagesload;
	
	if (document.images)
	{
		imagesrc = new Array(
			"_public/www.vegvesen.no/images/regionskontakt/kart-region-1.gif",
			"_public/www.vegvesen.no/images/regionskontakt/kart-region-2.gif",
			"_public/www.vegvesen.no/images/regionskontakt/kart-region-3.gif",
			"_public/www.vegvesen.no/images/regionskontakt/kart-region-4.gif",
			"_public/www.vegvesen.no/images/regionskontakt/kart-region-5.gif",
			"_public/www.vegvesen.no/images/regionskontakt/kart-region-default.gif" );
	
		imagesload = new Array(); 
	
		for(counter in imagesrc)
		{
			imagesload[counter] = new Image();
			imagesload[counter].src = imagesrc[counter];
		}
	}
}

function initRegionsKontaktRegionskartImagemap() {
	    
	regkontakt_preloadRegionskartImageMap();
	
	markerValgtFylkeIRegionskart();	
	
	for (var i=1; i<=5; i++) {
		$j("#mapArea" + i).mouseover(eh_regkontakt_regionskartImagemap_markereRegion);
		$j("#mapArea" + i).mouseout(eh_regkontakt_regionskartImagemap_fjernMarkering);
		$j("#mapArea" + i + "_text").mouseover(eh_regkontakt_regionskartImagemap_markereRegion);
		$j("#mapArea" + i + "_text").mouseout(eh_regkontakt_regionskartImagemap_fjernMarkering);
	}

}

function markerValgtFylkeIRegionskart() {
	var valgtRegionIdAttributt = $j("#r_Kart area[class='selected']").attr("id");
	if (valgtRegionIdAttributt) {
		var valgtRegion = valgtRegionIdAttributt.substr(7,1);
		regkontakt_regionskartImagemap_basisbilde = "_public/www.vegvesen.no/images/regionskontakt/kart-region-" + valgtRegion + ".gif";
		$j("#regionskart").attr("src", regkontakt_regionskartImagemap_basisbilde);
	}
}

//---------------------------------------------------------------------------------------
//
//  eh_initPage() is the starting point for executing Javascript on the page
// 
//---------------------------------------------------------------------------------------

function finnesPaaSiden(element) {
	return (!(element === null) && $j(element).length > 0);
}

function eh_initPage() {
	// Sjekk om siden har sin egen, lokale Javascript-fil med funksjonen localInit(). Funksjonen utf?res hvis den finnes.
	
	
	if (typeof(localInit) == "function") {
		localInit();
	}

	if (finnesPaaSiden("#vpSok")) { // Sjekk om vi er på en side som har elementet for søking i vegprosjekter
		initVegprosjekter();
	}

	if (finnesPaaSiden("#norgeskart") && finnesPaaSiden("#m_Kart")) { //sjekk om vi er på en side med norgeskart imagemap
		var valgtFylke = "-1";
		var selectedAreaElement = $j("#m_Kart area[class='selected']");
		var valgtFylke = selectedAreaElement.attr("id");
		initNorgeskartImagemap(valgtFylke);
	}
	
	if (finnesPaaSiden("#regionskart") && finnesPaaSiden("#m_Kart")) { //sjekk om vi er på en side med norgeskart imagemap
		initRegionskartImagemap();
	}
	
	
	if (finnesPaaSiden("#regionskart") && finnesPaaSiden("#r_Kart")) { //sjekk om vi er på en side med norgeskart imagemap og det er regionkontaktsiden
		initRegionsKontaktRegionskartImagemap();
	}
	
	if (finnesPaaSiden("#offentligJournal")) {
		var picker = $j('#datepicker').datePicker({startDate:'01.01.2003'});
		if ($j('#datepicker').attr("value").length == 0) {
			var today = new Date();
			var yesterday = today.setDate(today.getDate()-1);
			picker.val(new Date(yesterday).asString()).trigger('change');
		}
	}

	if (finnesPaaSiden("#trafikkWebkameraKart")) {
		initTrafikkWebkameraKart();	
	}
	
	if (finnesPaaSiden("#trafikkWebkameraGoogleKart")) {
		initTrafikkWebkameraGoogleKart();	
	}
	
	if (finnesPaaSiden("#trafikkstasjonerKontaktinfo")) {
		initTrafikkstasjoner();		
	}
	
	if (finnesPaaSiden("#regionkontorKart")) {
	    initRegionsKontorKart();
	}
	
	
	if (finnesPaaSiden("#vmPaaForsiden")) {
		initVegmeldingerPaaForsiden();	
	}
	
	if (finnesPaaSiden("#vmEgenrapportSkjema")) {
		$j(".markeringsknapper").show();
	}	
		
	if (finnesPaaSiden("#skjema")) {
		initSkjemabygger();
	}
	
	if (finnesPaaSiden("#bildegalleri")) {
		initBildegalleri();
	}
	
	if(finnesPaaSiden(".topbar a.contentprint img") ){
		$j(".topbar a.contentprint").attr("style", "display:inline;");
		$j(".topbar a.contentprint img").attr("style", "display:inline;");
	}
	
	if (finnesPaaSiden("#kriseForside")) {
	    //Todo rydd opp og skill ut height og width
	    var kriseUrl = "#TB_inline?height=505&width=1004&inlineId=hiddenContent";
	    $j(".thickbox[id='kriseForside']").each(function () {
        $j(this).attr("href", kriseUrl);
        }).unbind("click").click(function () {
        var a = this.href || this.alt;
        tb_show('', a, null);
        this.blur();
        return false;
    });
	   tb_show('', kriseUrl, null);
	}
	
	// Test av ny karttjeneste
	if(finnesPaaSiden("#WMSmap")) {
	    // Lag kartet
        var map = new OpenLayers.Map( 'WMSmap', {
          //projection: new OpenLayers.Projection('EPSG:900913'),
		  projection: new OpenLayers.Projection('EPSG:32633'),
		  //displayProjection: new OpenLayers.Projection("EPSG:4326"),
		  //displayProjection: new OpenLayers.Projection("EPSG:32633"),
          maxExtent: new OpenLayers.Bounds(-20037508.34, -20037508.34, 20037508.34, 20037508.34),
          //units: 'm', 
		  units: 'dd', 
          maxResolution: 156543.0339, // tilsvarer zoom level 3 (hele er 21664.0)
          numZoomLevels: 18 // egentlig 21, men maxResolution tilsvarer zoom level 3 (følgelig er 0-3 skrudd av)
        } );
        map.addControl(new OpenLayers.Control.MouseDefaults());

         
         var point = new OpenLayers.LonLat(5.681571, 58.550532);

         // Hvorfor dukker ikke disse opp!?
         //var markers = new OpenLayers.Layer.Markers( "Markers" );
		 var markers = new OpenLayers.Layer.Markers( "Markers",  {sphericalMercator: true});
         map.addLayer(markers);
         var size = new OpenLayers.Size(21,25);
         var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
         var icon = new OpenLayers.Icon('http://www.openstreetmap.org/openlayers/img/marker.png',size);
         icon.setOpacity(0.5); 
         //Disse vises: 
		 //markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(166361,6834916),icon));
		 
		 //Rv. 53 Tyinosen - googlemap koordinater:   61.274399, 8.140033
		 //alert(OpenLayers.Layer.SphericalMercator.forwardMercator(61.274399,8.140033).lon); --> gir nesten riktig; 6821034.8943944
		 //alert(OpenLayers.Layer.SphericalMercator.forwardMercator(61.274399,8.140033).lat); --> gir feil 
		 
		 //marker = new OpenLayers.Marker(new OpenLayers.LonLat(166361,6800000),icon);
		 //marker2 = new OpenLayers.Marker(new OpenLayers.LonLat(169361,6820000),icon.clone());
		 
		 //Lik som på googlemaps tyinosen
		 //marker = new OpenLayers.Marker(new OpenLayers.LonLat(132606.90583,6812451.91051),icon);
		 marker = new OpenLayers.Marker(new OpenLayers.LonLat(132606.90583,6821034.8943944),icon);
		 
		 marker.events.register('mousedown', marker, function(evt) { alert(this.icon.url); OpenLayers.Event.stop(evt); }); 
		 markers.addMarker(marker);
		 //markers.addMarker(marker2);
		 
		 markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.forwardMercator(61.274399,8.140033),icon.clone())));
		  markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.inverseMercator(61.274399,8.140033),icon.clone())));
		   markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.forwardMercator(8.140033,61.274399),icon.clone())));
		    markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.inverseMercator(8.140033,61.274399),icon.clone())));
		 
		 //markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(176361,6934916),icon.clone()));
         
		 //Disse kommer da ikke opp...
		 // markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.forwardMercator(10.421000,59.937989),icon)));
		 // markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.inverseMercator(10.421000,59.937989),icon.clone())));
		 // markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.forwardMercator(59.937989,10.421000),icon.clone())));
		 // markers.addMarker(new OpenLayers.Marker((OpenLayers.Layer.SphericalMercator.inverseMercator(59.937989,10.421000),icon.clone())));
       
	     // alert('lon1' + OpenLayers.Layer.SphericalMercator.forwardMercator(10.421000,59.937989).lon);
         // alert('lat' + OpenLayers.Layer.SphericalMercator.forwardMercator(10.421000,59.937989).lat);
	   
	     // alert('lon2' + OpenLayers.Layer.SphericalMercator.inverseMercator(10.421000,59.937989).lon);
         // alert('lat' + OpenLayers.Layer.SphericalMercator.inverseMercator(10.421000,59.937989).lat);
		 
		 // alert('lon3' + OpenLayers.Layer.SphericalMercator.forwardMercator(59.937989,10.421000).lon);
         // alert('lat' + OpenLayers.Layer.SphericalMercator.forwardMercator(59.937989,10.421000).lat);
		 
		 // alert('lon4' + OpenLayers.Layer.SphericalMercator.inverseMercator(59.937989,10.421000).lon);
         // alert('lat' + OpenLayers.Layer.SphericalMercator.inverseMercator(59.937989,10.421000).lat);
	   
         markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(767427.763983, 9006846.112024),icon.clone()));
		 markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(767427, 9006846),icon.clone()));
		 markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(112024, 763983),icon.clone()));
		 markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(9006846, 767427),icon.clone()));
		 
		
         
         // markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(160060,85944),icon.clone()));
		 // markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(160060,859447),icon.clone()));
		 // markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(160060,385944),icon.clone()));
         
           
         //Note that if you pass an icon into the Marker constructor, it will take that icon and use it.  
         //This means that you should not share icons between markers -- you use them once, but you should clone() for any additional markers using that same icon.
 
		map.addControl(new OpenLayers.Control.Navigation());
		map.addControl(new OpenLayers.Control.PanZoomBar());
		map.addControl(new OpenLayers.Control.Permalink());
		map.addControl(new OpenLayers.Control.MousePosition());
		map.addControl(new OpenLayers.Control.LayerSwitcher()); 
         

         // Definer karttjenesten(e)
         var topo2 = new OpenLayers.Layer.WMS(
    		  
			
            //"topo2","http://wms.geonorge.no/skwms1/wms.kartdata2",
            
			
			//"topo2","http://openwms.statkart.no/skwms1/wms.kartdata2",
			
			"topo2","http://www.webatlas.no/wms-vegvesen_test",
            
			
			//{layers: 'Hoydelag,Veger,N5000Vegnavn,N2000Vegnavn,N1000Vegnavn,N500Vegnavn,N250Vegnavn,Stedsnavn,Tekst,Vannflate,N5000Vegnavn,N2000Vegnavn,N1000Vegnavn,N500Vegnavn,N250Vegnavn'},{attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'}
            //{layers: 'Kartdata2_WMS'},{attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'}
            //{layers: 'Hoydelag'},{attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'}
            //{layers: 'global_mosaic,Navn,Hoydelag,Vann,Omraadenavn'},{attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'}
            //{layers: 'ortofoto,Navn,Hoydelag,Vann,Omraadenavn'},{attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'}
            //{layers: 'Navn,Hoydelag,Markslag,Vann,Omraadenavn,Samferdsel'},{attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'}
         
         {layers: 'Navn,Hoydelag,Markslag,Vann,Omraadenavn,Samferdsel'}
         //{layers: 'Kartdata2_WMS'}
		 
		 
		 //,{sphericalMercator: true}
		 );
         
		  var statkart_sjo = new OpenLayers.Layer.WMS
		  (
			"Sjøkart hovedkartserien 2",
			"http://opencache.statkart.no/gatekeeper/gk/gk.open",
		 {
		 layers: 'sjo_hovedkart2',
		 format: 'image/png'
		 },
		 {
		 //
		 isBaseLayer: true,
		 attribution:'<a href="http://www.statkart.no">Statens kartverk</a>, <a href="http://www.statkart.no/nor/Land/Fagomrader/Geovekst/">Geovekst</a> og <a href="http://www.statkart.no/?module=Articles;action=Article.publicShow;ID=14194">kommuner</a>'
		 }
		 ); 
		 
         // Legg tjenesten(e) til kartet
         map.addLayer(topo2);
         OpenLayers.IMAGE_RELOAD_ATTEMPTS = 3;
         map.setCenter(new OpenLayers.LonLat(258181,7150509),5); //Hele norge
       
      
         
	}

	if(finnesPaaSiden("#feedbackForm")) {
	   
	   $j("#articleWasUseful").click(function(){
	       alert('Takk for tilbakemelding. Hilsen Statens vegvesen');
	       $j("#feedbackForm").submit();
	       });
	       
	   $j("#articleWasNotUseful").click(function(){
	       $j("#articleCommentTextarea").attr("style", "display:block;");
	       $j("#submitFormButton").attr("style", "display:block;");
	       $j(".savnerText").attr("style", "display:block;");
	   });
	   
	   $j("#submitFormButton").click(function(){
	       alert('Takk for tilbakemelding. Hilsen Statens vegvesen');
	       });
	     
	}
	
	
	$j("#vpSokNullstillKnapp").show(); // Vis "nullstill"-knapp for brukere med javascript

	
	loadContrastAndFontsize();
	
	$j(".external, .lastexternal").click(eh_openInNewWindow); // All links with this class should open in an external window
	$j(".autosubmit").change(eh_autoSubmitForm);  // All dropdown lists with this class should autosubmit the form when changed
	$j(".contentprint").click(eh_print);
	$j("#changeContrast").click(eh_toggleContrast);
	$j("#changeFontSize").click(eh_toggleFontsize);
	$j("#setNormalFontSize").click(function(e){e.preventDefault(); setFontsize('normal')});
	$j("#setLargeFontSize").click(function(e){e.preventDefault(); setFontsize('large')});
	$j("#setLargestFontSize").click(function(e){e.preventDefault(); setFontsize('largest')});
	
	// Kommentert ut siden vi heller vil ha ordet "søk" på selve søkeknappen
	//initSokeboks();
	
	googleAnalytics();

}

//---------------------------------------------------------------------------------------
//
//  eh_initPage() skal kalles n?r DOM er ferdig lastet
// 
//---------------------------------------------------------------------------------------

$j(document).ready(eh_initPage);