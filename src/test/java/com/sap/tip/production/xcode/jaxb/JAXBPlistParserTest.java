package com.sap.tip.production.xcode.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sap.tip.production.xcode.Array;
import com.sap.tip.production.xcode.Dict;
import com.sap.tip.production.xcode.Plist;

public class JAXBPlistParserTest
{
  public static final String fileName = "src/test/resources/project.pbxproj.xml";
  public static final String fileNameOpenStep = "src/test/resources/project.pbxproj";

  @Test
  public void load() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    Plist plist = parser.load(fileName);
    assertNotNull(plist);
    assertEquals("1.0", plist.getVersion());
    Dict dict = plist.getDict();
    assertNotNull(dict);

    Dict objects = (Dict) dict.get("objects");
    Dict group = (Dict) objects.get("83FC273A15580501000A0343");
    assertEquals("PBXGroup", group.get("isa"));

    Array children = (Array) group.get("children");
    assertEquals(3, children.size());
    assertEquals("83FC274F15580501000A0343", children.get(0));
    assertEquals("83FC274815580501000A0343", children.get(1));
    assertEquals("83FC274615580501000A0343", children.get(2));
  }

  @Test
  public void convertOpenStepToXML() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    try
    {
      parser.load(fileNameOpenStep);
      Assert.fail();
    }
    catch (javax.xml.bind.UnmarshalException e)
    {
      if (!(e.getCause().getClass() == SAXParseException.class))
      {
        Assert.fail();
      }
    }

    File xmlProj = File.createTempFile("project", ".pbxproj");
    xmlProj.deleteOnExit();

    parser.convert(fileNameOpenStep, xmlProj);
    Plist plist = parser.load(xmlProj.getAbsolutePath());
    assertEquals("1.0", plist.getVersion());
  }

  @Test(expected = javax.xml.bind.UnmarshalException.class)
  public void loadWithUnexpectedElements() throws Exception
  {
    String xml = "<plist><dict><a></a></dict></plist>";
    InputSource source = new InputSource(new StringReader(xml));

    JAXBPlistParser parser = new JAXBPlistParser();
    parser.load(source);
  }

  @Test(expected = javax.xml.bind.UnmarshalException.class)
  public void loadWithUnexpectedDTD() throws Exception
  {
    String xml = "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.xyz.com/DTDs/PropertyList-1.0.dtd\">\n"
          +
          "<plist version=\"1.0\"></plist>";
    InputSource source = new InputSource(new StringReader(xml));

    JAXBPlistParser parser = new JAXBPlistParser();
    parser.load(source);
  }

  @Test
  public void save() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    Plist plist = parser.load(fileName);

    File output = File.createTempFile("proj", ".pbxproj");
    output.deleteOnExit();
    parser.save(plist, output.getAbsolutePath());

    assertXMLEqual(new File(fileName), output);
  }

  private void assertXMLEqual(File expected, File actual) throws Exception
  {
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    db.setEntityResolver(new EntityResolver() {
      @Override
      public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException
      {
        return new InputSource(new StringReader(""));
      }
    });

    Document doc1 = db.parse(expected);
    Document doc2 = db.parse(actual);
    XMLUnit.setIgnoreWhitespace(true);
    XMLUnit.setIgnoreComments(true);
    XMLAssert.assertXMLEqual(doc1, doc2);
  }
}
