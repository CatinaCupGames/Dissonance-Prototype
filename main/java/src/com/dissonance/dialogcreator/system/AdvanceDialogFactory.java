package com.dissonance.dialogcreator.system;

import com.dissonance.framework.game.scene.dialog.DialogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class AdvanceDialogFactory {

    public static void createDialogFromId(String id, String path) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new File(path));

        NodeList dialogs = document.getElementsByTagName("dialog");

        for (int i = 0; i < dialogs.getLength(); i++) {
            Node node = dialogs.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getAttribute("dialog_id").equals(id)) {
                    TransformerFactory transFactory = TransformerFactory.newInstance();
                    Transformer transformer = transFactory.newTransformer();
                    StringWriter buffer = new StringWriter();
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                    transformer.transform(new DOMSource(element), new StreamResult(buffer));
                    String dialogXML = "<game_dialog>" + buffer.toString() + "</game_dialog>";
                    createDialogFromXMLString(dialogXML);
                }
            }
        }
    }

    private static boolean createDialogFromXMLString(String xml) throws UnsupportedEncodingException {
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        return DialogFactory.loadDialog(stream);
    }
}
