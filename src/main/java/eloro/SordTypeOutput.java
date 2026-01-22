package eloro;

import com.elo.flows.api.schema.annotations.DisplayOptions;
import com.elo.flows.api.schema.annotations.Property;

public class SordTypeOutput {
    
    @DisplayOptions(order = 1)
    @Property(displayName = "Sord Type", description = "The type of the SORD (e.g., FOLDER, DOCUMENT, etc.)")
    private String sordType;
    
    @DisplayOptions(order = 2)
    @Property(displayName = "Sord Type ID", description = "The numeric type ID of the SORD")
    private int sordTypeId;
    
    @DisplayOptions(order = 3)
    @Property(displayName = "Mask Name", description = "The keywording mask name")
    private String maskName;
    
    @DisplayOptions(order = 4)
    @Property(displayName = "Mask ID", description = "The keywording mask ID")
    private int maskId;
    
    @DisplayOptions(order = 5)
    @Property(displayName = "Object Name", description = "The name of the SORD object")
    private String objectName;
    
    @DisplayOptions(order = 6)
    @Property(displayName = "Success", description = "Whether the operation was successful")
    private boolean success;
    
    @DisplayOptions(order = 7)
    @Property(displayName = "Error Message", description = "Error message if operation failed")
    private String errorMessage;

    // Getters and Setters
    public String getSordType() {
        return sordType;
    }

    public void setSordType(String sordType) {
        this.sordType = sordType;
    }

    public int getSordTypeId() {
        return sordTypeId;
    }

    public void setSordTypeId(int sordTypeId) {
        this.sordTypeId = sordTypeId;
    }

    public String getMaskName() {
        return maskName;
    }

    public void setMaskName(String maskName) {
        this.maskName = maskName;
    }

    public int getMaskId() {
        return maskId;
    }

    public void setMaskId(int maskId) {
        this.maskId = maskId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
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
}
