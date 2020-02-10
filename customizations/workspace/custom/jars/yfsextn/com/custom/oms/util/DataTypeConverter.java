/*
 * Copyright notice
 */
package com.kroger.oms.util;

// Java imports
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.xerces.dom.DocumentImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import com.yantra.yfc.log.YFCLogCategory;



/**
 * Class description goes here.
 *
 * 
 */
public final class DataTypeConverter {
	/**
	 * DataTypeConverter.java
	 */
	private DataTypeConverter() {
	}

	/**
	 * DATE_FORMAT : DATETIME_FORMAT : ISO_DATETIME_FORMAT :
	 */
	private static final String DATE_FORMAT = "yyyy-MM-dd", DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss", ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss-HH:mm",
	        SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static YFCLogCategory logger = YFCLogCategory.instance(DataTypeConverter.class);

	/**
	 * Converting ord.w3.Node to org.w3.Document
	 * 
	 * @param node
	 *            Node
	 * @return Document
	 */
	public static Document toDocument(Node node) {
		Document newDoc = null;
		try {
			if (node != null) {
				Element element = (Element) node;

				newDoc = new DocumentImpl();
				// Make a copy of the element subtree suitable for
				// inserting into newDoc
				Node dup = newDoc.importNode(element, true);
				// Insert the copy into newDoc
				newDoc.getDocumentElement().appendChild(dup);
			}
		} catch (Exception e) {
			logger.error("Error Detected: {}", e.getMessage(), e);
			
		}
		return newDoc;
	}

	/**
	 * @param inXML
	 *            Document
	 * @return Document
	 */
	public static String toString(Document inXML) {
		String outXMLStr = "";
		if (inXML != null) {
			outXMLStr = XMLUtil.getXMLString(inXML);
		}
		return outXMLStr;
	}

	/**
	 * @param node
	 *            Node
	 * @return String
	 */
	public static String toString(Node node) {
		String outXmlStr = "";
		try {
			if (node != null) {
				Document doc = new DocumentImpl();
				Node nodeDoc = doc.importNode(node, true);
				doc.appendChild(nodeDoc);
				if (doc != null) {
					outXmlStr = toString(doc);
				} // if (doc != null)
			} // if (node != null)
		} catch (Exception e) {
			logger.error("Error Detected: {}", e.getMessage(), e);
		}
		return outXmlStr;
	}

	/**
	 * This method converts Sql Date to String in MM/dd/yyyy format
	 * 
	 * @param value
	 *            java.sql.Date
	 * @return String
	 * @throws Exception
	 *             :
	 **/
	public static String sqlDateToString(java.sql.Date value) throws Exception {
		if (value == null) {

			return null;
		} else {
			return timestampToString(new Timestamp(value.getTime()), ISO_DATETIME_FORMAT);
		}
	}

	/**
	 * This converts TimeStamp to String in '"MM/dd/yyyy"' format
	 * 
	 * @param value
	 *            java.sql.Timestamp
	 * @return String
	 * @throws Exception
	 *             :
	 **/
	public static String timestampToString(Timestamp value) throws Exception {
		return timestampToString(value, ISO_DATETIME_FORMAT);
	}

	/**
	 * This method converts Sql Date to String in specified format
	 * 
	 * @param value
	 *            java.sql.Date
	 * @param format
	 *            String
	 * @return String
	 * @throws Exception
	 *             :
	 **/
	public static String sqlDateToString(java.sql.Date value, String format) throws Exception {
		if (value == null) {
			return null;
		} else {
			return timestampToString(new Timestamp(value.getTime()), format);
		}
	}

	/**
	 * This converts TimeStamp to String in Specified format
	 * 
	 * @param value
	 *            java.sql.Timestamp
	 * @param format
	 *            String
	 * @return String
	 * @throws Exception
	 *             :
	 **/
	public static String timestampToString(Timestamp value, String format) throws Exception {
		if (value == null) {
			return null;
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			java.util.Date d = new java.util.Date(value.getTime());
			return formatter.format(d);
		}
	}

