package com.kroger.oms.util;

	import java.rmi.RemoteException;

import org.w3c.dom.Document;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

	/**
	 * 
	 * This class provides various utility methods which can be reused by all
	 * modules. Examples of such methods are: 1. Utility methods to invoke the apis
	 * and services 2. Utility methods to invoke commonly used APIs such as
	 * getOrderList etc
	 *
	 */
	public class KrogerCommonUtil {

		/**
		 * LoggerUtil Instance.
		 */
		private static YFCLogCategory logger = YFCLogCategory.instance(KrogerCommonUtil.class);
		/**
		 * Instance of YIFApi used to invoke Sterling Commerce APIs or services.
		 */
		private static YIFApi api;

		// Utility Class - Mask Constructor
		private KrogerCommonUtil() {

		}

		static {
			try {
				KrogerCommonUtil.api = YIFClientFactory.getInstance().getApi();
			} catch (Exception e) {
				KrogerCommonUtil.logger.error("IOM_UTIL_0001", e);
			}
		}

		/**
		 * Invokes a Sterling Commerce API.
		 * 
		 * @param env          Sterling Commerce Environment Context.
		 * @param templateName Name of API Output Template that needs to be set
		 * @param apiName      Name of API to invoke.
		 * @param inDoc        Input Document to be passed to the API.
		 * @throws java.lang.Exception Exception thrown by the API.
		 * @return Output of the API.
		 */
		public static Document invokeAPI(YFSEnvironment env, String templateName, String apiName, Document inDoc)
				throws Exception {
			env.setApiTemplate(apiName, templateName);
			Document returnDoc = KrogerCommonUtil.api.invoke(env, apiName, inDoc);
			env.clearApiTemplate(apiName);
			return returnDoc;
		}

		/**
		 * Invokes a Sterling Commerce API.
		 * 
		 * @param env      Sterling Commerce Environment Context.
		 * @param template Output template document for the API
		 * @param apiName  Name of API to invoke.
		 * @param inDoc    Input Document to be passed to the API.
		 * @throws java.lang.Exception Exception thrown by the API.
		 * @return Output of the API.
		 */
		public static Document invokeAPI(YFSEnvironment env, Document template, String apiName, Document inDoc)
				throws Exception {
			env.setApiTemplate(apiName, template);
			Document returnDoc = KrogerCommonUtil.api.invoke(env, apiName, inDoc);
			env.clearApiTemplate(apiName);
			return returnDoc;
		}

		/**
		 * Invokes a Sterling Commerce API.
		 * 
		 * @param env     Sterling Commerce Environment Context.
		 * @param apiName Name of API to invoke.
		 * @param inDoc   Input Document to be passed to the API.
		 * @throws java.lang.Exception Exception thrown by the API.
		 * @return Output of the API.
		 */
		public static Document invokeAPI(YFSEnvironment env, String apiName, Document inDoc) throws Exception {
			return KrogerCommonUtil.api.invoke(env, apiName, inDoc);
		}

		/**
		 * Invokes a Sterling Commerce API.
		 * 
		 * @param env      Sterling Commerce Environment Context.
		 * @param apiName  Name of API to invoke.
		 * @param inDocStr Input to be passed to the API. Should be a valid XML string.
		 * @throws java.lang.Exception Exception thrown by the API.
		 * @return Output of the API.
		 */
		public static Document invokeAPI(YFSEnvironment env, String apiName, String inDocStr) throws Exception {
			return KrogerCommonUtil.api.invoke(env, apiName, YFCDocument.parse(inDocStr).getDocument());
		}

		/**
		 * Invokes a Sterling Commerce Service.
		 * 
		 * @param env         Sterling Commerce Environment Context.
		 * @param serviceName Name of Service to invoke.
		 * @param inDoc       Input Document to be passed to the Service.
		 * @throws java.lang.Exception Exception thrown by the Service.
		 * @return Output of the Service.
		 */
		public static Document invokeService(YFSEnvironment env, String serviceName, Document inDoc)
				throws RemoteException {
			return KrogerCommonUtil.api.executeFlow(env, serviceName, inDoc);
		}

		/**
		 * Invokes a Sterling Commerce Service.
		 * 
		 * @param env         Sterling Commerce Environment Context.
		 * @param serviceName Name of Service to invoke.
		 * @param inDocStr    Input to be passed to the Service. Should be a valid XML
		 *                    String.
		 * @throws java.lang.Exception Exception thrown by the Service.
		 * @return Output of the Service.
		 */
		public static Document invokeService(YFSEnvironment env, String serviceName, String inDocStr) throws Exception {
			return KrogerCommonUtil.api.executeFlow(env, serviceName, YFCDocument.parse(inDocStr).getDocument());
		}


}
