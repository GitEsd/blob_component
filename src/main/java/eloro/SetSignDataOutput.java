package eloro;

import com.elo.flows.api.schema.annotations.DisplayOptions;
import com.elo.flows.api.schema.annotations.Property;

public class SetSignDataOutput {
    
    @DisplayOptions(order = 1)
    @Property(displayName = "Object ID", description = "The Object ID of the updated SORD")
    private String objId;
    
    @DisplayOptions(order = 2)
    @Property(displayName = "Success", description = "Whether the operation was successful")
    private boolean success;
    
    @DisplayOptions(order = 3)
    @Property(displayName = "Message", description = "Status message")
    private String message;
    
    @DisplayOptions(order = 4)
    @Property(displayName = "Error Message", description = "Error message if operation failed")
    private String errorMessage;

    // Getters and Setters
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