	/**
	 * This converts Integer to String
	 * 
	 * @param value
	 *            Integer
	 * @return String
	 * @throws Exception
	 *             :
	 **/
	public static String integerToString(Integer value) throws Exception {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * This converts String to Date
	 * 
	 * @param data
	 *            String
	 * @return java.util.Date :
	 * @throws ParseException
	 *             :
	 **/
	public static java.util.Date stringToDate(String data) throws ParseException {
		if (data == null || data.equals("")) {
			return null;
		}

		SimpleDateFormat formatter = null;
		if (data.length() == DATE_FORMAT.length()) {
			formatter = new SimpleDateFormat(DATE_FORMAT);
		} else if (data.length() == DATETIME_FORMAT.length()) {
			formatter = new SimpleDateFormat(DATETIME_FORMAT);
		} else {
			formatter = new SimpleDateFormat(DATETIME_FORMAT);
		}
		return formatter.parse(data);
	}

	/**
	 * This converts String to Date
	 * 
	 * @param data
	 *            String
	 * @param dateFormat
	 *            String
	 * @return java.util.Date :
	 * @throws ParseException
	 *             :
	 **/
	public static java.util.Date stringToDate(String data, String dateFormat) throws ParseException {
		if (data == null || data.equals("")) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.parse(data);
	}

	/**
	 * This converts String to Date
	 * 
	 * @param data
	 *            String
	 * @return java.sql.Date
	 * @throws ParseException
	 *             :
	 **/
	public static java.sql.Date stringToSqlDate(String data) throws ParseException {
		return new java.sql.Date(((java.util.Date) stringToDate(data, SQL_DATETIME_FORMAT)).getTime());
	}

	/**
	 * Added by Mallik As stringToSqlDate() ignoring the time value for passed date.In FVC this method is introduced to get the time value also
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static java.sql.Timestamp stringToSqlTimestamp(String data) throws ParseException {
		return new java.sql.Timestamp(((java.util.Date) stringToDate(data, SQL_DATETIME_FORMAT)).getTime());
	}

	/**
	 * Convert String to int
	 * 
	 * @param str
	 *            String
	 * @return int
	 * @throws OMSAppException
	 *             extends Exception
	 */
	public static int stringToInt(String str) {
		int i = 0;
		if (str != null && str.trim().length() != 0) {
			return Integer.parseInt(str);
		} // if (str != null)
		return i;
	}

	/**
	 * Convert String to Double
	 * 
	 * @param str
	 *            String
	 * @return double
	 * @throws OMSAppException
	 *             extends Exception
	 */
	public static double stringToDouble(String str)  {
		double i = 0.0D;
		if (str != null && str.trim().length() != 0) {
			i = Double.parseDouble(str);

		} // if (str != null)
		return i;
	}

	/**
	 *
	 * Method: double2String
	 *
	 * This will convert Double object to String
	 *
	 * @param doubleObj
	 *            -Double object
	 * @return String
	 *
	 */
	public static String double2String(Double doubleObj) {
		String str = "";

		if (doubleObj == null) {
			str = "";
		} else {
			str = String.valueOf(doubleObj.doubleValue());
		}

		return str;
	}

	/**
	 * Convert String to Float
	 * 
	 * @param str
	 *            String
	 * @return float
	 * @throws OMSAppException
	 *             extends Exception
	 */
	public static float stringToFloat(String str) {
		float i = 0.0F;
		if (str != null && str.trim().length() != 0) {
			i = Float.parseFloat(str);

		} // if (str != null)
		return i;
	}

	/**
	 * Convert String to Double
	 * 
	 * @param str
	 *            String
	 * @return double
	 * @throws OMSAppException
	 *             extends Exception
	 */
	public static long stringToLong(String str) {
		long i = 0L;
		if (str != null && str.trim().length() != 0) {
			i = Long.parseLong(str);

		} // if (str != null)
		return i;
	}

	/**
	 * @param str
	 *            String
	 * @return boolean
	 * @throws OMSAppException
	 *             exnteds yfsexcet
	 */
	public static boolean stringToBoolean(String str)  {
		boolean rtnVal = false;
		if (str != null && str.trim().length() != 0) {
			try {
				rtnVal = (new Boolean(str.trim())).booleanValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rtnVal;
	}

	/**
	 *
	 * Method: getRoundUpDouble
	 *
	 * Returns the rounded-up decimal value to the specified decimal place.
	 *
	 * @param val
	 *            - double value.
	 * @param decimalPlace
	 *            - Decimal place to round off.
	 * @return
	 * @throws OMSAppException
	 *
	 * @see
	 * @since
	 *
	 */
	public static double getRoundUpDouble(double val, int decimalPlace)  {
		BigDecimal bd = new BigDecimal(val);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
		val = bd.doubleValue();

		return val;
	}

	/**
	 *
	 * Method: getRoundDownDouble
	 *
	 * Returns the rounded-down decimal value to the specified decimal place.
	 *
	 * @param val
	 *            - double value.
	 * @param decimalPlace
	 *            - Decimal place to round off.
	 * @return
	 * @throws OMSAppException
	 *
	 * @see
	 * @since
	 *
	 */
	public static double getRoundDownDouble(double val, int decimalPlace)  {
		BigDecimal bd = new BigDecimal(val);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_DOWN);
		val = bd.doubleValue();

		return val;
	}

	/**
	 * encode new line characters
	 * 
	 * @param value
	 * @return
	 */
	public static String encodeNewline(String value) {
		String encodedValue = value;
		try {
			if (value != null && value.length() > 0) {
				logger.info("Before ecncoding = "+ value);
				
				if (value.indexOf('\n') != -1) {
					encodedValue = URLEncoder.encode(value, "UTF-8");
					// replace + with blank space
					encodedValue = encodedValue.replace('+', ' ');
				}
				logger.info("After ecncoding = " + encodedValue);
				
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Error Detected: {}", e.getMessage(), e);
		}
		return encodedValue;
	}
}