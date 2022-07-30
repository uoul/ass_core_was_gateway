package ass.core;

import ass.core.BusinessObjects.AlertMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Vector;

public class JavaMain {
    public static void main(String[] args) {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"iso-8859-15\" standalone=\"yes\"?>\n" +
                "<pdu>\n" +
                "<order-list count=\"2\">\n" +
                "<order index=\"1\">\n" +
                "<key>0x07abe41b</key>\n" +
                "<origin tid=\"0300012\">Linz-Stadt</origin>\n" +
                "<receive-tad>2008-11-06 13:21:01</receive-tad>\n" +
                "<operation-id>BWSt40001 </operation-id>\n" +
                "<level>1</level>\n" +
                "<name>Max Mustermann</name>\n" +
                "<operation-name>BRAND BAUM-, FLUR-, BÖSCHUNG</operation-name>\n" +
                "<caller>012345</caller>\n" +
                "<location>Musterstraße 42</location>\n" +
                "<info>HILFE!!!</info>\n" +
                "<program>Feuer</program>\n" +
                "<status>Ausgerückt</status>\n" +
                "<watch-out-tad>2008-11-06 13:46:44</watch-out-tad>\n" +
                "<finished-tad></finished-tad>\n" +
                "<destination-list count=\"2\">\n" +
                "<destination index=\"1\" id=\"40117\">Test 1</destination>\n" +
                "<destination index=\"2\" id=\"40120\">Test 2</destination>\n" +
                "</destination-list>\n" +
                "<paging-destination-list count=\"1\">\n" +
                "<paging-destination index=\"1\" id=\"40122\">Test3</paging-destination>\n" +
                "</paging-destination-list>\n" +
                "</order>\n" +
                "<order index=\"2\">\n" +
                "<key>0x07abe41c</key>\n" +
                "<origin tid=\"0300012\">Linz-Stadt</origin>\n" +
                "<receive-tad>2008-11-06 13:48:41</receive-tad>\n" +
                "<operation-id>BWSt40002 </operation-id>\n" +
                "<level>1</level>\n" +
                "<name>Hasso</name>\n" +
                "<operation-name>TE TIERRETTUNG</operation-name>\n" +
                "<caller>0123456789</caller>\n" +
                "<location>0000 Musterhausen, Musterweg 1</location>\n" +
                "<info>Tiger im Tank</info>\n" +
                "<program>Feuer</program>\n" +
                "<status>Alarmiert</status>\n" +
                "<watch-out-tad></watch-out-tad>\n" +
                "<finished-tad></finished-tad>\n" +
                "<destination-list count=\"1\">\n" +
                "<destination index=\"1\" id=\"40117\">Test 1</destination>\n" +
                "</destination-list>\n" +
                "</order>\n" +
                "</order-list>\n" +
                "</pdu>";
        Vector<AlertMsg> msgs = WasXmlInterpreter.parseXmlToAlertMgs(xmlStr);
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writeValueAsString(msgs));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
