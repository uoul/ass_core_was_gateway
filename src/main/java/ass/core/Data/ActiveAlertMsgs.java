package ass.core.Data;

import ass.core.BusinessObjects.AlertMsg;

import javax.inject.Singleton;
import java.util.Vector;

@Singleton
public class ActiveAlertMsgs {
    Vector<AlertMsg> activeMsgs = new Vector<>();

    public void clearAlertMsgs() {
        activeMsgs.clear();
    }

    public Vector<AlertMsg> getActiveMsgs() {
        return activeMsgs;
    }

    public void setActiveMsgs(Vector<AlertMsg> activeMsgs) {
        this.activeMsgs = activeMsgs;
    }
}
