package eloro;

import com.elo.flows.api.schema.annotations.DisplayOptions;
import com.elo.flows.api.schema.annotations.Property;

public class GetSignDataInput {
    
    @DisplayOptions(order = 1)
    @Property(displayName = "Object ID", description = "The ELO Object ID (GUID or numeric ID) of the document", required = true)
    private String objId;
    
    @DisplayOptions(order = 2)
    @Property(displayName = "BLOB Key", description = "The FORMBLOB key to read (e.g., WO_SIGN)", required = true)
    private String blobKey;

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getBlobKey() {
        return blobKey;
    }

    public void setBlobKey(String blobKey) {
        this.blobKey = blobKey;
    }
}
