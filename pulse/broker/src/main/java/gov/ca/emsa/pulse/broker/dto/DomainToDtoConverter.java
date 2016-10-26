package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;
import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.NameAssembly;
import gov.ca.emsa.pulse.common.domain.NameRepresentation;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;
import gov.ca.emsa.pulse.common.domain.PatientRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;

public class DomainToDtoConverter {
	private static final Logger logger = LogManager.getLogger(DomainToDtoConverter.class);

	public static PatientDTO convertToPatient(Patient domainObj) {
		PatientDTO result = new PatientDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		result.setFriendlyName(domainObj.getFriendlyName());
		result.setFullName(domainObj.getFullName());
		result.setGender(domainObj.getGender());
		if(!StringUtils.isEmpty(domainObj.getDateOfBirth())) {
			LocalDate patientDob = null;
			try {
				patientDob = LocalDate.parse(domainObj.getDateOfBirth(), DateTimeFormatter.ISO_DATE);
			} catch(DateTimeParseException pex) {
				logger.error("Could not parse " + domainObj.getDateOfBirth() + " as a date in the format " + DateTimeFormatter.ISO_DATE);
			} 
			result.setDateOfBirth(patientDob);
		}
		result.setSsn(domainObj.getSsn());

		if(domainObj.getAcf() != null) {
			AlternateCareFacilityDTO acf = convert(domainObj.getAcf());
			result.setAcf(acf);
		}

		return result;
	}

	public static PatientRecordDTO convertToPatientRecord(PatientRecord domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		result.setOrganizationPatientRecordId(domainObj.getOrgPatientRecordId());
		
		if(domainObj.getPatientRecordName() != null){
			for(PatientRecordName PatientRecordName : domainObj.getPatientRecordName()){
				PatientRecordNameDTO PatientRecordNameDTO = new PatientRecordNameDTO();
				PatientRecordNameDTO.setFamilyName(PatientRecordName.getFamilyName());
				ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
				for(GivenName givenDto : PatientRecordName.getGivenName()){
					GivenNameDTO givenName = new GivenNameDTO();
					givenName.setGivenName(givenDto.getGivenName());
					givenName.setId(givenDto.getId());
					givenName.setPatientRecordNameId(givenDto.getPatientRecordNameId());
					givens.add(givenName);
				}
				PatientRecordNameDTO.setGivenName(givens);
				if(PatientRecordName.getSuffix() != null)
					PatientRecordNameDTO.setSuffix(PatientRecordName.getSuffix());
				if(PatientRecordName.getPrefix() != null)
					PatientRecordNameDTO.setPrefix(PatientRecordName.getPrefix());
				if(PatientRecordName.getNameType() != null){
					NameTypeDTO nameType = new NameTypeDTO();
					nameType.setCode(PatientRecordName.getNameType().getCode());
					nameType.setDescription(PatientRecordName.getNameType().getDescription());
					nameType.setId(PatientRecordName.getNameType().getId());
					PatientRecordNameDTO.setNameType(nameType);
				}
				if(PatientRecordName.getNameRepresentation() != null){
					NameRepresentationDTO nameRep = new NameRepresentationDTO();
					nameRep.setCode(PatientRecordName.getNameType().getCode());
					nameRep.setDescription(PatientRecordName.getNameType().getDescription());
					nameRep.setId(PatientRecordName.getNameType().getId());
					PatientRecordNameDTO.setNameRepresentation(nameRep);
				}
				if(PatientRecordName.getNameAssembly() != null){
					NameAssemblyDTO nameAssembly = new NameAssemblyDTO();
					nameAssembly.setCode(PatientRecordName.getNameType().getCode());
					nameAssembly.setDescription(PatientRecordName.getNameType().getDescription());
					nameAssembly.setId(PatientRecordName.getNameType().getId());
					PatientRecordNameDTO.setNameAssembly(nameAssembly);
				}
				if(PatientRecordName.getEffectiveDate() != null)
					PatientRecordNameDTO.setEffectiveDate(PatientRecordName.getEffectiveDate());
				if(PatientRecordName.getExpirationDate() != null)
					PatientRecordNameDTO.setExpirationDate(PatientRecordName.getExpirationDate());
			}
		}
		PatientGenderDTO pgDto = new PatientGenderDTO();
		pgDto.setCode(domainObj.getGender().getCode());
		pgDto.setDescription(domainObj.getGender().getDescription());
		result.setPatientGender(pgDto);
		if(!StringUtils.isEmpty(domainObj.getDateOfBirth())) {
			LocalDate patientDob = null;
			try {
				patientDob = LocalDate.parse(domainObj.getDateOfBirth(), DateTimeFormatter.ISO_DATE);
			} catch(DateTimeParseException pex) {
				logger.error("Could not parse " + domainObj.getDateOfBirth() + " as a date in the format " + DateTimeFormatter.ISO_DATE);
			} 
			result.setDateOfBirth(patientDob);
		}
		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());

		if(domainObj.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setStreetLineOne(domainObj.getAddress().getStreet1());
			address.setStreetLineTwo(domainObj.getAddress().getStreet2());
			address.setCity(domainObj.getAddress().getCity());
			address.setState(domainObj.getAddress().getState());
			address.setZipcode(domainObj.getAddress().getZipcode());
			address.setCountry(domainObj.getAddress().getCountry());
			result.setAddress(address);
		}

		return result;
	}

	public static DocumentDTO convert(Document domainObj) {
		DocumentDTO result = new DocumentDTO();
		result.setName(domainObj.getName());
		result.setFormat(domainObj.getFormat());
		result.setClassName(domainObj.getClassName());
		result.setConfidentiality(domainObj.getConfidentiality());
		result.setCreationTime(domainObj.getCreationTime());
		result.setDescription(domainObj.getDescription());
		result.setSize(domainObj.getSize());
		result.setPatientOrgMapId(domainObj.getOrgMapId());

		if(domainObj.getIdentifier() != null) {
			result.setDocumentUniqueId(domainObj.getIdentifier().getDocumentUniqueId());
			result.setHomeCommunityId(domainObj.getIdentifier().getHomeCommunityId());
			result.setRepositoryUniqueId(domainObj.getIdentifier().getRepositoryUniqueId());
		}
		return result;
	}
	
	public static AlternateCareFacilityDTO convert(AlternateCareFacility domainObj) {
		AlternateCareFacilityDTO result = new AlternateCareFacilityDTO();
		result.setId(domainObj.getId());
		result.setName(domainObj.getName());
		result.setPhoneNumber(domainObj.getPhoneNumber());
		if(domainObj.getAddress() != null) {
			if(domainObj.getAddress().getLines() != null) {
				for(String line : domainObj.getAddress().getLines()) {
					AddressLineDTO lineDto = new AddressLineDTO();
					lineDto.setLine(line);
					result.getLines().add(lineDto);
				}
			}
			result.setCity(domainObj.getAddress().getCity());
			result.setState(domainObj.getAddress().getState());
			result.setZipcode(domainObj.getAddress().getZipcode());
			result.setCountry(domainObj.getAddress().getCountry());
		}
		return result;
	}
}
