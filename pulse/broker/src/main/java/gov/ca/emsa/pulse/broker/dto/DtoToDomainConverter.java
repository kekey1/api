package gov.ca.emsa.pulse.broker.dto;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;

import gov.ca.emsa.pulse.common.domain.Organization;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientOrganizationMap;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryOrganization;

public class DtoToDomainConverter {
	private static final Logger logger = LogManager.getLogger(DtoToDomainConverter.class);
	private static final DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static Patient convert(PatientDTO dtoObj) {
		Patient result = new Patient();
		result.setId(dtoObj.getId());
		if(dtoObj.getPatientName() != null){
			result.getPatientName().setFamilyName(dtoObj.getPatientName().getFamilyName());
			ArrayList<GivenName> givens = new ArrayList<GivenName>();
			for(GivenNameDTO givenDto : dtoObj.getPatientName().getGivenName()){
				GivenName givenName = new GivenName();
				givenName.setGivenName(givenDto.getGivenName());
				givenName.setId(givenDto.getId());
				givenName.setPatientNameId(givenDto.getPatientNameId());
				givens.add(givenName);
			}
			result.getPatientName().setGivenName(givens);
			if(dtoObj.getPatientName().getSuffix() != null)
				result.getPatientName().setSuffix(dtoObj.getPatientName().getSuffix());
			if(dtoObj.getPatientName().getPrefix() != null)
				result.getPatientName().setPrefix(dtoObj.getPatientName().getPrefix());
			if(dtoObj.getPatientName().getNameTypeCode() != null)
				result.getPatientName().setNameTypeCode(dtoObj.getPatientName().getNameTypeCode());
			if(dtoObj.getPatientName().getNameTypeCodeDescription() != null)
				result.getPatientName().setNameTypeCodeDescription(dtoObj.getPatientName().getNameTypeCodeDescription());
			if(dtoObj.getPatientName().getNameRepresentationCode() != null)
				result.getPatientName().setNameRepresentationCode(dtoObj.getPatientName().getNameRepresentationCode());
			if(dtoObj.getPatientName().getNameRepresentationCodeDescription() != null)
				result.getPatientName().setNameRepresentationCodeDescription(dtoObj.getPatientName().getNameRepresentationCodeDescription());
			if(dtoObj.getPatientName().getNameAssemblyOrderCode() != null)
				result.getPatientName().setNameAssemblyOrderCode(dtoObj.getPatientName().getNameAssemblyOrderCode());
			if(dtoObj.getPatientName().getNameAssemblyOrderCodeDescription() != null)
				result.getPatientName().setNameAssemblyOrderCodeDescription(dtoObj.getPatientName().getNameAssemblyOrderCodeDescription());
			if(dtoObj.getPatientName().getEffectiveDate() != null)
				result.getPatientName().setEffectiveDate(dtoObj.getPatientName().getEffectiveDate());
			if(dtoObj.getPatientName().getExpirationDate() != null)
				result.getPatientName().setExpirationDate(dtoObj.getPatientName().getExpirationDate());
		}
		result.setGender(dtoObj.getGender());
		if(dtoObj.getDateOfBirth() != null) {
			result.setDateOfBirth(outFormatter.format(dtoObj.getDateOfBirth()));
		}
		result.setPhoneNumber(dtoObj.getPhoneNumber());
		result.setSsn(dtoObj.getSsn());
		result.setLastRead(dtoObj.getLastReadDate());
		
		if(dtoObj.getAcf() != null) {
			AlternateCareFacility acf = new AlternateCareFacility();
			acf.setId(dtoObj.getAcf().getId());
			acf.setName(dtoObj.getAcf().getName());
			if(dtoObj.getAcf().getAddress() != null)  {
				AddressDTO acfAddrDto = dtoObj.getAcf().getAddress();
				Address acfAddr = convert(acfAddrDto);
				acf.setAddress(acfAddr);
			}
			result.setAcf(acf);
		}
		
		if(dtoObj.getAddress() != null) {
			Address addr = convert(dtoObj.getAddress());
			result.setAddress(addr);
		}
		
		if(dtoObj.getOrgMaps() != null && dtoObj.getOrgMaps().size() > 0) {
			for(PatientOrganizationMapDTO orgMapDto : dtoObj.getOrgMaps()) {
				PatientOrganizationMap orgMap = DtoToDomainConverter.convert(orgMapDto);
				result.getOrgMaps().add(orgMap);
			}
		}
		
		return result;
	}
	
