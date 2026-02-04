package eloro;

import com.elo.flows.api.schema.annotations.DisplayOptions;
import com.elo.flows.api.schema.annotations.Property;

import java.util.List;

public class GetSignDataOutput {
    
    @DisplayOptions(order = 1)
    @Property(displayName = "BLOB Data", description = "The raw BLOB data as string")
    private String blobData;
    
    @DisplayOptions(order = 2)
    @Property(displayName = "BLOB Type", description = "The type of the BLOB field")
    private String blobType;
    
    @DisplayOptions(order = 3)
    @Property(displayName = "Success", description = "Whether the operation was successful")
    private boolean success;
    
    @DisplayOptions(order = 4)
    @Property(displayName = "Error Message", description = "Error message if operation failed")
    private String errorMessage;
    
    @DisplayOptions(order = 5)
    @Property(displayName = "Available Keys", description = "List of all available FORMBLOB keys when requested key is not found")
    private List<String> availableKeys;
    
    @DisplayOptions(order = 6)
    @Property(displayName = "Debug Info", description = "Debug information for troubleshooting")
    private String debugInfo;

    // Getters and Setters
    public String getBlobData() {
        return blobData;
    }

    public void setBlobData(String blobData) {
        this.blobData = blobData;
    }

    public String getBlobType() {
        return blobType;
    }

    public void setBlobType(String blobType) {
        this.blobType = blobType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public List<String> getAvailableKeys() {
        return availableKeys;
    }
    
    public void setAvailableKeys(List<String> availableKeys) {
        this.availableKeys = availableKeys;
    }
    
    public String getDebugInfo() {
        return debugInfo;
    }
    
    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }
}
