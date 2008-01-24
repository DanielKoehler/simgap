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

package net.sf.gap.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

import net.sf.gap.xml.types.*;
import net.sf.gap.xml.parsing.impl.GridParser;
import net.sf.gap.xml.parsing.impl.VOSParser;
import net.sf.gap.xml.parsing.impl.TopologyParser;

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
       String xml = "xml/flatgrid.xml";
       
       XMLReader reader = new XMLReader(xsd, xml);
 
       ScenarioType scenario = reader.getScenario();
       System.out.println("Scenario's name: " + scenario.getName());
    }
    
    public ScenarioType getScenario() {
        Document document = this.getDocument();
 
        Element scenarioElement = (Element) this.getDocument().getElementsByTagName("scenario").item(0);
        String scenarioName = scenarioElement.getAttribute("name");
        ScenarioType scenario = new ScenarioType(scenarioName);
        
        TopologyParser topologyParser = new TopologyParser(document);
        NetworkTopologyType topology = topologyParser.getTopology();

        scenario.setTopology(topology);
        
        GridParser gridParser = new GridParser(document);
        GridType grid = gridParser.getGrid();
        scenario.setGrid(grid);
            
        VOSParser vosParser = new VOSParser(document);
        VOSType vos = vosParser.getVOS(grid);
        scenario.setVos(vos);

        return scenario;
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
            document = builder.parse(new File(this.get_xml()));
            boolean valid = (document!=null);
            if (!valid) {
              System.err.println(this.get_xml() + " is NOT valid against schema " + this.get_xsd());
            }
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
