package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentWrapper;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201310UV02;

public interface JSONToSOAPService {
	public PRPAIN201310UV02 convertPatientRecordListToSOAPResponse(List<PatientRecord> patientRecords);
	public AdhocQueryResponse convertDocumentListToSOAPResponse(List<Document> docs, String patientId);
	public RetrieveDocumentSetResponseType convertDocumentSetToSOAPResponse(List<DocumentWrapper> docs);
}