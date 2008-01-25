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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.SequenceInputStream;

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
 * <p>
 * This class is responsible for reading a simulation scenario from an XML file
 * which have to comply to a given XML schema represented with an .xsd file.
 * </p>
 * <p>
 * Before parsing the XML file this class checks its validity against its schema
 * </p>
 * <p>
 * The organization of this class is quite simple:
 * <ul>
 *  <li>It is initialized with the path to an XSD and an XML file through
 * its constructor public XMLReader(String xsd, String xml)
 *  </li>
 *  <li> It exposes public method ScenarioType getScenario() which extracts 
 * from the XML input file an instance of ScenarioType class
 *  </li>
 * </ul>
 * </p>
 * 
 * @author Giovanni Novelli
 * @see net.sf.gap.xml.types.ScenarioType
 *
 */public class XMLReader {
    private String _xsd;
    private String _xml;
    
    public XMLReader(String xsd, String xml) {
        this.set_xsd(xsd);
        this.set_xml(xml);
    }
    
    public static void main(String[] args) {
       String xsd = "xml/schema.xsd";
       //String xml = "xml/flatgrid";
       String xml = "xml/manyvos.xml";
       
       XMLReader reader = new XMLReader(xsd, xml);
 
       ScenarioType scenario = reader.getScenario();
       System.out.println("Scenario's name: " + scenario.getName());
    }
    
    /**
     * Extracts from the XML input file an instance of ScenarioType class
     * 
     * @return scenario from current XML input file
     */
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
    
    private Document getDocument() {
        Document document = null;
        try {
            // define the type of schema - we use W3C:
            String schemaLang = "http://www.w3.org/2001/XMLSchema";
            
            SchemaFactory factory =
                    SchemaFactory.newInstance(schemaLang);
            Schema schema = factory.newSchema(new StreamSource(this.get_xsd()));
            Validator validator = schema.newValidator();

            if (this.get_xml().endsWith(".xml")) {
                validator.validate(new StreamSource(this.get_xml()));
                // Parse the XML as a W3C document.
                DocumentBuilder builder =
                        DocumentBuilderFactory.newInstance().newDocumentBuilder();
                document = builder.parse(new File(this.get_xml()));
                boolean valid = (document!=null);
                if (!valid) {
                  System.err.println(this.get_xml() + " is NOT valid against schema " + this.get_xsd());
                }
            } else { //@TODO Fixit
                InputStream win = new CompositeInputStream(this.get_xml());
                win.read();
                DocumentBuilder builder =
                        DocumentBuilderFactory.newInstance().newDocumentBuilder();
                document = builder.parse(win);
                boolean valid = (document!=null);
                if (!valid) {
                  System.err.println("NOT valid against schema " + this.get_xsd());
                }
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

    private String get_xsd() {
        return _xsd;
    }

    private void set_xsd(String xsd) {
        this._xsd = xsd;
    }

    private String get_xml() {
        return _xml;
    }

    private void set_xml(String xml) {
        this._xml = xml;
    }
}

class CompositeInputStream extends InputStream
{
    private final InputStream[] in;
    private final InputStream inProlog;
    private final InputStream inEpilog;
    public CompositeInputStream(String path)
    {
        this.inProlog = new ByteArrayInputStream(
            "<scenario  name=\"flatgrid\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"schema.xsd\" xsi:type=\"SimulationScenarioType\">".getBytes());
        this.inEpilog = new ByteArrayInputStream("</scenario>".getBytes());
        this.in = new InputStream[2];
        try {
        this.in[0] = new FileInputStream(path+"/topology.xml");
        this.in[1] = new FileInputStream(path+"/grid.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int read() throws IOException
    {
        int result = this.inProlog.read();
        if (result == -1)
            result = this.in[0].read();
        if (result == -1)
            result = this.in[1].read();
        if (result == -1)
            result = this.inEpilog.read();
        return result;
    }
}
