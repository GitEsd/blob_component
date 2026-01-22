package com.elo.integration.maskManager;

import com.elo.flows.api.components.annotations.Component;
import com.elo.flows.api.components.annotations.Connection;
import com.elo.flows.api.components.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.elo.ix.client.IXConnectionIF;
import de.elo.ix.client.Sord;
import de.elo.ix.client.SordC;
import de.elo.ix.client.LockC;
import de.elo.ix.client.EditInfoC;
import de.elo.ix.client.EditInfo;

@Component(preview = true, version = "v1", namespace = "eloro", name = "SordTypeService", displayName = "Sord Type Service")
public class SordTypeService {

    @Connection
    IXConnectionIF ixConnect;

    private static final Logger LOG = LoggerFactory.getLogger(SordTypeService.class);

    @Service(displayName = "Get Sord Type")
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
            
            // Checkout SORD cu lock pentru a obține informațiile
            EditInfo editInfo = ixConnect.ix().checkoutSord(
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
   
}
