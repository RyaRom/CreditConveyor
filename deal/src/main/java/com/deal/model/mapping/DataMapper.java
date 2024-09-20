package com.deal.model.mapping;

import com.deal.model.dto.ApplicationDTO;
import com.deal.model.dto.ClientDTO;
import com.deal.model.dto.CreditDTO;
import com.deal.model.dto.EmploymentDTO;
import com.deal.model.dto.FinishRegistrationRequestDTO;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.dto.ScoringDataDTO;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.entities.Credit;
import com.deal.model.json.Employment;
import com.deal.model.json.LoanOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DataMapper {

    @Mappings({
            @Mapping(source = "passportSeries", target = "passportId.series"),
            @Mapping(source = "passportNumber", target = "passportId.number"),
    })
    Client toClient(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanOffer toOfferJsonb(LoanOfferDTO loanOfferDTO);

    @Mappings({
            @Mapping(source = "clientId", target = "client"),
            @Mapping(source = "creditId", target = "credit"),
            @Mapping(source = "statusHistoryId", target = "statusHistory"),
            @Mapping(source = "secCode", target = "sesCode")
    })
    ApplicationDTO toApplicationDTO(Application application);

    @Mappings({
            @Mapping(source = "passportId", target = "passport"),
            @Mapping(source = "employmentId", target = "employment")
    })
    ClientDTO toClientDTO(Client client);

    @Mappings({
            @Mapping(source = "employmentStatus", target = "status"),
            @Mapping(source = "employerInn", target = "employmentInn")
    })
    Employment toEmploymentJsonb(EmploymentDTO employmentDTO);

    @Mappings({
            @Mapping(source = "status", target = "employmentStatus"),
            @Mapping(source = "employmentInn", target = "employerInn")
    })
    EmploymentDTO toEmploymentDTO(Employment employment);

    @Mappings({
            @Mapping(source = "isInsuranceEnabled", target = "insuranceEnable"),
            @Mapping(source = "isSalaryClient", target = "salaryClient"),
    })
    Credit toCredit(CreditDTO creditDTO);
    @Mappings({
            @Mapping(source = "insuranceEnable", target = "isInsuranceEnabled"),
            @Mapping(source = "salaryClient", target = "isSalaryClient"),
    })
    CreditDTO toCreditDTO(Credit credit);

    @Mappings({
            @Mapping(source = "request.gender", target = "gender"),
            @Mapping(source = "request.passportIssueBranch", target = "passportIssueBranch"),
            @Mapping(source = "request.passportIssueDate", target = "passportIssueDate"),
            @Mapping(source = "request.maritalStatus", target = "maritalStatus"),
            @Mapping(source = "request.dependentAmount", target = "dependentAmount"),
            @Mapping(source = "request.employment", target = "employment"),
            @Mapping(source = "request.account", target = "account"),
            @Mapping(source = "application.appliedOffer.requestedAmount", target = "amount"),
            @Mapping(source = "application.appliedOffer.term", target = "term"),
            @Mapping(source = "application.clientId.firstName", target = "firstName"),
            @Mapping(source = "application.clientId.middleName", target = "middleName"),
            @Mapping(source = "application.clientId.lastName", target = "lastName"),
            @Mapping(source = "application.clientId.birthdate", target = "birthdate"),
            @Mapping(source = "application.clientId.passportId.series", target = "passportSeries"),
            @Mapping(source = "application.clientId.passportId.number", target = "passportNumber"),
            @Mapping(source = "application.appliedOffer.isInsuranceEnabled", target = "isInsuranceEnabled"),
            @Mapping(source = "application.appliedOffer.isSalaryClient", target = "isSalaryClient")
    })
    ScoringDataDTO mapScoringData(FinishRegistrationRequestDTO request, Application application);
}
