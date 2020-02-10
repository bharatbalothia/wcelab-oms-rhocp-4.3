package com.kroger.oms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yantra.yfc.log.YFCLogCategory;

/**
 * 
 */
public class XMLUtil {
	private static final int INT_FIVE = 5;
	private static final int INT_FOUR = 4;
	private static final int INT_SIX = 6;
	private static YFCLogCategory logger = YFCLogCategory.instance(XMLUtil.class);

	public static Document getDocumentForXMLStr(String xmlStr) {
		Document inXMLDoc = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
			DocumentBuilderFactory domFactory = null;
			DocumentBuilder domBuilder = null;
			domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			domBuilder = domFactory.newDocumentBuilder();
			inXMLDoc = domBuilder.parse(bais);

		} catch (Exception e) {
			logger.error("Exception occured while converting String Object to Document object.{}", e.getMessage(), e);

		}
		return inXMLDoc;
	}

	public static Document getDocumentForInputStream(InputStream in) {
		Document inXMLDoc = null;
		try {
			DocumentBuilderFactory domFactory = null;
			DocumentBuilder domBuilder = null;
			domFactory = DocumentBuilderFactory.newInstance();
			domBuilder = domFactory.newDocumentBuilder();
			inXMLDoc = domBuilder.parse(in);
		} catch (Exception e) {
			logger.error("Exception occured while converting String Object to Document object." + e.getMessage());
		}
		return inXMLDoc;
	}

	public static Document getDocumentForXML(String fileName) {
		Document inXMLDoc = null;
		try {

			DocumentBuilderFactory domFactory = null;
			DocumentBuilder domBuilder = null;
			domFactory = DocumentBuilderFactory.newInstance();
			domBuilder = domFactory.newDocumentBuilder();
			inXMLDoc = domBuilder.parse(new File(fileName));
		} catch (Exception e) {
			logger.error("Exception occured while creating the Document object..");
		}
		return inXMLDoc;
	}

	// common XML parsing apis
	public static Node getRootNode(Document inXMLDoc) {
		return inXMLDoc.getDocumentElement();
	}

	public static String getAttribute(Node node, String attName) {
		return getXMLAttribute(node, attName);
	}

	/**
	 * 
	 * Method: getXMLAttribute
	 * 
	 * This method returns the attribute in the node
	 * 
	 * @param node
	 * @param attName
	 * @return attVal
	 * @throws OMSAppException if the node or attVal is null or blank
	 * 
	 * @see
	 * @since
	 * 
	 */
	public static String getXMLAttribute(Node node, String attName) {
		if (null == node) {
			throw new IllegalArgumentException("node is null");
		}
		if (node instanceof Element) {
			return getXMLAttribute((Element) node, attName);
		} else {
			throw new IllegalArgumentException("Expected Element. Found " + node.getClass().getName());
		}
	}

	/**
	 * 
	 * Method: getXMLAttribute
	 * 
	 * This method returns the attribute in the node
	 * 
	 * @param node
	 * @param attName
	 * @return attVal
	 * @throws OMSAppException if the node or attVal is null or blank
	 * 
	 * @see
	 * @since
	 * 
	 */
	public static String getXMLAttribute(Element node, String attName) {
		String attVal = null;

		if (node != null) {
			attVal = node.getAttribute(attName);
			if (null != attVal) {
				attVal = attVal.trim();
			}
			return attVal;

		} else {
			return null;
		}
	}

	public static void setAttribute(Node node, String attName, String attVal) {
		((Element) node).setAttribute(attName, attVal);
	}

	public static Node getNode(Node parentNode, String childNodeName) {
		NodeList nodeList = parentNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals(childNodeName)) {
				return node;
			}
		}
		return null;
	}

	public static Document getNewDocument() {
		return new DocumentImpl();
	}

	public static String getNodeVal(Node node) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0, limit = nodeList.getLength(); i < limit; i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeType() == Node.TEXT_NODE) {
				return childNode.getNodeValue();
			}
		}
		return "";
	}

	/**
	 * Returns a string when passed a Document Object
	 * 
	 * @param document
	 * @return String
	 * @throws Exception
	 */
	public static String getXMLString(Document document) {
		try {
			OutputFormat oFmt = new OutputFormat(document, "UTF-8", true);
			// for output format, encoding is iso-8859-1 and indentation is
			// true.
			oFmt.setPreserveSpace(true);
			XMLSerializer xmlOP = new XMLSerializer(oFmt);
			// XMLSerializer xmlOP = new XMLSerializer();
			StringWriter strWriter = new StringWriter();
			xmlOP.setOutputCharStream(strWriter);
			xmlOP.serialize(document);

			return strWriter.toString();
		} catch (DOMException e) {

			return null;
		} catch (IOException e) {

			return null;
		}
	}

	/**
	 * given a node gets a particular sibling node given the sibling's nodename in a
	 * string
	 * 
	 * @param node      the node with which a particular sibling has to be found
	 * @param childname Search string for the sibling
	 * @return node with for the sibling requested
	 * 
	 */

	public static Node getNextSiblingByName(Node node, String childName) {

		Node resultNode = null;

		node = node.getNextSibling();
		while (node != null) {
			String nodeName = node.getNodeName();
			if (nodeName.compareTo(childName) == 0) {
				resultNode = node;
				break;
			}

			node = node.getNextSibling();
		}
		return resultNode;
	} // End of method getnextSiblingByName

	/**
	 * 
	 * Method: getErrorDocument
	 * 
	 * create the error document
	 * 
	 * @param errorCode
	 * @param errorMsg
	 * @param stack
	 * @return
	 * @throws Exception
	 * 
	 * @see
	 * @since
	 * 
	 */
	public static Document getErrorDocument(String errorCode, String errorMsg, String stack) throws Exception {
		Document returnXML = null;
		try {
			returnXML = new DocumentImpl();
			Element rootEle = returnXML.createElement("Errors");
			returnXML.appendChild(rootEle);
			Element errorElement = returnXML.createElement("Error");
			errorElement.setAttribute("ErrorCode", errorCode);
			errorElement.setAttribute("ErrorDescription", errorMsg);
			rootEle.appendChild(errorElement);
			Text stackElement = returnXML.createTextNode("Stack");
			stackElement.setData(stack);
			errorElement.appendChild(stackElement);
		} catch (Exception ex) {
			throw ex;
		}
		return returnXML;
	} // End of method getnextSiblingByName

	public static Document getYFSExceptionErrorDocument(String errorCode, String errMsg) {

		/*
		 * Prepares the Document with the following XML structure. <Errors> <Error
		 * ErrorCode="" ErrorDescription=""> </Error> </Errors>
		 */

		Document errorDoc = getNewDocument();
		Element errorsEle = errorDoc.createElement("Errors");
		errorDoc.appendChild(errorsEle);

		Element errorEle = errorDoc.createElement("Error");
		errorEle.setAttribute("ErrorCode", errorCode);
		errorEle.setAttribute("ErrorDescription", errMsg);

		errorsEle.appendChild(errorEle);

		return errorDoc;
	}

	public static Document getYFSExceptionErrorDocument(String errorCode, String errMsg, String moreInfo) {

		/*
		 * Prepares the Document with the following XML structure. <Errors> <Error
		 * ErrorCode="" ErrorDescription="" ErrorRelatedMoreInfo=""> </Error> </Errors>
		 */

		Document errorDoc = getNewDocument();
		Element errorsEle = errorDoc.createElement("Errors");
		errorDoc.appendChild(errorsEle);

		Element errorEle = errorDoc.createElement("Error");
		errorEle.setAttribute("ErrorCode", errorCode);
		errorEle.setAttribute("ErrorDescription", errMsg);
		errorEle.setAttribute("ErrorRelatedMoreInfo", moreInfo);

		errorsEle.appendChild(errorEle);

		return errorDoc;
	}

	/**
	 * 
	 * 
	 *
	 */
	public static DocumentBuilder getDomBuilder() {
		DocumentBuilder domBuilder = null;
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			domBuilder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			logger.error("Error detected {}", pce.getMessage(), pce);

			throw new RuntimeException("Failed to create DomBuilder", pce);
		}
		return domBuilder;
	}

	public static final String getStringAttrVal(Element e, String name) {
		if (e == null || name == null) {
			throw new IllegalArgumentException("element is null");
		}

		if (e.hasAttribute(name)) {
			return e.getAttribute(name);
		}
		return null;
	}

	public static final Double getDoubleAttrVal(Element e, String name) {
		String str = getStringAttrVal(e, name);
		if (str != null && str.trim().length() > 0) {
			return new Double(DataTypeConverter.stringToDouble(str));
		}
		return null;
	}

	/**
	 * 
	 * 
	 * /** Writes XML document into output stream. It is the users responsibility to
	 * close the stream after use.
	 * 
	 * @param document to be streamed. not null.
	 * @param os       used output stream. not null.
	 * @throws Exception if stream or xml problem occurs
	 */
	public static void documentToStream(Document document, OutputStream os) throws Exception {
		if (document == null || os == null) {
			throw new IllegalArgumentException("invalid parameter");
		}

		String xml = XMLUtil.getXMLString(document);

		os.write(xml.getBytes());
	}

	public static Node renameNode(Node f, String newName, boolean deep) {
		Document d = new DocumentImpl();
		Node r = d.createElement(newName);

		r = copyAttributes(f, r);

		if (deep) {
			NodeList nl = f.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node n1 = renameNode(nl.item(i), nl.item(i).getNodeName(), deep);
				r.appendChild(n1);
			}
		}
		return r;
	}

	public static Node copyAttributes(Node f, Node n) {

		switch (f.getNodeType()) {
		case Node.ELEMENT_NODE:
			NamedNodeMap map = f.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				((Element) n).setAttribute(((Attr) map.item(i)).getName(), ((Attr) map.item(i)).getValue());
			}
			break;
		}
		return n;
	}

	/**
	 * getChildren() will return list of all immediate child of the parent which
	 * have specified child name.
	 * 
	 * @param ele
	 * @param childName
	 * @return
	 */
	public static List<Element> getChildren(Element ele, String childName) {
		if (XMLUtil.isVoid(childName)) {
			return null;
		}
		List<Element> list = new ArrayList<Element>();
		if (ele != null && ele.hasChildNodes()) {
			NodeList childList = ele.getChildNodes();
			for (int i = 0; i < childList.getLength(); i++) {
				if (childList.item(i) instanceof Element && childName.equals(childList.item(i).getNodeName())) {
					list.add((Element) childList.item(i));
				}
			}
		}
		return list;
	}

	// private static DocumentBuilderFactory fac;
	//
	// /*This will ensure that only one instance of DocumentBuilderFactory is
	// created*/
	// static {
	// this.fac = DocumentBuilderFactory.newInstance();
	// }
	//
	/**
	 * Create a new blank XML Document
	 * 
	 * @return Document type
	 * @throws ParserConfigurationException throws ParserConfiguration exception
	 */
	public static Document newDocument() throws ParserConfigurationException {

		return getDocumentBuilder().newDocument();
	}

	/**
	 * Parse an XML string or a file, to return the Document.
	 * 
	 * @param inXML if starts with '&lt;', it is an XML string; otherwise it should
	 *              be an XML file name.
	 * 
	 * @return the Document object generated
	 * @throws ParserConfigurationException when XML parser is not properly
	 *                                      configured.
	 * @throws SAXException                 when failed parsing XML string.
	 * @throws IOException                  throws IO Exception
	 */
	public static Document getDocument(String inXML) throws ParserConfigurationException, SAXException, IOException {
		Document retVal = null;
		if (inXML != null) {
			String modifiedInXML = inXML.trim();
			if (modifiedInXML.length() > 0) {
				if (modifiedInXML.startsWith("<")) {
					StringReader strReader = new StringReader(modifiedInXML);
					InputSource iSource = new InputSource(strReader);
					return XMLUtil.getDocument(iSource);
				}

				// It's a file
				FileReader inFileReader = new FileReader(modifiedInXML);
				try {
					InputSource iSource = new InputSource(inFileReader);
					retVal = XMLUtil.getDocument(iSource);
				} finally {
					inFileReader.close();
				}
			}
		}
		return retVal;
	}

	/**
	 * Generate a Document object according to InputSource object.
	 * 
	 * @param inSource input source
	 * @return Document sterling input document type
	 * @throws ParserConfigurationException when XML parser is not properly
	 *                                      configured.
	 * @throws SAXException                 when failed parsing XML string.
	 * @throws IOException                  throws IO Exception
	 */
	public static Document getDocument(InputSource inSource)
			throws ParserConfigurationException, SAXException, IOException {

		return getDocumentBuilder().parse(inSource);
	}

	private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		// fac.setNamespaceAware(Y.equalsIgnoreCase(YFSSystem.getProperty(IS_NAMESPACE_AWARE_PROP)));
		DocumentBuilder dbdr = fac.newDocumentBuilder();
		return dbdr;
	}

	/**
	 * Generate a Document object according to InputStream object.
	 * 
	 * @param inStream Input Stream
	 * @return Document Steling input document type
	 * @throws ParserConfigurationException when XML parser is not properly
	 *                                      configured.
	 * @throws SAXException                 when failed parsing XML string.
	 * @throws IOException                  throws IO Excepttion
	 */
	public static Document getDocument(InputStream inStream)
			throws ParserConfigurationException, SAXException, IOException {
		Document retDoc = XMLUtil.getDocument(new InputSource(new InputStreamReader(inStream)));
		inStream.close();
		return retDoc;
	}

	/**
	 * Parse an XML file, to return the Document.
	 * 
	 * @deprecated use getDocument(String) instead.
	 * @param inXMLFileName input XML file name
	 * @param isFile        flag to ensure the if file or directory
	 * @return the Document object generated
	 * @throws ParserConfigurationException when XML parser is not properly
	 *                                      configured.
	 * @throws SAXException                 when failed parsing XML string.
	 * @throws IOException                  throws IO Exception
	 */
	@Deprecated
	public static Document getDocument(String inXMLFileName, boolean isFile)
			throws ParserConfigurationException, SAXException, IOException {
		if ((inXMLFileName != null) && (!inXMLFileName.equals(""))) {
			FileReader inFileReader = new FileReader(inXMLFileName);
			InputSource iSource = new InputSource(inFileReader);
			Document doc = XMLUtil.getDocument(iSource);
			inFileReader.close();
			return doc;
		}
		return null;
	}

	/**
	 * Create a Document object with input as the name of document element.
	 * 
	 * @param docElementTag the document element name.
	 * @return Document Sterling input document type
	 * @throws ParserConfigurationException throws parser configuration exception
	 */
	public static Document createDocument(String docElementTag) throws ParserConfigurationException {

		Document doc = getDocumentBuilder().newDocument();
		Element ele = doc.createElement(docElementTag);
		doc.appendChild(ele);
		return doc;
	}

	/**
	 * Merges document doc2 in to doc1. For e.g.,
	 * <p>
	 * if doc1 = &lt;Root1>&lt;A1/>&lt;/Root1>
	 * <p>
	 * & doc2 = &lt;Root2>&lt;B1/>&lt;Root2>
	 * <p>
	 * then the merged Doc will be doc1 = &lt;Root1>&lt;A1/>&lt;B1/>&lt;/Root1>
	 * 
	 * @param doc1 XML Document 1
	 * @param doc2 XML Document 2
	 * @return Document Sterling input document
	 * @deprecated use addDocument(Document doc1,Document doc2, boolean ignoreRoot)
	 */
	@Deprecated
	public static Document addDocument(Document doc1, Document doc2) {
		Element rt1 = doc1.getDocumentElement();
		Element rt2 = doc2.getDocumentElement();

		NodeList nlst2 = rt2.getChildNodes();
		int len = nlst2.getLength();
		Node nd = null;
		for (int i = 0; i < len; i++) {
			nd = doc1.importNode(nlst2.item(i), true);
			rt1.appendChild(nd);
		}
		return doc1;
	}

	/**
	 * Merges document doc2 in to doc1. Root node of doc2 is included only if
	 * ignoreRoot flag is set to false.
	 * <p/>
	 * For e.g.,
	 * <p>
	 * if doc1 = &lt;Root1>&lt;A1/>&lt;/Root1>
	 * <p>
	 * & doc2 = &lt;Root2>&lt;B1/>&lt;Root2>
	 * <p>
	 * then the merged Doc will be doc1 =
	 * &lt;Root1>&lt;A1/><B>&lt;Root2>&lt;B1/>&lt;Root2></B>&lt;/Root1> <B>if
	 * ignoreRoot = false</B>
	 * <p>
	 * <B>if ignoreRoot = true</B> then the merged Doc will be
	 * <p>
	 * doc1 = &lt;Root1>&lt;A1/><B>&lt;B1/></B>&lt;/Root1>
	 * 
	 * @param doc1       XML Document1
	 * @param doc2       XML Document2
	 * @param ignoreRoot ignores root element of doc2 in the merged doc.
	 * @return Document document type
	 */
	public static Document addDocument(Document doc1, Document doc2, boolean ignoreRoot) {
		Element rt1 = doc1.getDocumentElement();
		Element rt2 = doc2.getDocumentElement();
		if (!ignoreRoot) {
			Node nd = doc1.importNode(rt2, true);
			rt1.appendChild(nd);
			return doc1;
		}
		NodeList nlst2 = rt2.getChildNodes();
		int len = nlst2.getLength();
		Node nd = null;
		for (int i = 0; i < len; i++) {
			nd = doc1.importNode(nlst2.item(i), true);
			rt1.appendChild(nd);
		}
		return doc1;
	}

	/**
	 * Create a new Document with the given Element as the root node.
	 * 
	 * @param inElement input element from XML
	 * @return Document Sterling input document type
	 * @throws Exception throws generic exception
	 */
	public static Document getDocumentForElement(Element inElement) throws Exception {

		Document doc = getDocumentBuilder().newDocument();
		doc.importNode(inElement, true);

		return doc;
	}

	/**
	 * Returns a formatted XML string for the Node, using encoding 'iso-8859-1'.
	 * 
	 * @param node a valid document object for which XML output in String form is
	 *             required.
	 * 
	 * @return the formatted XML string.
	 */

	public static String serialize(Node node) {
		return XMLUtil.serialize(node, "iso-8859-1", true);
	}

	/**
	 * Return a XML string for a Node, with specified encoding and indenting flag.
	 * <p>
	 * <b>Note:</b> only serialize DOCUMENT_NODE, ELEMENT_NODE, and
	 * DOCUMENT_FRAGMENT_NODE
	 * 
	 * @param node      the input node.
	 * @param encoding  such as "UTF-8", "iso-8859-1"
	 * @param indenting indenting output or not.
	 * 
	 * @return the XML string
	 */
	public static String serialize(Node node, String encoding, boolean indenting) {
		OutputFormat outFmt = null;
		StringWriter strWriter = null;
		XMLSerializer xmlSerializer = null;
		String retVal = null;

		try {
			outFmt = new OutputFormat("xml", encoding, indenting);
			outFmt.setOmitXMLDeclaration(true);
			strWriter = new StringWriter();

			xmlSerializer = new XMLSerializer(strWriter, outFmt);

			int ntype = node.getNodeType();

			switch (ntype) {
			case Node.DOCUMENT_FRAGMENT_NODE:
				xmlSerializer.serialize((DocumentFragment) node);
				break;
			case Node.DOCUMENT_NODE:
				xmlSerializer.serialize((Document) node);
				break;
			case Node.ELEMENT_NODE:
				xmlSerializer.serialize((Element) node);
				break;
			default:
				throw new IOException("Can serialize only Document, DocumentFragment and Element type nodes");
			}

			retVal = strWriter.toString();
		} catch (IOException e) {
			retVal = e.getMessage();
		} finally {
			try {
				// added for violation-Value is null and guaranteed to be
				// dereferenced on exception path fix
				if (strWriter != null)
					strWriter.close();
			} catch (IOException ie) {
				retVal = ie.getMessage();

			}
		}

		return retVal;
	}

	/**
	 * Return a decendent of first parameter, that is the first one to match the
	 * XPath specified in the second parameter.
	 * 
	 * @param ele     The element to work on.
	 * @param tagName format like "CHILD/GRANDCHILD/GRANDGRANDCHILD"
	 * 
	 * @return the first element that matched, null if nothing matches.
	 */
	public static Element getFirstElementByName(Element ele, String tagName) {
		StringTokenizer st = new StringTokenizer(tagName, "/");
		Element curr = ele;
		Node node;
		String tag;
		while (st.hasMoreTokens()) {
			tag = st.nextToken();
			node = curr.getFirstChild();
			while (node != null) {
				if (node.getNodeType() == Node.ELEMENT_NODE && tag.equals(node.getNodeName())) {
					break;
				}
				node = node.getNextSibling();
			}

			if (node != null) {
				curr = (Element) node;
			} else {
				return null;
			}
		}

		return curr;
	}

	/**
	 * csc stands for Convert Special Character. Change &, <, ", ' into XML
	 * acceptable. Because it could be used frequently, it is short-named to 'csc'.
	 * Usually when a string is used for XML values, the string should be parsed
	 * first.
	 * 
	 * @param str the String to convert.
	 * @return converted String with & to &amp;amp;, < to &amp;lt;, " to &amp;quot;,
	 *         ' to &amp;apos;
	 */
	public static String csc(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}

		StringBuffer buf = new StringBuffer(str);
		int i = 0;
		char c;

		while (i < buf.length()) {
			c = buf.charAt(i);
			if (c == '&') {
				buf.replace(i, i + 1, "&amp;");
				i += INT_FIVE;
			} else if (c == '<') {
				buf.replace(i, i + 1, "&lt;");
				i += INT_FOUR;
			} else if (c == '"') {
				buf.replace(i, i + 1, "&quot;");
				i += INT_SIX;
			} else if (c == '\'') {
				buf.replace(i, i + 1, "&apos;");
				i += INT_SIX;
			} else if (c == '>') {
				buf.replace(i, i + 1, "&gt;");
				i += INT_FOUR;
			} else {
				i++;
			}
		}

		return buf.toString();
	}

	/**
	 * For an Element node, return its Text node's value; otherwise return the
	 * node's value.
	 * 
	 * @param node Node
	 * @return String
	 * @return
	 */
	public static String getNodeValue(Node node) {
		String retval = null;
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Node child = node.getFirstChild();
			while (child != null) {
				if (child.getNodeType() == Node.TEXT_NODE) {
					return child.getNodeValue();
				}
				child = child.getNextSibling();
			}
		} else {
			retval = node.getNodeValue();
		}

		return retval;
	}

	/**
	 * For an Element node, set its Text node's value (create one if it does not
	 * have); otherwise set the node's value.
	 * 
	 * @param node node
	 * @param val  value
	 */
	public static void setNodeValue(Node node, String val) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Node child = node.getFirstChild();
			while (child != null) {
				if (child.getNodeType() == Node.TEXT_NODE) {
					break;
				}
				child = child.getNextSibling();
			}
			if (child == null) {
				child = node.getOwnerDocument().createTextNode(val);
				node.appendChild(child);
			} else {
				child.setNodeValue(val);
			}
		} else {
			node.setNodeValue(val);
		}
	}

	/**
	 * Creates an element with the text node value supplied
	 * 
	 * @param doc         the XML document on which this text node element has to be
	 *                    created.
	 * @param elementName the name of the element to be created
	 * @param attributes  usually a Hashtable containing name/value pairs for the
	 *                    attributes of the element.
	 * @param textValue   the value for the text node of the element.
	 * @return Element
	 * 
	 */
	public static Element createTextElement(Document doc, String elementName, String textValue) {
		Element elem = doc.createElement(elementName);
		elem.appendChild(doc.createTextNode(textValue));

		return elem;
	}

	/**
	 * Creates an element with the text node value supplied
	 * 
	 * @param doc           the XML document on which this text node element has to
	 *                      be created.
	 * @param parentElement the parent element on which this text node element has
	 *                      to be appended
	 * @param elementName   the name of the element to be created
	 * @param attributes    usually a Hashtable containing name/value pairs for the
	 *                      attributes of the element.
	 * @param textValue     the value for the text node of the element.
	 * @return Element
	 */
	public static Element appendTextChild(Document doc, Element parentElement, String elementName, String textValue) {
		Element elem = doc.createElement(elementName);
		elem.appendChild(doc.createTextNode(textValue));

		parentElement.appendChild(elem);
		return elem;
	}

	/**
	 * Create an element with either attributes or text node.
	 * 
	 * @param doc            the XML document on which the node has to be created
	 * @param elementName    the name of the element to be created
	 * @param hashAttributes the value for the text node or the attributes for the
	 *                       node element
	 * @param textNodeFlag   a flag signifying whether te node to be created is the
	 *                       text node
	 * @return Element
	 */
	public static Element createElement(Document doc, String elementName, Map<String, String> hashAttributes) {
		Element elem = doc.createElement(elementName);
		if (hashAttributes != null) {
			for (String key : hashAttributes.keySet()) {
				elem.setAttribute(key, hashAttributes.get(key));
			}

		}
		return elem;
	}

	/**
	 * This method is for adding child Nodes to parent node element.
	 * 
	 * @param parentElement Parent Element under which the new Element should be
	 *                      present
	 * @param childElement  Child Element which should be added.
	 */
	public static void appendChild(Element parentElement, Element childElement) {
		parentElement.appendChild(childElement);
	}

	/**
	 * This method is for setting the attribute of an element
	 * 
	 * @param objElement     Element where this attribute should be set
	 * @param attributeName  Name of the attribute
	 * @param attributeValue Value of the attribute
	 */
	public static void setAttribute(Element objElement, String attributeName, String attributeValue) {
		objElement.setAttribute(attributeName, attributeValue);
	}

	/**
	 * This method is for removing an attribute from an Element.
	 * 
	 * @param objElement    Element from where the attribute should be removed.
	 * @param attributeName Name of the attribute
	 */
	public static void removeAttribute(Element objElement, String attributeName) {
		objElement.removeAttribute(attributeName);
	}

	/**
	 * This method is for removing the child element of an element
	 * 
	 * @param parentElement Element from where the child element should be removed.
	 * @param childElement  Child Element which needs to be removed from the parent
	 */
	public static void removeChild(Element parentElement, Element childElement) {
		parentElement.removeChild(childElement);
	}

	/**
	 * Method to create a text mode for an element
	 * 
	 * @param doc           the XML document on which the node has to be created
	 * @param parentElement the element for which the text node has to be created.
	 * @param elementValue  the value for the text node.
	 */
	public static void createTextNode(Document doc, Element parentElement, String elementValue) {
		parentElement.appendChild(doc.createTextNode(elementValue));
	}

	/**
	 * 
	 * This method takes a document Element as input and returns the XML String.
	 * 
	 * @param element a valid element object for which XML output in String form is
	 *                required.
	 * @return XML String of the given element
	 */

	public static String getElementXMLString(Element element) {
		if (isVoid(element)) {
			return null;
		} else {
			return XMLUtil.serialize(element);
		}
	}

	/**
	 * Convert the Document to String and write to a file.
	 * 
	 * @param document document to be converted to string
	 * @param fileName name of the file where the contents will be written
	 * @throws IOException IO excpetion which can be throws as a part of file
	 *                     operation
	 */
	public static void flushToAFile(Document document, String fileName) throws IOException {
		if (document != null) {
			OutputFormat oFmt = new OutputFormat(document, "iso-8859-1", true);
			oFmt.setPreserveSpace(true);
			XMLSerializer xmlOP = new XMLSerializer(oFmt);
			FileWriter out = new FileWriter(new File(fileName));
			xmlOP.setOutputCharStream(out);
			xmlOP.serialize(document);
			out.close();
		}
	}

	/**
	 * Serialize a Document to String and output to a java.io.Writer.
	 * 
	 * @param document Document to be converted to string
	 * @param writer   Writer object for serializing the file
	 * @throws IOException IO exception to be thrown as a part of file I/O
	 */
	public static void flushToAFile(Document document, Writer writer) throws IOException {
		if (document != null) {
			OutputFormat oFmt = new OutputFormat(document, "iso-8859-1", true);
			oFmt.setPreserveSpace(true);
			XMLSerializer xmlOP = new XMLSerializer(oFmt);
			xmlOP.setOutputCharStream(writer);
			xmlOP.serialize(document);
			writer.close();
		}
	}

	/**
	 * This method constructs and inserts a process Instruction in the given
	 * document
	 * 
	 * @param doc         document for processing
	 * @param rootElement root element in the XML document
	 * @param strTarget   target
	 * @param strData     instructions
	 */
	public static void createProcessingInstruction(Document doc, Element rootElement, String strTarget,
			String strData) {
		ProcessingInstruction p = doc.createProcessingInstruction(strTarget, strData);
		doc.insertBefore(p, rootElement);
	}

	/**
	 * 
	 * @param element       element
	 * @param attributeName attribute name
	 * @return the value of the attribute in the element.
	 */
	public static String getAttribute(Element element, String attributeName) {
		if (element != null) {
			return element.getAttribute(attributeName);
		} else {
			return null;
		}
	}

	/**
	 * Get the first direct child Element with the name.
	 * 
	 * @deprecated use getFirstElementByName() instead.
	 * @param element  element
	 * @param nodeName nodeName
	 * @return Element type
	 */
	@Deprecated
	public static Element getUniqueSubNode(Element element, String nodeName) {
		Element uniqueElem = null;
		NodeList nodeList = element.getElementsByTagName(nodeName);
		if (nodeList != null && nodeList.getLength() > 0) {
			int size = nodeList.getLength();
			for (int count = 0; count < size; count++) {
				uniqueElem = (Element) (nodeList.item(count));
				if (uniqueElem != null) {
					if (uniqueElem.getParentNode() == element) {
						break;
					}
				}
			}
		}
		return uniqueElem;
	}

	/**
	 * Gets the node value for a sub element under a Element with unique name.
	 * 
	 * @deprecated the logic is not clear as the implementation gets the value of
	 *             grand-child instead of direct child. should use
	 *             getFirstElementByName() and getNodeValue() combination for
	 *             application logic.
	 * @param element  element
	 * @param nodeName nodeName
	 * @return String type
	 */
	@Deprecated
	public static String getUniqueSubNodeValue(Element element, String nodeName) {
		NodeList nodeList = element.getElementsByTagName(nodeName);
		String retval = null;
		if (nodeList != null) {
			Element uniqueElem = (Element) (nodeList.item(0));
			if (uniqueElem != null) {
				if (uniqueElem.getFirstChild() != null) {
					return uniqueElem.getFirstChild().getNodeValue();
				} else {
					retval = null;
				}
			} else {
				retval = null;
			}
		} else {
			retval = null;
		}

		return retval;
	}

	/**
	 * Return the sub elements with given name, as a List.
	 * 
	 * @param element  element
	 * @param nodeName nodeName
	 * @return List
	 */
	public static List<Element> getSubNodeList(Element element, String nodeName) {
		NodeList nodeList = element.getElementsByTagName(nodeName);
		List<Element> elemList = new ArrayList<Element>();
		for (int count = 0; count < nodeList.getLength(); count++) {
			elemList.add((Element) nodeList.item(count));
		}
		return elemList;
	}

	/**
	 * Same as getSubNodeList().
	 * 
	 * @see #getSubNodeList(Element, String).
	 * @param startElement startElement
	 * @param elemName     element Name
	 * @return List
	 */
	public static List<Element> getElementsByTagName(Element startElement, String elemName) {
		NodeList nodeList = startElement.getElementsByTagName(elemName);
		List<Element> elemList = new ArrayList<Element>();
		for (int count = 0; count < nodeList.getLength(); count++) {
			elemList.add((Element) nodeList.item(count));
		}
		return elemList;
	}

	/**
	 * Gets the count of sub nodes under one node matching the sub node name
	 * 
	 * @param parentElement  Element under which sub nodes reside
	 * @param subElementName Name of the sub node to look for in the parent node
	 * @return integer type
	 */
	public static int getElementsCountByTagName(Element parentElement, String subElementName) {
		NodeList nodeList = parentElement.getElementsByTagName(subElementName);
		if (nodeList != null) {
			return nodeList.getLength();
		} else {
			return 0;
		}
	}

	/**
	 * Removes the passed Node name from the input document. If no name is passed,
	 * it removes all the nodes.
	 * 
	 * @param node     Node from which we have to remove the child nodes
	 * @param nodeType nodeType e.g. Element Node, Comment Node or Text Node
	 * @param name     Name of the Child node to be removed
	 */
	public static void removeAll(Node node, int nodeType, String name) {
		if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) {
			node.getParentNode().removeChild(node);
		} else {
			// Visit the children
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				XMLUtil.removeAll(list.item(i), nodeType, name);
			}
		}

	}

	/**
	 * This method removes the elements that match the xpath passed as input
	 * 
	 * @param parentElement Parent Element from which child elements have to be
	 *                      removed
	 * @param xpath         XPath of the Element which need to be removed
	 * @return Modified parent element
	 */
	// public static Element removeElements(Element parentElement, String xpath)
	// {
	// NodeList list = (NodeList) YRCXPathUtils.evaluate(parentElement, xpath,
	// XPathConstants.NODESET);
	//
	// for (int i = 0; i < list.getLength(); i++) {
	// Node node = list.item(i);
	// node.getParentNode().removeChild(node);
	// }
	// return parentElement;
	// }

	/**
	 * Imports an element including the subtree from another document under the
	 * parent element. Returns the newly created child element. This method returns
	 * null if either parent or element to be imported is null.
	 * 
	 * @param parentEle      parentEle
	 * @param ele2beImported ele2beImported
	 * @return Element
	 */
	public static Element importElement(Element parentEle, Element ele2beImported) {
		Element child = null;
		if (parentEle != null && ele2beImported != null) {
			child = (Element) parentEle.getOwnerDocument().importNode(ele2beImported, true);
			parentEle.appendChild(child);
		}
		return child;
	}

	/**
	 * Imports an element including the subtree from another document under the
	 * parent element. Returns the newly created child element. This method returns
	 * null if either parentDoc or element to be imported is null.
	 * 
	 * @param parentDoc      parentDoc
	 * @param ele2beImported ele2beImported
	 * @return Element
	 */
	public static Element importElement(Document parentDoc, Element ele2beImported) {
		Element child = null;
		if (parentDoc != null && ele2beImported != null) {
			child = (Element) parentDoc.importNode(ele2beImported, true);
			parentDoc.appendChild(child);
		}
		return child;
	}

	/**
	 * Utility method to check if a given string is null or empty (length is zero
	 * after trim call).
	 * <p>
	 * </p>
	 * 
	 * @param inStr String for void check.
	 * @return true if the given string is void.
	 */
	// public static boolean isVoid(String inStr) {
	// return (inStr == null) ? true : (inStr.trim().length() == 0) ? true :
	// false;
	// }

	/**
	 * Utility method to check if a given object is void (just null check).
	 * <p>
	 * </p>
	 * 
	 * @param obj Object for void check.
	 * @return true if the given object is null.
	 *         <p>
	 *         </p>
	 */
	public static boolean isVoid(Object obj) {
		// return (obj == null) ? true : false;
		boolean retVal = false;
		if (obj == null) {
			retVal = true;

		}
		return retVal;
	}

	/**
	 * Gets the child element with the given name. If not found returns null. This
	 * method returns null if either parent is null or child name is void.
	 * 
	 * @param parentEle parentEle
	 * @param childName childName
	 * @return Element
	 */
	public static Element getChildElement(Element parentEle, String childName) {
		return XMLUtil.getChildElement(parentEle, childName, false);
	}

	/**
	 * Gets the child element with the given name. If not found: 1) a new element
	 * will be created if "createIfNotExists" is true. OR 2) null will be returned
	 * if "createIfNotExists" is false. This method returns null if either parent is
	 * null or child name is void.
	 * 
	 * @param parentEle         parentEle
	 * @param childName         childName
	 * @param createIfNotExists createIfNotExists flag
	 * @return Element
	 */
	public static Element getChildElement(Element parentEle, String childName, boolean createIfNotExists) {

		Element child = null;
		if (parentEle != null && !XMLUtil.isVoid(childName)) {
			for (Node n = parentEle.getFirstChild(); n != null; n = n.getNextSibling()) {
				if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(childName)) {
					return (Element) n;
				}
			}

			// Did not find the element, create it if createIfNotExists is true
			// else return null;
			if (createIfNotExists) {
				child = XMLUtil.createChild(parentEle, childName);

			}
		}
		return child;
	}

	/**
	 * Creates a child element under the parent element with given child name.
	 * Returns the newly created child element. This method returns null if either
	 * parent is null or child name is void.
	 * 
	 * @param parentEle parentElement
	 * @param childName childName
	 * @return Element
	 */
	public static Element createChild(Element parentEle, String childName) {
		Element child = null;
		if (parentEle != null && !XMLUtil.isVoid(childName)) {
			child = parentEle.getOwnerDocument().createElement(childName);
			parentEle.appendChild(child);
		}
		return child;
	}

	/**
	 * Get the iterator for all children of Element type.
	 * 
	 * @param ele Element
	 * @return Iterator
	 */
	public static Iterator<Element> getChildren(Element ele) {
		List<Element> list = new ArrayList<Element>();
		if (ele != null && ele.hasChildNodes()) {
			NodeList childList = ele.getChildNodes();
			for (int i = 0; i < childList.getLength(); i++) {
				if (childList.item(i) instanceof Element) {
					list.add((Element) childList.item(i));
				}
			}
		}
		return list.iterator();
	}

	/**
	 * Get the attribute value as double. Returns 0 if attribute value is void or if
	 * the attribute does not exist.
	 * 
	 * @param ele      element
	 * @param attrName attribute Name
	 * @return double
	 */
	public static double getDoubleAttribute(Element ele, String attrName) {
		String val = XMLUtil.getAttribute(ele, attrName);
		if (XMLUtil.isVoid(val) || "" == val) {
			return 0.0;
		} else {
			return Double.parseDouble(val);
		}
	}

	/**
	 * This method will copy all the attribute from one node to other node.
	 * 
	 * @param toEle   toEle
	 * @param fromEle fromEle
	 * 
	 */
	public static void copyAttributes(Element toEle, Element fromEle) {
		NamedNodeMap fromAttrbMap = fromEle.getAttributes();

		if (fromAttrbMap != null) {
			int fromAttrbMapLength = fromAttrbMap.getLength();

			for (int i = 0; i < fromAttrbMapLength; i++) {
				Node attrbNode = fromAttrbMap.item(i);

				if ((attrbNode == null) || (attrbNode.getNodeType() != Node.ATTRIBUTE_NODE)) {
					continue;
				}

				String attrbName = attrbNode.getNodeName();
				String attrbVal = attrbNode.getNodeValue();

				String toAttrbVal = XMLUtil.getAttribute(toEle, attrbName);

				if (toAttrbVal.length() == 0) {
					XMLUtil.setAttribute(toEle, attrbName, attrbVal);
				}
			}
		}
	}

	/**
	 * Creates a Document object
	 * 
	 * @return empty Document object
	 * @throws ParserConfigurationException when XML parser is not properly
	 *                                      configured.
	 */
	public static Document getDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	public static void mergeElement(Element fromElement, Element toElement, boolean checkHasAttributeInTarget) {
		if (fromElement == null) {
			return;
		}
		mergeAttributes(fromElement, toElement, checkHasAttributeInTarget);
		for (Iterator<Element> i = getChildren(fromElement); i.hasNext();) {
			Element schild = i.next();
			Element dchild = getChildElement(toElement, schild.getTagName());
			if (dchild != null) {
				mergeElement(schild, dchild, checkHasAttributeInTarget);
			} else {
				importElement(toElement, schild);
			}
		}

	}

	public static void mergeAttributes(Element fromEle, Element toEle, boolean checkHasAttributeInTarget) {
		if (fromEle != null && toEle != null) {
			NamedNodeMap map = fromEle.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				if (checkHasAttributeInTarget) {
					boolean hasAttribute = toEle.hasAttribute(map.item(i).getNodeName());
					if (!hasAttribute) {
						toEle.setAttribute(map.item(i).getNodeName(), map.item(i).getNodeValue());
					}
				} else {
					toEle.setAttribute(map.item(i).getNodeName(), map.item(i).getNodeValue());
				}
			}

		}
	}

	/**
	 * This method accepts an Element as a parameter and returns a document with
	 * this 'element name' as its root element.
	 * 
	 * @Author - Zameer
	 * @param eleRootElement
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document getDocumentFromElement(Element eleRootElement) throws ParserConfigurationException {

		Document docFromElement = getDocument();
		Node nodeRoot = docFromElement.importNode(eleRootElement, true);
		docFromElement.appendChild(nodeRoot);
		return docFromElement;

	}

	public static InputStream getInputStreamFromDocument(Document inDoc) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = new DOMSource(inDoc);
		Result outputTarget = new StreamResult(outputStream);
		try {
			TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
		} catch (TransformerException ex) {
			logger.error("Error detected {}", ex.getMessage(), ex);

			throw new RuntimeException("Error getting output Stream", ex);
		}
		InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
		return is;
	}
}
