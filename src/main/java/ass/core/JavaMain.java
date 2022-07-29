package ass.core;

import ass.core.BusinessObjects.AlertMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Vector;

public class JavaMain {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Vector<AlertMsg.Destination> v = new Vector<>();
        v.add(AlertMsg.Destination.builder().build());
        AlertMsg msg = AlertMsg.builder()
                .origin(AlertMsg.Origin.builder().build())
                .destinations(v)
                .pagingDestinations(v)
                .build();
        try {
            System.out.println(mapper.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
