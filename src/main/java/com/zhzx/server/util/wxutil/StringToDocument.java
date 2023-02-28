package com.zhzx.server.util.wxutil;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by 11345 on 2022/6/14.
 */
@Slf4j
public class StringToDocument {
    public static Document stringToDoc(String xmlStr) {
        //字符串转XML
        Document doc = null;
        try {
            xmlStr = new String(xmlStr.getBytes(),"UTF-8");
            StringReader sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);

        } catch (ParserConfigurationException e) {
            log.error(xmlStr);
            e.printStackTrace();
        } catch (SAXException e) {
            log.error(xmlStr);
            e.printStackTrace();
        } catch (IOException e) {
            log.error(xmlStr);
            e.printStackTrace();
        }
        return doc;
    }
}
