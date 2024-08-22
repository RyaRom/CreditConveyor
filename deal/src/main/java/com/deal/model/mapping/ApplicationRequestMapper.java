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
            @Mapping(source = "application.applied_offer.requestedAmount", target = "amount"),
            @Mapping(source = "application.applied_offer.term", target = "term"),
            @Mapping(source = "application.clientId.first_name", target = "firstName"),
            @Mapping(source = "application.clientId.middle_name", target = "middleName"),
            @Mapping(source = "application.clientId.last_name", target = "lastName"),
            @Mapping(source = "application.clientId.birth_date", target = "birthdate"),
            @Mapping(source = "application.clientId.passportId.series", target = "passportSeries"),
            @Mapping(source = "application.clientId.passportId.number", target = "passportNumber"),
            @Mapping(source = "application.applied_offer.isInsuranceEnabled", target = "isInsuranceEnabled"),
            @Mapping(source = "application.applied_offer.isSalaryClient", target = "isSalaryClient")
    })
    ScoringDataDTO mapScoringData(FinishRegistrationRequestDTO request, Application application);
}
