package no.unified.soak.service.impl;

import java.io.InputStream;
import java.util.Locale;

import no.unified.soak.service.DecorCacheManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.StringUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class DecorCacheManagerImpl extends BaseManager implements DecorCacheManager {

	final static Log log = LogFactory.getLog(DecorCacheManagerImpl.class);
	private static String[] minimumPageDecoration = new String[] {
			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\r\n<head>\r\n", "</head>\r\n<body>\r\n",
			"</body>\r\n</html>\r\n" };
	private static String[] decorElements = minimumPageDecoration;

	private String decorationUrlBackup = "http://localhost:7937/mengdetrening/public/placeholderWebpageEmulator.txthtml";
	
	public void executeTask() {
		log.info("running DecorCacheManager");
		LocaleContextHolder.setLocale(ApplicationResourcesUtil.getNewLocaleWithDefaultCountryAndVariant(null));
		updateDecoration();
	}
	
	public void setLocale(Locale locale) {}
	public void setMessageSource(MessageSource messageSource) {}


	public String[] getDecorElements(){
		return decorElements;
	}
	
	private static synchronized void setDecorElements(String[] de){
		DecorCacheManagerImpl.decorElements = de;

		log.info("PageDecoration updated!");
	}

	public void updateDecoration(){ 
		// runs at server startup and according to TASK_RUN_INTERVAL_MILLISECOND
		String[] decor = fetchPageDecoration(ApplicationResourcesUtil.getText("global.pageDecorator.url"));
		if(decor == null){
			decor = fetchPageDecoration(decorationUrlBackup);
		}
		if(decor == null){
			decor = minimumPageDecoration;
		}
		setDecorElements(decor);
	}
	
	private String[] fetchPageDecoration(String decorationUrl) {
		String headPlaceholder = ApplicationResourcesUtil.getText("global.pageDecorator.headPlaceholder");
		String bodyPlaceholder = ApplicationResourcesUtil.getText("global.pageDecorator.bodyPlaceholder");
		StringBuffer pageDecorationBeforeHeadPleaceholder = new StringBuffer(1000);
		StringBuffer pageDecorationBetweenHeadAndBodyPleaceholders = new StringBuffer(1000);
		StringBuffer pageDecorationAfterBodyPleaceholder = new StringBuffer(1000);

		HttpClient client = new HttpClient(new SimpleHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		String ctmpl = getStringFromUrl(decorationUrl, client);

		if (StringUtils.isBlank(ctmpl)) {
			log.warn("Failing getting page decoration from [" + decorationUrl+"]");
			return null;
		}

		int baseStartPos = ctmpl.indexOf("<base ");
		int baseEndPos = ctmpl.indexOf(">", baseStartPos + 1);
		int headStartPos = ctmpl.indexOf(headPlaceholder);
		int bodyStartPos = ctmpl.indexOf(bodyPlaceholder, headStartPos + 1);

		if (headStartPos == -1) {
			log.warn("Unable to find headPlaceholder [" + headPlaceholder + "] in received page decoration [" + ctmpl + "].");
			return null;
		}
		if (bodyStartPos == -1) {
			log.warn("Unable to find bodyPlaceholder [" + bodyPlaceholder + "] in received page decoration [" + ctmpl + "].");
			return null;
		}

		if (baseStartPos > -1 && baseStartPos < headStartPos) {
			// Avoid base tag and fetch everything from start to head placeholder.
			pageDecorationBeforeHeadPleaceholder.append(ctmpl.substring(0, baseStartPos));
			pageDecorationBeforeHeadPleaceholder.append(ctmpl.substring(baseEndPos + 1, headStartPos));
		} else {
			// Base tag is void, do fetch everything from start to head placeholder.
			pageDecorationBeforeHeadPleaceholder.append(ctmpl.substring(0, headStartPos));
		}

		if (baseStartPos > headStartPos) {
			pageDecorationBetweenHeadAndBodyPleaceholders.append(ctmpl.substring(headStartPos + headPlaceholder.length(), baseStartPos));
			pageDecorationBetweenHeadAndBodyPleaceholders.append(ctmpl.substring(baseEndPos + 1, bodyStartPos));
		} else {
			pageDecorationBetweenHeadAndBodyPleaceholders.append(ctmpl.substring(headStartPos + headPlaceholder.length(), bodyStartPos));
		}

		pageDecorationAfterBodyPleaceholder.append(ctmpl.substring(bodyStartPos + bodyPlaceholder.length()));

		return new String[] { pageDecorationBeforeHeadPleaceholder.toString(),
				pageDecorationBetweenHeadAndBodyPleaceholders.toString(), pageDecorationAfterBodyPleaceholder.toString() };
	}

	private String getStringFromUrl(String decorationUrl, HttpClient client) {
		if (StringUtils.isEmpty(decorationUrl)) {
			log.warn("Page decoration Url is blank [" + decorationUrl + "] so no page decoration can be fetched.");
			return null;
		}
		GetMethod getMethod = new GetMethod(decorationUrl);
		getMethod.setFollowRedirects(true);
		int resultCode;
		boolean failureFetching = true;
		String ctmpl = null;
		try {
			resultCode = client.executeMethod(getMethod);
			InputStream ctmplStream = getMethod.getResponseBodyAsStream();
			ctmpl = StringUtil.convertStreamToString(ctmplStream, "UTF8");
			if (StringUtils.isBlank(ctmpl) || resultCode != 200) {
				log.warn("Page decoration is blank from [" + ctmpl + "] or have bad resultcode (" + resultCode
						+ ").\nUnable to fetch page decoration from url " + decorationUrl);
				failureFetching = true;
			}
			else if(!StringUtils.isBlank(ctmpl) && resultCode == 200 
					&& (ctmpl.indexOf(ApplicationResourcesUtil.getText("global.pageDecorator.headPlaceholder")) == -1 
							|| ctmpl.indexOf(ApplicationResourcesUtil.getText("global.pageDecorator.bodyPlaceholder")) == -1)){
				log.error(" -*-*-*-*-*-*-*-*-*- HTTP:200, men ikke placeHolder(s) fra " + decorationUrl + " -*-*-*-*-*-*-*-*-*-");
				log.error(ctmpl);
				log.error(" -*-*-*-*-*-*--*-*-*-*-*-*--*-*-*-*-*-*--*-*-*-*-*-*--*-*-*-*-*-*-");
				failureFetching = true;
			}
			else {
				log.info("Got page decoration "+ctmpl+" from " + decorationUrl + " with resultcode="+resultCode);
				failureFetching = false;
			}
		} catch (Exception e) {
			log.warn("Error connecting to [" + decorationUrl + "]: " + e);
		} finally {
			getMethod.releaseConnection();
		}
		if (!failureFetching) {
			return ctmpl;
		}
		return null;
	}
}
