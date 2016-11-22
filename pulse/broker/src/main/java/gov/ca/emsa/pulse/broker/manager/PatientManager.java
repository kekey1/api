package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;

import java.sql.SQLException;
import java.util.List;

public interface PatientManager extends CachedDataManager {
	public PatientDTO getPatientById(Long patientId);
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public PatientDTO create(PatientDTO toCreate) throws SQLException;
	public PatientDTO update(PatientDTO toUpdate) throws SQLException;
	public void delete(Long patientId) throws SQLException;
	
	public PatientLocationMapDTO updateOrganizationMap(PatientLocationMapDTO toUpdate) throws SQLException;
	public PatientLocationMapDTO createOrganizationMap(PatientLocationMapDTO toCreate) throws SQLException;
	public PatientLocationMapDTO createOrganizationMapFromPatientRecord(PatientDTO patient, Long patientRecordId) throws SQLException;	
}
