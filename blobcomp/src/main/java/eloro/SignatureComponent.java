package eloro;

import de.elo.ix.client.MapData;
import de.elo.ix.client.MapValue;
import de.elo.ix.client.FileData;
import java.util.Map;

import com.elo.flows.api.components.annotations.Component;
import com.elo.flows.api.components.annotations.Connection;
import com.elo.flows.api.components.annotations.ConnectionRequired;
import com.elo.flows.api.components.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.elo.ix.client.IXConnection;
import de.elo.ix.client.Sord;
import de.elo.ix.client.SordC;
import eloro.GetSignDataInput;
import eloro.GetSignDataOutput;
import eloro.SetSignDataInput;
import eloro.SetSignDataOutput;
import eloro.SordTypeInput;
import eloro.SordTypeOutput;
import de.elo.ix.client.LockC;
import de.elo.ix.client.EditInfoC;
import de.elo.ix.client.EditInfo;
import de.elo.ix.client.ObjKey;
import de.elo.ix.client.KeyValue;
import de.elo.ix.client.MapValue;
import de.elo.ix.client.FileData;
import de.elo.ix.client.MapDomainC;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component(preview = true, version = "v1", namespace = "eloro", name = "SignatureComponent", displayName = "Signature Component")
public class SignatureComponent {

    @Connection
    IXConnection ixConnection;

    private static final Logger LOG = LoggerFactory.getLogger(SignatureComponent.class);
    @Service(displayName = "Get Sord Type")
    @ConnectionRequired
    public SordTypeOutput getSordType(SordTypeInput input) {
        SordTypeOutput output = new SordTypeOutput();
        
        try {
            String objId = input.getObjId();
            
            if (objId == null || objId.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("Object ID is required");
                return output;
            }
            
            LOG.info("Getting SORD type for objId: {}", objId);
            
            // Checkout SORD pentru a obține informațiile
            EditInfo editInfo = ixConnection.ix().checkoutSord(
                objId, 
                EditInfoC.mbSord, 
                LockC.NO
            );
            
            Sord sord = editInfo.getSord();
            
            if (sord == null) {
                output.setSuccess(false);
                output.setErrorMessage("SORD not found for objId: " + objId);
                return output;
            }
            
            output.setObjectName(sord.getName());
            output.setMaskId(sord.getMask());
            output.setMaskName(sord.getMaskName());
            output.setSordTypeId(sord.getType());
            
            String sordTypeName = determineSordTypeName(sord.getType());
            output.setSordType(sordTypeName);
            
            output.setSuccess(true);
            
            LOG.info("SORD type retrieved successfully: type={}, typeName={}, mask={}", 
                sord.getType(), sordTypeName, sord.getMaskName());
            
        } catch (Exception e) {
            LOG.error("Error getting SORD type", e);
            output.setSuccess(false);
            output.setErrorMessage("Error: " + e.getMessage());
        }
        
        return output;
    }
    

    private String determineSordTypeName(int type) {
        if (type < SordC.LBT_DOCUMENT) {
            // Este un folder - verificăm tipurile standard de folder din ELO
            // LBT_ARCHIVE = 0, alte foldere au valori între 0 și LBT_DOCUMENT
            if (type == SordC.LBT_ARCHIVE) {
                return "ARCHIVE";
            } else {
                return "FOLDER";
            }
        } else {
            // Este un document
            if (type == SordC.LBT_DOCUMENT) {
                return "DOCUMENT";
            } else if (type == SordC.LBT_DOCUMENT_MAX) {
                return "DOCUMENT_MAX";
            } else {
                return "DOCUMENT";
            }
        }
    }

    /**
     * Service pentru obținerea datelor de semnătură din FORMBLOB.
     * Echivalent cu RF_eloroapps_serviceGetSignData
     */
    @Service(displayName = "Get Sign Data")
    @ConnectionRequired
    public GetSignDataOutput getSignData(GetSignDataInput input) {
        GetSignDataOutput output = new GetSignDataOutput();
        try {
            String objId = input.getObjId();
            String blobKey = input.getBlobKey();
            if (objId == null || objId.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("Object ID is required");
                return output;
            }
            if (blobKey == null || blobKey.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("BLOB Key is required");
                return output;
            }
            String blobValue = getFormBlobValue(objId, blobKey);
            if (blobValue != null) {
                output.setBlobData(blobValue);
                output.setBlobType("FORMBLOB");
                output.setSuccess(true);
            } else {
                List<String> availableKeys = getAllFormDataKeys(objId);
                output.setAvailableKeys(availableKeys);
                output.setBlobType("FORMBLOB");
                output.setSuccess(true);
                output.setErrorMessage("Key '" + blobKey + "' not found. See availableKeys for existing keys.");
            }
        } catch (Exception e) {
            output.setSuccess(false);
            output.setErrorMessage("Error: " + e.getMessage());
        }
        return output;
    }

