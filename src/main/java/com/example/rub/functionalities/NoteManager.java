package com.example.rub.functionalities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.util.Calendar;

public class NoteManager {
    private final DocumentBuilder docBuilder;
    public NoteManager() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
    }

    public Document createDocument(){
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("companyNotes");
        doc.appendChild(rootElement);
        return doc;
    }

    public void addCallNote (Document doc, String note){
        Element call = doc.createElement("chiamata");
        Calendar now = Calendar.getInstance();
        call.setAttribute("data",now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH)+1) + "/" + now.get(Calendar.YEAR));
        call.setAttribute("operatore", GlobalContext.operator.name());
        call.setTextContent(note);
        Element root = doc.getDocumentElement();
        root.appendChild(call);
    }
    public void writeXml(Document doc, OutputStream output) throws TransformerException{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }
}