	public static PatientOrganizationMap convert(PatientOrganizationMapDTO dto) {	
		PatientOrganizationMap result = new PatientOrganizationMap();
		result.setId(dto.getId());
		result.setPatientId(dto.getPatientId());
		if(dto.getOrg() != null) {
			result.setOrganization(DtoToDomainConverter.convert(dto.getOrg()));
		} else {
			Organization org = new Organization();
			org.setId(dto.getOrganizationId());
			result.setOrganization(org);
		}
		result.setDocumentsQuerySuccess(dto.getDocumentsQuerySuccess());
		result.setDocumentsQueryStatus(dto.getDocumentsQueryStatus());
		result.setDocumentsQueryStart(dto.getDocumentsQueryStart());
		result.setDocumentsQueryEnd(dto.getDocumentsQueryEnd());

		if(dto.getDocuments() != null && dto.getDocuments().size() > 0) {
			for(DocumentDTO docDto : dto.getDocuments()) {
				Document doc = DtoToDomainConverter.convert(docDto);
				result.getDocuments().add(doc);
			}
		}
		return result;
	}
	
	public static Address convert(AddressDTO addressDto){
		Address address = new Address();
		address.setId(addressDto.getId());
		address.setCity(addressDto.getCity());
		address.setCountry(address.getCountry());
		address.setState(addressDto.getState());
		address.setStreet1(addressDto.getStreetLineOne());
		address.setStreet2(addressDto.getStreetLineTwo());
		address.setZipcode(addressDto.getZipcode());
		address.setCountry(addressDto.getCountry());
		return address;
	}
	
	public static AlternateCareFacility convert(AlternateCareFacilityDTO acfDto){
		AlternateCareFacility acf = new AlternateCareFacility();
		if(acfDto.getAddress() != null){
			acf.setAddress(convert(acfDto.getAddress()));
		}
		acf.setId(acfDto.getId());
		acf.setName(acfDto.getName());
		acf.setPhoneNumber(acfDto.getPhoneNumber());
		acf.setLastRead(acfDto.getLastReadDate());
		return acf;
	}
	
	public static Query convert(QueryDTO queryDto){
		Query query = new Query();
		query.setId(queryDto.getId());
		query.setStatus(queryDto.getStatus());
		query.setLastRead(queryDto.getLastReadDate());

		try {
			ObjectMapper termReader = new ObjectMapper();
			PatientSearch terms = termReader.readValue(queryDto.getTerms(), PatientSearch.class);
			query.setTerms(terms);
		} catch(IOException ioex) {
			logger.error("Could not read " + queryDto.getTerms() + " as JSON.");
		}
		
		query.setUserToken(queryDto.getUserId());
		for(QueryOrganizationDTO qOrgDto : queryDto.getOrgStatuses()){
			QueryOrganization qOrg = DtoToDomainConverter.convert(qOrgDto);
			query.getOrgStatuses().add(qOrg);
		}
		return query;
	}
	
	public static QueryOrganization convert(QueryOrganizationDTO qOrgDto){
		QueryOrganization qOrg = new QueryOrganization();
		qOrg.setId(qOrgDto.getId());
		
		if(qOrgDto.getOrg() != null) {
			qOrg.setOrg(convert(qOrgDto.getOrg()));
		}
		
		qOrg.setQueryId(qOrgDto.getQueryId());
		for(PatientRecordDTO prDto : qOrgDto.getResults()){
			PatientRecord pr = DtoToDomainConverter.convert(prDto);
			qOrg.getResults().add(pr);
		}		
		qOrg.setStartDate(qOrgDto.getStartDate());
		qOrg.setEndDate(qOrgDto.getEndDate());
		qOrg.setStatus(qOrgDto.getStatus());
		qOrg.setSuccess(qOrgDto.getSuccess());
		return qOrg;
	}
	