    private String getFormBlobValue(String objId, String key) throws Exception {
        MapData mapData = ixConnection.ix().checkoutMap("FORMDATA", objId, new String[] { key }, LockC.NO);
        if (mapData.getItems() != null && mapData.getItems().length > 0) {
            for (Object item : mapData.getItems()) {
                if (item instanceof MapValue) {
                    MapValue mv = (MapValue) item;
                    if (mv.getKey() != null && mv.getKey().equals(key)) {
                        FileData fd = mv.getBlobValue();
                        if (fd != null) {
                            try (InputStream stream = fd.getStream()) {
                                if (stream != null) {
                                    return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                                }
                            }
                        }
                    }
                }
            }
        }
        Map<String, MapValue> mapItems = mapData.getMapItems();
        if (mapItems != null && mapItems.containsKey(key)) {
            MapValue mv = mapItems.get(key);
            FileData fd = mv.getBlobValue();
            if (fd != null) {
                try (InputStream stream = fd.getStream()) {
                    if (stream != null) {
                        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Returnează toate cheile FORMDATA disponibile pentru un objId cu info despre valori
     */
    private List<String> getAllFormDataKeys(String objId) {
        List<String> keys = new ArrayList<>();
        try {

            Object[] allItems = ixConnection.ix().checkoutMap("FORMDATA", objId, new String[] { "*" }, LockC.NO).getItems();
            
            if (allItems != null) {
                for (Object item : allItems) {
                    String keyInfo = "";
                    if (item instanceof KeyValue) {
                        KeyValue kv = (KeyValue) item;
                        if (kv.getKey() != null) {
                            String val = kv.getValue();
                            keyInfo = kv.getKey() + " (KeyValue, value=" + (val != null ? "'" + val.substring(0, Math.min(20, val.length())) + "...'" : "null") + ")";
                            keys.add(keyInfo);
                        }
                    } else if (item instanceof MapValue) {
                        MapValue mv = (MapValue) item;
                        if (mv.getKey() != null) {
                            FileData fd = mv.getBlobValue();
                            keyInfo = mv.getKey() + " (MapValue, hasBlob=" + (fd != null && fd.getData() != null) + ")";
                            keys.add(keyInfo);
                        }
                    } else {
                        // Alt tip
                        keys.add("Unknown type: " + item.getClass().getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error getting FORMDATA keys for objId '{}': {}", objId, e.getMessage());
        }
        return keys;
    }

   
    @Service(displayName = "Set Sign Data")
    @ConnectionRequired
    public SetSignDataOutput setSignData(SetSignDataInput input) {
        SetSignDataOutput output = new SetSignDataOutput();
        
        try {
            String objId = input.getObjId();
            String blobKey = input.getBlobKey();
            String blobType = input.getBlobType();
            String blobData = input.getBlobData();
            
            if (objId == null || objId.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("Object ID is required");
                return output;
            }
            
            if (blobKey == null || blobKey.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("BLOB Key is required");
                return output;
            }
            
            if (blobType == null || blobType.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("BLOB Type is required");
                return output;
            }
            
            if (blobData == null || blobData.trim().isEmpty()) {
                output.setSuccess(false);
                output.setErrorMessage("BLOB Data is required");
                return output;
            }
            
            LOG.info("Setting sign data for objId: {}, blobKey: {}, blobType: {}", objId, blobKey, blobType);
            
            // Setează FORMBLOB folosind MapItems API
            setFormBlobValue(objId, blobKey, blobData);
            
            output.setObjId(objId);
            output.setSuccess(true);
            output.setMessage("Sign data saved successfully for key: " + blobKey);
            
            LOG.info("Sign data set successfully for objId: {}, key: {}", objId, blobKey);
            
        } catch (Exception e) {
            LOG.error("Error setting sign data", e);
            output.setSuccess(false);
            output.setErrorMessage("Error: " + e.getMessage());
        }
        
        return output;
    }
    
    /**
     * Setează valoarea FORMBLOB folosind MapValue+FileData (BLOB)
     */
    private void setFormBlobValue(String objId, String key, String value) throws Exception {
        // Creează MapValue cu FileData pentru BLOB
        FileData fileData = new FileData("text/plain", value.getBytes(StandardCharsets.UTF_8));
        MapValue mapValue = new MapValue(key, fileData);
        int objIdNum = Integer.parseInt(objId);
        ixConnection.ix().checkinMap("FORMDATA", objId, objIdNum, new MapValue[] { mapValue }, LockC.NO);
        LOG.info("FORMDATA BLOB set for key '{}' on objId '{}' (MapValue+FileData)", key, objId);
    }
    @Service(displayName = "Get session ticket")
    @ConnectionRequired
    public String getSessionTicket() {
        try {
            String ticket = ixConnection.getLoginResult().getClientInfo().getTicket();
            return ticket;
        } catch (Exception e) {
            return null;
        }
    }
}