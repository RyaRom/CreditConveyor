package com.deal.model.mapping;

import com.deal.model.dto.*;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.entities.Credit;
import com.deal.model.entities.PaymentScheduleElement;
import com.deal.model.json.Employment;
import com.deal.model.json.LoanOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ApplicationRequestMapper {
    @Mappings({
            @Mapping(source = "passportSeries", target = "passportId.series"),
            @Mapping(source = "passportNumber", target = "passportId.number"),
    })
    Client toClient(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanOffer toOfferJsonb(LoanOfferDTO loanOfferDTO);

    @Mappings({
            @Mapping(source = "employmentStatus", target = "status")
    })
    Employment toEmploymentJsonb(EmploymentDTO employmentDTO);

    @Mappings({
            @Mapping(source = "isInsuranceEnabled", target = "insuranceEnable"),
            @Mapping(source = "isSalaryClient", target = "salaryClient"),
            @Mapping(source = "psk", target = "psk"),
    })
    Credit toCredit(CreditDTO creditDTO);

    PaymentScheduleElement toSchedule(PaymentScheduleElementDTO paymentScheduleElementDTO);

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
