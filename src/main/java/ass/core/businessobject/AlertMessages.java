package ass.core.businessobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor@AllArgsConstructor
public class AlertMessages {

    @Builder.Default
    List<AlertMessage> alertMessages = new ArrayList<>();

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static AlertMessages fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, AlertMessages.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @SuperBuilder
    @ToString(callSuper = true)
    @NoArgsConstructor@AllArgsConstructor
    public static class AlertMessage {
        String key;
        Origin origin;
        String receiveTad;
        String operationId;
        Integer level;
        String name;
        String operationName;
        String caller;
        String location;
        String info;
        String program;
        String status;
        String watchOutTad;
        String finishedTad;
        List<Destination> destinations;
        List<Destination> pagingDestinations;

        @Data
        @SuperBuilder
        @ToString(callSuper = true)
        @NoArgsConstructor@AllArgsConstructor
        public static class Origin {
            Integer tid;
            String name;
        }

        @Data
        @SuperBuilder
        @ToString(callSuper = true)
        @NoArgsConstructor@AllArgsConstructor
        public static class Destination {
            Integer index;
            Integer id;
            String name;
        }
    }
}
