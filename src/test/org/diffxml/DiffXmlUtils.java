package org.diffxml;

import org.diffxml.diffxml.DOMOps;
import org.diffxml.diffxml.fmes.ParserInitialisationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DiffXmlUtils {
    protected final XPath xpath = XPathFactory.newInstance().newXPath();

    protected Document getDocument(File fileA) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DOMOps.initParser(fac);
        return fac.newDocumentBuilder().parse(fileA);
    }

    protected String getString(Document document) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            DOMOps.outputXML(document, os, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toString();
    }
    public File getFile(String fileName) {
        return new File(getClass().getResource(fileName).getFile());
    }

    public static class CommonContext implements NamespaceContext {

        public String getNamespaceURI(String prefix) {
            if("common".equals(prefix)) {
                return "www.cengage.com/SOA/common";
            }
            if("activity".equals(prefix)) {
                return "www.cengage.com/SOA/activity";
            }
            return null;
        }

        public String getPrefix(String namespaceURI) {
            return null;
        }

        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }

    }
}
