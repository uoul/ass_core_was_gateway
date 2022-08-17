package ass.core;

import ass.core.businessobject.AlertMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class WasXmlInterpreter {
    public static AlertMessages parseXmlToAlertMgs(String xml) {
        AlertMessages msgs = AlertMessages.builder().build();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            document.getDocumentElement().normalize();
            NodeList orderList = document.getElementsByTagName("order");
            // iterate over all orders -> build them
            for (int i = 0; i < orderList.getLength(); i++) {
                AlertMessages.AlertMessage msg = AlertMessages.AlertMessage.builder().build();
                NodeList currentOrder = orderList.item(i).getChildNodes();
                for (int j = 0; j < currentOrder.getLength(); j++) {
                    if (currentOrder.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        String currentNodeVal = currentOrder.item(j).getTextContent();
                        switch (currentOrder.item(j).getNodeName()) {
                            case "key" -> msg.setKey(currentNodeVal);
                            case "origin" -> msg.setOrigin(AlertMessages.AlertMessage.Origin.builder()
                                    .tid(Integer.parseInt(currentOrder.item(j).getAttributes().getNamedItem("tid").getNodeValue()))
                                    .name(currentNodeVal)
                                    .build());
                            case "receive-tad" -> msg.setReceiveTad(currentNodeVal);
                            case "operation-id" -> msg.setOperationId(currentNodeVal);
                            case "level" -> msg.setLevel(Integer.parseInt(currentNodeVal));
                            case "name" -> msg.setName(currentNodeVal);
                            case "operation-name" -> msg.setOperationName(currentNodeVal);
                            case "caller" -> msg.setCaller(currentNodeVal);
                            case "location" -> msg.setLocation(currentNodeVal);
                            case "info" -> msg.setInfo(currentNodeVal);
                            case "program" -> msg.setProgram(currentNodeVal);
                            case "status" -> msg.setStatus(currentNodeVal);
                            case "watch-out-tad" -> msg.setWatchOutTad(currentNodeVal);
                            case "finished-tad" -> msg.setFinishedTad(currentNodeVal);
                            case "destination-list" -> {
                                List<AlertMessages.AlertMessage.Destination> destinations = new ArrayList<>();
                                for (int k = 0; k < currentOrder.item(j).getChildNodes().getLength(); k++) {
                                    if (currentOrder.item(j).getChildNodes().item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Node destinationNode = currentOrder.item(j).getChildNodes().item(k);
                                        destinations.add(AlertMessages.AlertMessage.Destination.builder()
                                                .index(Integer.parseInt(destinationNode.getAttributes().getNamedItem("index").getNodeValue()))
                                                .id(Integer.parseInt(destinationNode.getAttributes().getNamedItem("id").getNodeValue()))
                                                .name(destinationNode.getTextContent())
                                                .build());
                                    }
                                }
                                msg.setDestinations(destinations);
                            }
                            case "paging-destination-list" -> {
                                List<AlertMessages.AlertMessage.Destination> destinations = new ArrayList<>();
                                for (int k = 0; k < currentOrder.item(j).getChildNodes().getLength(); k++) {
                                    if (currentOrder.item(j).getChildNodes().item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        Node destinationNode = currentOrder.item(j).getChildNodes().item(k);
                                        destinations.add(AlertMessages.AlertMessage.Destination.builder()
                                                .index(Integer.parseInt(destinationNode.getAttributes().getNamedItem("index").getNodeValue()))
                                                .id(Integer.parseInt(destinationNode.getAttributes().getNamedItem("id").getNodeValue()))
                                                .name(destinationNode.getTextContent())
                                                .build());
                                    }
                                }
                                msg.setPagingDestinations(destinations);
                            }
                        }
                    }
                }
                msgs.getAlertMessages().add(msg);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return msgs;
    }
}
