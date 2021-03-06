/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.xml;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A record reader that reads xml records from an xml file.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class XmlRecordReader implements RecordReader {

    private static final Logger LOGGER = Logger.getLogger(XmlRecordReader.class.getSimpleName());

    /**
     * The root element name.
     */
    private String rootElementName;

    /**
     * The xml input file.
     */
    private File xmlFile;

    /**
     * The xml reader.
     */
    private XMLEventReader xmlEventReader;

    /**
     * The current record number.
     */
    private int currentRecordNumber;

    public XmlRecordReader(final String rootElementName, final File xmlFile) {
        this.rootElementName = rootElementName;
        this.xmlFile = xmlFile;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(xmlFile));
    }

    @Override
    public boolean hasNextRecord() {
        try {
            while (!nextTagIsRootElementStart()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent instanceof EndDocument) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during checking the existence of next xml record", e);
            return false;
        }
    }

    @Override
    public Record readNextRecord() throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        while (!nextTagIsRootElementEnd()) {
            stringBuilder.append(xmlEventReader.nextEvent().toString());
        }
        //append root element end tag
        stringBuilder.append(xmlEventReader.nextEvent().toString());

        return new XmlRecord(++currentRecordNumber, stringBuilder.toString());
    }

    @Override
    public Integer getTotalRecords() {
        int totalRecords = 0;
        XMLEventReader totalRecordsXmlEventReader = null;
        try {
            totalRecordsXmlEventReader =
                    XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(xmlFile));
            XMLEvent event;
            while (totalRecordsXmlEventReader.hasNext()) {
                event = totalRecordsXmlEventReader.nextEvent();
                if (event.isStartElement() && event.asStartElement().getName().toString().equals(rootElementName)) {
                    totalRecords++;
                }
            }
        } catch (XMLStreamException e) {
            LOGGER.log(Level.SEVERE, "Unable to read data from xml file " + xmlFile, e);
            return null;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found " + xmlFile, e);
            return null;
        } finally {
            if (totalRecordsXmlEventReader != null) {
                try {
                    totalRecordsXmlEventReader.close();
                } catch (XMLStreamException e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred during closing xml total record reader", e);
                }
            }
        }
        return totalRecords;
    }

    @Override
    public String getDataSourceName() {
        return xmlFile.getAbsolutePath();
    }

    @Override
    public void close() throws Exception {
        xmlEventReader.close();
    }

    /**
     * Utility method to check if the next tag matches a start tag of the root element.
     * @return true if the next tag matches a start element of the root element, false else
     * @throws Exception thrown if no able to peek the next xml element
     */
    private boolean nextTagIsRootElementStart() throws Exception {
        return xmlEventReader.peek().isStartElement() &&
               xmlEventReader.peek().asStartElement().getName().toString().equalsIgnoreCase(rootElementName);
    }

    /**
     * Utility method to check if the next tag matches an end tag of the root element.
     * @return true if the next tag matches an end tag of the root element, false else
     * @throws Exception thrown if no able to peek the next xml element
     */
    private boolean nextTagIsRootElementEnd() throws Exception {
        return xmlEventReader.peek().isEndElement() &&
                xmlEventReader.peek().asEndElement().getName().toString().equalsIgnoreCase(rootElementName);
    }

}
