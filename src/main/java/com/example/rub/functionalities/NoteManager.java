package com.example.rub.functionalities;

import com.example.rub.enums.LogType;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.comparator.InteressamentoComp;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.UUID;

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

    public void addCallNote (Document doc, String note, int durata, boolean messaggio, InteressamentoStatus oldValue, InteressamentoStatus newValue){
        Element call = doc.createElement("chiamata");
        Calendar now = Calendar.getInstance();
        String m = ""+(now.get(Calendar.MONTH)+1);
        if (m.length() == 1)    m = "0"+m;
        String d = ""+now.get(Calendar.DAY_OF_MONTH);
        if (d.length() == 1)    d = "0"+d;
        call.setAttribute("data",now.get(Calendar.YEAR) + "-" + m + "-" + d );
        call.setAttribute("operatore", GlobalContext.operator.name());
        call.setAttribute("durata", ""+durata);
        call.setAttribute("number", "" + (getNoteNumber(doc)+1));
        call.setAttribute("cancelled", "false");
        call.setAttribute("messaggio", "" + messaggio);
        call.setTextContent(note);
        call.setAttribute("previousInterest", oldValue.name());
        call.setAttribute("newInterest", newValue.name());
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
                    e.setAttribute("newInterest", feedback);
                }
            }
        }
    }

    public boolean deleteNote(Document doc, String noteNumber){
        boolean ret = false;
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("chiamata");
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                if (e.getAttribute("number").equals(noteNumber)){
                    if(e.getAttribute("cancelled").equals("false")){
                        e.setAttribute("cancelled", "true");
                        ret = true;
                    }
                }
            }
        }
        return ret;
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

    public int getNoteNumber(Document document){        //Se la nota viene cancellata dal file xml il noteNumber diventa inaffidabile e quindi il modify note modifica le note sbagliate
        int n = 0;
        try {
            NodeList nodeList = document.getElementsByTagName("chiamata");
            n = nodeList.getLength();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        }
        return n;
    }

    public LinkedList<String> getAnnotationDates (UUID noteId, int durata, Operatori operator, boolean includeMessages){
        LinkedList<String> ret = new LinkedList<>();
        try {
            Document doc = readXml(""+noteId);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("chiamata");
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node node = nodeList.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    if ((Integer.parseInt(e.getAttribute("durata")) >= durata || includeMessages) &&
                            e.getAttribute("operatore").equals(operator.name()) &&
                            ((e.getAttribute("messaggio").equals("false")) || includeMessages) &&
                            e.getAttribute("cancelled").equals("false")) {
                        String t = e.getAttribute("data");
                        if (new InteressamentoComp().compare(InteressamentoStatus.valueOf(e.getAttribute("previousInterest")), InteressamentoStatus.valueOf(e.getAttribute("newInterest"))) < 0) {
                            switch (InteressamentoStatus.valueOf(e.getAttribute("newInterest"))) {
                                case LISTINO:
                                    t = t + "A";
                                    break;
                                case CAMPIONE:
                                    t = t + "B";
                                    break;
                                case CLIENTE:
                                    t = t + "C";
                                    break;
                                default:
                                    t = t + "X";
                            }
                        } else {
                            t = t + "X";
                        }
                        ret.add(t);
                    }
                }
            }
        } catch (FileNotFoundException ignored)   {}
        catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        }
        return ret;
    }
}
