package eloro;

import com.elo.flows.api.schema.annotations.DisplayOptions;
import com.elo.flows.api.schema.annotations.Property;

public class SetSignDataInput {
    
    @DisplayOptions(order = 1)
    @Property(displayName = "Object ID", description = "The ELO Object ID (GUID or numeric ID) of the document", required = true)
    private String objId;
    
    @DisplayOptions(order = 2)
    @Property(displayName = "BLOB Key", description = "The FORMBLOB key to write (e.g., WO_SIGN)", required = true)
    private String blobKey;
    
    @DisplayOptions(order = 3)
    @Property(displayName = "BLOB Type", description = "The type of the field (e.g., FORMBLOB, GRP, MAP)", required = true)
    private String blobType;
    
    @DisplayOptions(order = 4)
    @Property(displayName = "BLOB Data", description = "The BLOB data to save", required = true)
    private String blobData;

    // Getters and Setters
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

    public String getBlobType() {
        return blobType;
    }

    public void setBlobType(String blobType) {
        this.blobType = blobType;
    }

    public String getBlobData() {
        return blobData;
    }

    public void setBlobData(String blobData) {
        this.blobData = blobData;
    }
}
