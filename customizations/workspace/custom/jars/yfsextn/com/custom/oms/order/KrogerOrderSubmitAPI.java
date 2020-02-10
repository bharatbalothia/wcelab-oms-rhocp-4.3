package com.kroger.oms.order;

import java.util.Date;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.icu.util.Calendar;
import com.kroger.oms.util.KrogerCommonUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.tools.datavalidator.XmlUtils;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class KrogerOrderSubmitAPI implements YIFCustomApi {
	
	private static final YFCLogCategory LOGGER = YFCLogCategory.instance(KrogerOrderSubmitAPI	.class.getName());

	public Document postOrdreDocument (YFSEnvironment env, Document inDoc) {
		try {

			Element eleRootDocIn = inDoc.getDocumentElement();

			Element eleOrder = SCXmlUtil.getXpathElement(eleRootDocIn, "//Order");
			String noOfOrders = eleOrder.getAttribute("NoOfOrders");
			int orderCount = Integer.parseInt(noOfOrders);
			String dateStr = String.valueOf(Calendar.DATE);
			if (!XmlUtils.isVoid(eleOrder)) {
				for (int i = 0; i <= orderCount; i++) {					
					eleOrder.setAttribute("OrderNo", "Kroger_"+dateStr+"_"+ i);
					KrogerCommonUtil.invokeService(env, "OrderLoad.Process", inDoc);
				}				
			} 
		} catch (Throwable e) {
			LOGGER.error(" Exception Occured in  : postOrdreDocument :" , e);
			throw new YFSException(e.getMessage());
		}
		return inDoc;
	
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
	