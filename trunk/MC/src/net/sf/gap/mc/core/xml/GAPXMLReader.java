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

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.transform.stream.StreamSource;
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
        this.set_xsd(xsd);
        this.set_xml(xml);
    }
    
    public static void main(String[] args) {
       String xsd = "xml/schema.xsd";
       String xml = "xml/data.xml";
       GAPXMLReader reader = new GAPXMLReader(xsd, xml);
       System.out.println(xsd + " " + xml);
       boolean valid = reader.validate();
       if (valid) {
        System.out.println(reader.get_xml() + " is valid for schema " + reader.get_xsd());
       }
    }
    
    public boolean validate() {
        boolean valid = false;
        try {
            // define the type of schema - we use W3C:
            String schemaLang = "http://www.w3.org/2001/XMLSchema";
            
            SchemaFactory factory =
                    SchemaFactory.newInstance(schemaLang);
            Schema schema = factory.newSchema(new StreamSource(this.get_xsd()));
            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(this.get_xml()));
            valid=true;
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
