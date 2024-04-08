package com.example.rub.functionalities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class NoteManager {
    private final DocumentBuilder docBuilder;
    public NoteManager() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
    }

    public Document createDocument(String name){
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("companyNotes");
        rootElement.setAttribute("Name",name);
        doc.appendChild(rootElement);
        return doc;
    }

    public void addCallNote (Document doc, String note, String durata){
        Element call = doc.createElement("chiamata");
        Calendar now = Calendar.getInstance();
        call.setAttribute("data",now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH)+1) + "/" + now.get(Calendar.YEAR));
        call.setAttribute("operatore", GlobalContext.operator.name());
        call.setAttribute("durata", durata);
        call.setAttribute("number", "" + (getNoteNumber(doc)+1));
        call.setAttribute("cancelled", "false");
        call.setTextContent(note);
        Element root = doc.getDocumentElement();
        root.appendChild(call);
    }

    public void modifyNote(Document doc, String noteNumber, String text, String feedback, String durata){
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("chiamata");
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                if (e.getAttribute("number").equals(noteNumber)){
                    e.setTextContent(text);
                    e.setAttribute("durata", durata);
                    e.setAttribute("feedback", feedback); //TODO
                }
            }
        }
    }

    public void writeXml(Document doc, String output) throws TransformerException{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("bin\\Note\\" + output + ".xml"));
        transformer.transform(source, result);
    }
    public Document readXml(String input) throws IOException, SAXException {
        return docBuilder.parse("bin\\Note\\" + input + ".xml");
    }

    public int getNoteNumber(Document document){
        int n = 0;
        try {
            NodeList nodeList = document.getElementsByTagName("chiamata");
            n = nodeList.getLength();
            System.out.println(n);
        } catch (Exception ignored) {}
        return n;
    }
}
