/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GAPXMLReader.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;

/**
 *
 * @author Giovanni Novelli
 */
public class GAPXMLReader {
    private String _xsd;
    private String _xml;
    
    public GAPXMLReader(String xsd, String xml) {
        this.set_xsd(_xsd);
        this.set_xml(_xml);
    }
    
    public static void main(String[] args) {
       GAPXMLReader reader = new GAPXMLReader("xml/schema.xsd","xml/data.xml");
       boolean valid = reader.validate();
       if (valid) {
        System.out.println(reader.get_xml() + " is valid for schema " + reader.get_xsd());
       }
    }
    
    public boolean validate() {
        boolean valid = false;
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(this.get_xsd()));
            Validator validator = schema.newValidator();

            // Parse the _xml as a W3C document.
            DocumentBuilder builder =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(this.get_xml()));
            validator.validate(new DOMSource(document));
            valid=true;
        } catch (ParserConfigurationException e) {
            System.err.println("ParserConfigurationException caught...");
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("SAXException caught...");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException caught...");
            e.printStackTrace();
        }
        return valid;
    }

    public String get_xsd() {
        return _xsd;
    }

    public void set_xsd(String xsd) {
        this._xsd = xsd;
    }

    public String get_xml() {
        return _xml;
    }

    public void set_xml(String xml) {
        this._xml = xml;
    }
}
