package ass.core.BusinessObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Vector;

@Data
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor@AllArgsConstructor
public class AlertMsg {
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
    Vector<Destination> destinations;
    Vector<Destination> pagingDestinations;

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
