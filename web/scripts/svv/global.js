//---------------------------------------------------------------------------------------
//
//  Set active CSS
// 
//---------------------------------------------------------------------------------------

function changeFontsize() {
	if(finnesPaaSiden("body.fontsizeLargest")){
		 $("body").removeClass("fontsizeLargest");
		 $("body").addClass("fontsizeNormal");
	}
	else if(finnesPaaSiden("body.fontsizeNormal")){
		 $("body").removeClass("fontsizeNormal");
		 $("body").addClass("fontsizeLarge");
	}
	else {
		 $("body").removeClass("fontsizeLarge");
		 $("body").addClass("fontsizeLargest");
	}
	
}
	
function setFontsize(size){
	if(size == "normal"){
		$("body").removeClass("fontsizeLarge");
		$("body").removeClass("fontsizeLargest");
		$("body").addClass("fontsizeNormal");
	}else if(size == "large"){
		$("body").removeClass("fontsizeLargest");
		$("body").removeClass("fontsizeNormal");
		$("body").addClass("fontsizeLarge");
	}else{
		$("body").removeClass("fontsizeNormal");
		$("body").removeClass("fontsizeLarge");
		$("body").addClass("fontsizeLargest");
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
	$("#changeContrast").text(contrastText[contrast]);
}


function setContrast(contrast) {
	if (contrast == "empty") {
	    /*change css src*/
	    var oldHrefCss = $("#contrast").attr("href");
		var newHrefCss = oldHrefCss.replace('contrast.css','empty.css');
		$("#contrast").attr("href", newHrefCss);
		
		/*change css src for external css (used when EVS decorates another application)*/
		if(finnesPaaSiden("#contrast_external") ){
			var oldHrefCssExt = $("#contrast_external").attr("href");
			var newHrefCssExt = oldHrefCssExt.replace('contrast.css','empty.css');
			$("#contrast_external").attr("href", newHrefCssExt);
		}
		
		/*change print-icon*/
		if(finnesPaaSiden(".topbar a.contentprint img") ){
			var oldHrefPrinter = $(".topbar a.contentprint img").attr("src");
			var newHrefPrinter = oldHrefPrinter.replace('printerikon-hoykontrast.gif','printerikon.gif');
			$(".topbar a.contentprint img").attr("src", newHrefPrinter);
		}
		$("#PageContainer").removeClass("highContrast");
	} else {
		/*change css src*/
		var oldHrefCss = $("#contrast").attr("href");
		var newHrefCss = oldHrefCss.replace('empty.css','contrast.css');
		$("#contrast").attr("href", newHrefCss);
		
		/*change css src for external css (used when EVS decorates another application)*/
		if(finnesPaaSiden("#contrast_external") ){
			var oldHrefCssExt = $("#contrast_external").attr("href");
			var newHrefCssExt = oldHrefCssExt.replace('empty.css','contrast.css');
			$("#contrast_external").attr("href", newHrefCssExt);
		}
		
		/*change print-icon*/
		if(finnesPaaSiden(".topbar a.contentprint img") ){
			var oldHrefPrinter = $(".topbar a.contentprint img").attr("src");
			var newHrefPrinter = oldHrefPrinter.replace('printerikon.gif','printerikon-hoykontrast.gif');
			$(".topbar a.contentprint img").attr("src", newHrefPrinter);
		}
		$("#PageContainer").addClass("highContrast");
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
	$(window).unload(saveContrast_eh);
	
	var cookief = readCookie("fontsize");
  var fontsize = cookief ? cookief : 'normal'
  setFontsize(fontsize);
  
  // Prepare saving of fontsize when leaving page
	$(window).unload(saveFontsize_eh);
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
	var form = $("#vpAvansertSokForm").get(0);
	form.fylke.selectedIndex = 0;
	form.kommune.selectedIndex = 0;
	form.veg.selectedIndex = 0;
	form.fase.selectedIndex = 0;
	form.vegpakke.selectedIndex = 0;
	form.taMedFullforteProsjekter.checked = false
}

function fjernSokeresultat() {
	$("#vpSok #resultatDirekteSok").empty();
	$("#vpSok .pagenavigation").hide();
}

function aktiverMenypunkt(id) {
	$("#vpFritekstSokMenypunkt").removeClass("selected");
	$("#vpSokIKartMenypunkt").removeClass("selected");
	$("#vpAvansertSokMenypunkt").removeClass("selected");
	$(id).addClass("selected");	
}

function showAvansertVPSok() {
	aktiverMenypunkt("#vpAvansertSokMenypunkt");
	$("#velgFylke").hide();
	$(".sokeMeny").hide();
	$("#avansertSok").show();
	$("#valgtMetodeHeader").text("Avansert søk");
	$("#hiddenSokemetode").attr("value", "avansert");
}

function showFritekstVPSok() {
	aktiverMenypunkt("#vpFritekstSokMenypunkt");
	//$("#velgFylke").hide();
	$("#avansertSok").hide();
	$(".sokeMeny").show();
	$("#valgtMetodeHeader").text("Fritekstsøk");
	$("#hiddenFritekstSokemetode").attr("value", "fritekst");
}

function showVPSokIKart() {
	aktiverMenypunkt("#vpSokIKartMenypunkt");
	$("#avansertSok").hide();
	$(".sokeMeny").show();
	$("#velgFylke").show();
	$("#valgtMetodeHeader").text("Velg fylke");
	$("#hiddenSokemetode").attr("value", "kart");
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
	
	$("#vpSokIKartLink").click(eh_showVPSokIKart);
	$("#vpAvansertSokLink").click(eh_showAvansertVPSok);
	$("#vpFritekstSokLink").click(eh_showFritekstVPSok);

	if ($("#hiddenSokemetode").attr("value") == "avansert") {
		showAvansertVPSok();
	} else if ($("#hiddenSokemetode").attr("value") == "fritekst") {
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
	return $('#datagrunnlag #' + type + id).text()
}

function antallBilder() {
	return parseInt($('#antallBilder').text());
}		

function byttTilBilde(id) {
	$('#bilde').attr('src', '');
	$('#bilde').hide();
	$('#bilde').attr('src', tekst('bilde', id));
	$('#bilde').attr('alt', tekst('alttekst', id));
	$('#bilde').fadeIn('slow');
	$('.bildetekst').html(tekst('bildetekst', id));														
	$('#bildeIndex').html(id + 1 + ' '); // Ekstra space for IE6
	$('#bildelenke').attr('href', tekst('orginalStorrelse', id));					
}

function skjulBilde(navn) {
	$('#' + navn + 'Bilde').hide();
	$('#' + navn + 'Pil').css("cursor", "default");
}

function visBilde(navn) {
	$('#' + navn + 'Bilde').show();
	$('#' + navn + 'Pil').css("cursor", "pointer");
}


function disableLeftArrow() {
	$('.venstrePil a').addClass('disabled');
}

function enableLeftArrow() {
	$('.venstrePil a').removeClass('disabled');
}

function disableRightArrow() {
	$('.hoyrePil a').addClass('disabled');
}

function enableRightArrow() {
	$('.hoyrePil a').removeClass('disabled');
}

function hoyre() {					
	visBilde('venstre');				
	if (valgtBilde < antallBilder() - 1) {
		$('#venstreBilde').attr('src', tekst('thumbnail', valgtBilde));
		$('#midtBilde').attr('src', tekst('thumbnail', valgtBilde + 1));
		if (valgtBilde == antallBilder() - 2) {
			skjulBilde('hoyre');						
		} else {
			$('#hoyreBilde').attr('src', tekst('thumbnail', valgtBilde + 2));
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
		$('#hoyreBilde').attr('src', tekst('thumbnail', valgtBilde));
		$('#midtBilde').attr('src', tekst('thumbnail', valgtBilde - 1));
		if (valgtBilde == 1) {
			skjulBilde('venstre');
		} else {
			$('#venstreBilde').attr('src', tekst('thumbnail', valgtBilde - 2));
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
	$('#midtBilde').attr('src', tekst('thumbnail', 0));
	$('#hoyreBilde').attr('src', tekst('thumbnail', 1));
	
	byttTilBilde(0);
				
	// Laster alle thumbnails for myk overgang n?r man skifter bilde
	$('#datagrunnlag .thumbnail').each( function() {
			var preloader = new Image();
			preloader.src = this.innerHTML;
		}
	);
	
	$("#bildegalleri").show();
	$("#bildeCounter").show();
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
	var form = $("#" + formId)[0];
	for (var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if (e.name == name) {
			e.checked = true;
		}
	}
}

//Unchecks all elements named 'name' in 'form'
function uncheckAll(formId, name) {
	var form = $("#" + formId)[0];
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
		$("#searchquery").val("Search..");
	}else{
		$("#searchquery").val("Søk..");
	}
	
	
	$("#searchquery").bind("focus", function() {
		if(path.indexOf("/en/") != -1){
			if($(this).val() == "Search.."){
				$(this).val("");	
			}
		}else{
			if($(this).val() == "Søk.."){
				$(this).val("");	
			}
		}
	});
	
	$("#searchquery").bind("blur", function() {
		if($(this).val() == ""){
			if(path.indexOf("/en/") != -1){
				$(this).val("Search..");	
			}else{
				$(this).val("Søk..");	
			}
		}
	});
}

function initVegmeldingerPaaForsiden() {
	$("#vmPaaForsidenLenke").show();
	$("#venteTekst").show();
	var url = $("#lenkeTilXmlKilde").text();
	var request = $.get(url, null, function(data, status) {
		// skjul ventetekst
		$("#venteTekst").hide();
		
		var error = $(data).find("exception");
		if (error.length != 0) {
			$("#feilmeldingTekst").show();
			return;
		}
		
		var messages = $(data).find("message");
		if (messages.length == 0) {
			$("#ingenFunnetTekst").show();
			return;
		}
		 
		messages.each(function() {
	        var message = $(this);
	        var messageType = $(message).children("messageType");
	        var heading = $(message).children("heading");
	        var hash = $(message).children("hash");
	        var validFrom = $(message).children("validFrom");
	        
			// opprette listeelement		        
	        var listElem = $("#vmPaaForsidenMal").clone().show();
	        var spans = $(listElem).find("span");
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
	$.extend($.validator.messages, {  
		required: "Påkrevet felt",  
		email: "Ugyldig epostadresse",
		number: "Ugyldig tall"
	});
		
    $("#skjema").validate();
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
			$("#lightboxPopup").css({ 'background-color': '#fff'});
		}
		$("#lightboxPopup .kart").empty(); /*clear out the google map*/
		$("#lightboxPopup").fadeOut(500, function () {
			lightboxPopupReset();
		});
		$("#lightboxBackground").fadeOut(500);
		lightboxPopupStatus = 0;
	}
}

function lightboxPopupReset(){
	$("#lightboxPopup").css({'left':'378px'});
	$("#lightboxPopup #innhold").css({'width':'225px'});
	$("#lightboxPopup #info").hide();
	$("#lightboxPopup #liste").hide();
	$("#lightboxPopup").css('width','auto');
	lightboxPopupExpanded = 0;
}


function lightboxLoadContent(url){
	if (jQuery.browser.msie) {
		//disable ClearType for better animation
		$("#lightboxPopup #info").css({ 'background-color': '#fbfbfb'});
	}
	
	if(lightboxPopupExpanded==0){
		$("#lightboxPopup").css('width','777px'); // Må sette bredde for å unngå wrap i IE6. 30 + 714 + 30 (+ 3 ekstra piksler for IE6) = 777.
		$("#lightboxPopup #innhold").animate({width:'714px'},750, function () {
			$("#lightboxPopup #info").load(url, function () {
				$("#lightboxPopup #info").fadeIn(300 , function () { 
				$("#lightboxPopup #trafikkstasjonKart").show();
				initTrafikkstasjonKart();
				if (jQuery.browser.msie) {
					$("#lightboxPopup #info").css({ 'background-color': 'transparent'});
				}	
				});
			});
		} );
		$("#lightboxPopup").animate({"left": "200px"},750);
		lightboxPopupExpanded = 1;
	}
	else{
		$("#lightboxPopup #trafikkstasjonKart").hide();
		$("#lightboxPopup #info").fadeOut(300, function () { 
			$("#lightboxPopup #info").load(url, function () {
				$("#lightboxPopup #info").fadeIn(300, function () { 
					$("#lightboxPopup #trafikkstasjonKart").show();
					initTrafikkstasjonKart();
					if (jQuery.browser.msie) {
					$("#lightboxPopup #info").css({ 'background-color': 'transparent'});
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
			$("#lightboxPopup").css({ 'background-color': '#fff'});
			$("#lightboxPopup #liste").css({ 'background-color': '#fbfbfb'});
		}
		$("#lightboxBackground").css({ "opacity": "0.8" });
		$("#lightboxBackground").fadeIn(500);
		
		$("#lightboxPopup").fadeIn(300, function () {
			if (jQuery.browser.msie) {
				$("#lightboxPopup").css({ 'background-color': 'transparent'});
			}
			$("#lightboxPopup #liste").load(url, function () {
				$("#lightboxPopup #liste").fadeIn(300, function () {
					if (jQuery.browser.msie) {
					$("#lightboxPopup #liste").css({ 'background-color': 'transparent'});
					}
				});
				$("#lightboxPopup #liste #fylkesnavn").focus();

			});
		});
		lightboxPopupStatus = 1;
	}
}

function lightboxPopupPosition(){ // Fix for IE6 slik at #lightboxBackground strekker seg langt nok ned på skjermen.
	if (jQuery.browser.msie) {
		var windowWidth = document.documentElement.clientWidth;
		var windowHeight = 991;
		$("#lightboxBackground").css({
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
				
	$("#closeButton").click(function(){
		lightboxPopupClose();
	});

	$("#lightboxBackground").click(function(){
		lightboxPopupClose();
	});
	
	$(document).keypress(function(e){
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
	$("#norgeskart").attr("src", "_public/www.vegvesen.no/images/norgeskart/norgeskart-mapArea" + this.id.substr(7) + ".gif");
	highlightFylkeFlagg = 1;
}

function eh_norgeskartImagemap_fjernMarkering() {
	highlightFylkeFlagg = 0; //alltid nullstill flagg.
	//fadeTo(100,1) => vent for 100ms
	$("#norgeskart").fadeTo(100, 1, function(){
		//sjekk om flagg er blitt satt av mouseover i tiden vi sov
		if(highlightFylkeFlagg==0){	
			$("#norgeskart").attr("src", norgeskartImagemap_basisbilde);
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
			$("#mapArea" + i).mouseover(eh_norgeskartImagemap_markereFylke);
			$("#mapArea" + i).mouseout(eh_norgeskartImagemap_fjernMarkering);
	}
	
	/*vis kart med markert fylke*/
	if(valgtFylke != "-1" && valgtFylke != undefined){
		norgeskartImagemap_basisbilde = "_public/www.vegvesen.no/images/norgeskart/norgeskart-" + valgtFylke + ".gif";
		$("#norgeskart").attr("src", norgeskartImagemap_basisbilde);
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
	$("#regionskart").attr("src", "_public/www.vegvesen.no/images/orgkart/kart-region-" + this.id.substr(7,1) + ".gif");
	$("#mapArea"+this.id.substr(7,1)+"_text").addClass("hover");
	highlightRegionFlagg = 1;
}

function eh_regionskartImagemap_fjernMarkering() {
	highlightRegionFlagg = 0; //alltid nullstill flagg.
	//fadeTo(100,1) => vent for 100ms
	$("#regionskart").fadeTo(100, 1, function(){
		//sjekk om flagg er blitt satt av mouseover i tiden vi sov
		if(highlightRegionFlagg==0){	
			$("#regionskart").attr("src", regionskartImagemap_basisbilde);
		}
	});
	$("#regioner a").removeClass("hover");
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
		$("#mapArea" + i).mouseover(eh_regionskartImagemap_markereRegion);
		$("#mapArea" + i).mouseout(eh_regionskartImagemap_fjernMarkering);
		$("#mapArea" + i + "_text").mouseover(eh_regionskartImagemap_markereRegion);
		$("#mapArea" + i + "_text").mouseout(eh_regionskartImagemap_fjernMarkering);
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
	$("#regionskart").attr("src", "_public/www.vegvesen.no/images/regionskontakt/kart-region-" + this.id.substr(7,1) + ".gif");
	highlightRegionFlagg = 1;
}

function eh_regkontakt_regionskartImagemap_fjernMarkering() {
	highlightRegionFlagg = 0; //alltid nullstill flagg.
	//fadeTo(100,1) => vent for 100ms
	$("#regionskart").fadeTo(100, 1, function(){
		//sjekk om flagg er blitt satt av mouseover i tiden vi sov
		if(highlightRegionFlagg==0){	
			$("#regionskart").attr("src", regkontakt_regionskartImagemap_basisbilde);
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
		$("#mapArea" + i).mouseover(eh_regkontakt_regionskartImagemap_markereRegion);
		$("#mapArea" + i).mouseout(eh_regkontakt_regionskartImagemap_fjernMarkering);
		$("#mapArea" + i + "_text").mouseover(eh_regkontakt_regionskartImagemap_markereRegion);
		$("#mapArea" + i + "_text").mouseout(eh_regkontakt_regionskartImagemap_fjernMarkering);
	}

}

function markerValgtFylkeIRegionskart() {
	var valgtRegionIdAttributt = $("#r_Kart area[class='selected']").attr("id");
	if (valgtRegionIdAttributt) {
		var valgtRegion = valgtRegionIdAttributt.substr(7,1);
		regkontakt_regionskartImagemap_basisbilde = "_public/www.vegvesen.no/images/regionskontakt/kart-region-" + valgtRegion + ".gif";
		$("#regionskart").attr("src", regkontakt_regionskartImagemap_basisbilde);
	}
}

//---------------------------------------------------------------------------------------
//
//  eh_initPage() is the starting point for executing Javascript on the page
// 
//---------------------------------------------------------------------------------------

function finnesPaaSiden(element) {
	return $(element).length > 0;
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
		var selectedAreaElement = $("#m_Kart area[class='selected']");
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
		var picker = $('#datepicker').datePicker({startDate:'01.01.2003'});
		if ($('#datepicker').attr("value").length == 0) {
			var today = new Date();
			var yesterday = today.setDate(today.getDate()-1);
			picker.val(new Date(yesterday).asString()).trigger('change');
		}
	}

	if (finnesPaaSiden("#trafikkWebkameraKart")) {
		initTrafikkWebkameraKart();	
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
		$(".markeringsknapper").show();
	}	
		
	if (finnesPaaSiden("#skjema")) {
		initSkjemabygger();
	}
	
	if (finnesPaaSiden("#bildegalleri")) {
		initBildegalleri();
	}

        if(finnesPaaSiden(".topbar a.contentprint img") ){
		$(".topbar a.contentprint").attr("style", "display:inline;");
		$(".topbar a.contentprint img").attr("style", "display:inline;");
	}

	if(finnesPaaSiden("#feedbackForm")) {
	   
	   $("#articleWasUseful").click(function(){
	       alert('Takk for tilbakemelding. Hilsen Statens vegvesen');
	       $("#feedbackForm").submit();
	       });
	       
	   $("#articleWasNotUseful").click(function(){
	       $("#articleCommentTextarea").attr("style", "display:block;");
	       $("#submitFormButton").attr("style", "display:block;");
	       $(".savnerText").attr("style", "display:block;");
	   });
	   
	   $("#submitFormButton").click(function(){
	       alert('Takk for tilbakemelding. Hilsen Statens vegvesen');
	       });
	     
	}
	
	
	$("#vpSokNullstillKnapp").show(); // Vis "nullstill"-knapp for brukere med javascript

	
	loadContrastAndFontsize();
	
	$(".external, .lastexternal").click(eh_openInNewWindow); // All links with this class should open in an external window
	$(".autosubmit").change(eh_autoSubmitForm);  // All dropdown lists with this class should autosubmit the form when changed
	$(".contentprint").click(eh_print);
	$("#changeContrast").click(eh_toggleContrast);
	$("#changeFontSize").click(eh_toggleFontsize);
	$("#setNormalFontSize").click(function(e){e.preventDefault(); setFontsize('normal')});
	$("#setLargeFontSize").click(function(e){e.preventDefault(); setFontsize('large')});
	$("#setLargestFontSize").click(function(e){e.preventDefault(); setFontsize('largest')});
	
	// Kommentert ut siden vi heller vil ha ordet "søk" på selve søkeknappen
	//initSokeboks();
	
	googleAnalytics();

}

//---------------------------------------------------------------------------------------
//
//  eh_initPage() skal kalles n?r DOM er ferdig lastet
// 
//---------------------------------------------------------------------------------------

$(document).ready(eh_initPage);