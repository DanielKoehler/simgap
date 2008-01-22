/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * XMLReader.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.mc.core.xml;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 *
 * @author Giovanni Novelli
 */
public class XMLReader {
    private String _xsd;
    private String _xml;
    
    public XMLReader(String xsd, String xml) {
        this.set_xsd(xsd);
        this.set_xml(xml);
    }
    
    public static void main(String[] args) {
       String xsd = "xml/schema.xsd";
       String xml = "xml/data.xml";
       XMLReader reader = new XMLReader(xsd, xml);
       System.out.println(xsd + " " + xml);
       Document document = reader.getDocument();
       boolean valid = (document!=null);
       if (valid) {
        System.out.println(reader.get_xml() + " is valid for schema " + reader.get_xsd());
       }
    }
    
    public Document getDocument() {
        Document document = null;
        try {
            // define the type of schema - we use W3C:
            String schemaLang = "http://www.w3.org/2001/XMLSchema";
            
            SchemaFactory factory =
                    SchemaFactory.newInstance(schemaLang);
            Schema schema = factory.newSchema(new StreamSource(this.get_xsd()));
            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(this.get_xml()));
            // Parse the XML as a W3C document.
            DocumentBuilder builder =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(new File("xml/data.xml"));

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
        return document;
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
