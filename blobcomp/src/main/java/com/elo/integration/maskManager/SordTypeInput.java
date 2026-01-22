package com.elo.integration.maskManager;

import com.elo.flows.api.schema.annotations.DisplayOptions;
import com.elo.flows.api.schema.annotations.Property;

public class SordTypeInput {
    
    @DisplayOptions(order = 1)
    @Property(displayName = "Object ID", description = "The ELO Object ID (GUID or numeric ID) of the document/folder", required = true)
    private String objId;

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