	public static PatientRecord convert(PatientRecordDTO prDto){
		PatientRecord pr = new PatientRecord();
		pr.setId(prDto.getId());
		pr.setSsn(prDto.getSsn());
		if(prDto.getPatientName() != null){
			pr.getPatientName().setFamilyName(prDto.getPatientName().getFamilyName());
			ArrayList<GivenName> givens = new ArrayList<GivenName>();
			for(GivenNameDTO givenDto : prDto.getPatientName().getGivenName()){
				GivenName givenName = new GivenName();
				givenName.setGivenName(givenDto.getGivenName());
				givenName.setId(givenDto.getId());
				givenName.setPatientNameId(givenDto.getPatientNameId());
				givens.add(givenName);
			}
			pr.getPatientName().setGivenName(givens);
			if(prDto.getPatientName().getSuffix() != null)
				pr.getPatientName().setSuffix(prDto.getPatientName().getSuffix());
			if(prDto.getPatientName().getPrefix() != null)
				pr.getPatientName().setPrefix(prDto.getPatientName().getPrefix());
			if(prDto.getPatientName().getNameTypeCode() != null)
				pr.getPatientName().setNameTypeCode(prDto.getPatientName().getNameTypeCode());
			if(prDto.getPatientName().getNameTypeCodeDescription() != null)
				pr.getPatientName().setNameTypeCodeDescription(prDto.getPatientName().getNameTypeCodeDescription());
			if(prDto.getPatientName().getNameRepresentationCode() != null)
				pr.getPatientName().setNameRepresentationCode(prDto.getPatientName().getNameRepresentationCode());
			if(prDto.getPatientName().getNameRepresentationCodeDescription() != null)
				pr.getPatientName().setNameRepresentationCodeDescription(prDto.getPatientName().getNameRepresentationCodeDescription());
			if(prDto.getPatientName().getNameAssemblyOrderCode() != null)
				pr.getPatientName().setNameAssemblyOrderCode(prDto.getPatientName().getNameAssemblyOrderCode());
			if(prDto.getPatientName().getNameAssemblyOrderCodeDescription() != null)
				pr.getPatientName().setNameAssemblyOrderCodeDescription(prDto.getPatientName().getNameAssemblyOrderCodeDescription());
			if(prDto.getPatientName().getEffectiveDate() != null)
				pr.getPatientName().setEffectiveDate(prDto.getPatientName().getEffectiveDate());
			if(prDto.getPatientName().getExpirationDate() != null)
				pr.getPatientName().setExpirationDate(prDto.getPatientName().getExpirationDate());
		}
		pr.setGender(prDto.getGender());
		pr.setPhoneNumber(prDto.getPhoneNumber());
		if(prDto.getDateOfBirth() != null) {
			pr.setDateOfBirth(outFormatter.format(prDto.getDateOfBirth()));
		}		
		if(prDto.getAddress() != null) {
			Address address = new Address();
			address.setStreet1(prDto.getAddress().getStreetLineOne());
			address.setStreet2(prDto.getAddress().getStreetLineTwo());
			address.setCity(prDto.getAddress().getCity());
			address.setState(prDto.getAddress().getState());
			address.setZipcode(prDto.getAddress().getZipcode());
			address.setCountry(prDto.getAddress().getCountry());
			pr.setAddress(address);
		}
		return pr;
	}
	
	public static Organization convert(OrganizationDTO orgDto){
		Organization org = new Organization();
		org.setId(orgDto.getId());
		org.setName(orgDto.getName());
		org.setAdapter(orgDto.getAdapter());
		org.setActive(orgDto.isActive());
		org.setOrganizationId(orgDto.getOrganizationId());
		if(orgDto.getCertificationKey() != null){
			org.setCertificationKey(orgDto.getCertificationKey());
		}
		if(orgDto.getEndpointUrl() != null){
			org.setEndpointUrl(orgDto.getEndpointUrl());
		}
		if(orgDto.getIpAddress() != null){
			org.setIpAddress(orgDto.getIpAddress());
		}
		if(orgDto.getUsername() != null){
			org.setUsername(orgDto.getUsername());
		}
		if(orgDto.getPassword() != null){
			org.setPassword(orgDto.getPassword());
		}
		return org;
	}
	
	public static Document convert(DocumentDTO dtoObj) {
		Document result = new Document();
		result.setId(dtoObj.getId()+"");
		result.setName(dtoObj.getName());
		result.setFormat(dtoObj.getFormat());
		result.setCached(dtoObj.getContents() != null && dtoObj.getContents().length > 0);
		result.setOrgMapId(dtoObj.getPatientOrgMapId());
		
		result.setClassName(dtoObj.getClassName());
		result.setConfidentiality(dtoObj.getConfidentiality());
		result.setCreationTime(dtoObj.getCreationTime());
		result.setDescription(dtoObj.getDescription());
		result.setSize(dtoObj.getSize());

		DocumentIdentifier docId = new DocumentIdentifier();
		docId.setDocumentUniqueId(dtoObj.getDocumentUniqueId());
		docId.setHomeCommunityId(dtoObj.getHomeCommunityId());
		docId.setRepositoryUniqueId(dtoObj.getRepositoryUniqueId());
		result.setIdentifier(docId);
		return result;
	}
}
