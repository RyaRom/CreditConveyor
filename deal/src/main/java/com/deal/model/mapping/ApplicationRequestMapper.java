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
            @Mapping(source = "firstName", target = "first_name"),
            @Mapping(source = "lastName", target = "last_name"),
            @Mapping(source = "middleName", target = "middle_name"),
            @Mapping(source = "birthdate", target = "birth_date"),
            @Mapping(source = "passportSeries", target = "passport_id.series"),
            @Mapping(source = "passportNumber", target = "passport_id.number"),
    })
    Client toClient(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanOffer toOfferJsonb(LoanOfferDTO loanOfferDTO);

    @Mappings({
            @Mapping(source = "employmentStatus", target = "status"),
            @Mapping(source = "employerINN", target = "employment_inn"),
            @Mapping(source = "workExperienceTotal", target = "work_experience_total"),
            @Mapping(source = "workExperienceCurrent", target = "work_experience_current")
    })
    Employment toEmploymentJsonb(EmploymentDTO employmentDTO);

    @Mappings({
            @Mapping(source = "monthlyPayment", target = "monthly_payment"),
            @Mapping(source = "isInsuranceEnabled", target = "insurance_enable"),
            @Mapping(source = "isSalaryClient", target = "salary_client"),
            @Mapping(source = "paymentSchedule", target = "payment_schedule")
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
            @Mapping(source = "application.client_id.first_name", target = "firstName"),
            @Mapping(source = "application.client_id.middle_name", target = "middleName"),
            @Mapping(source = "application.client_id.last_name", target = "lastName"),
            @Mapping(source = "application.client_id.birth_date", target = "birthdate"),
            @Mapping(source = "application.client_id.passport_id.series", target = "passportSeries"),
            @Mapping(source = "application.client_id.passport_id.number", target = "passportNumber"),
            @Mapping(source = "application.applied_offer.isInsuranceEnabled", target = "isInsuranceEnabled"),
            @Mapping(source = "application.applied_offer.isSalaryClient", target = "isSalaryClient")
    })
    ScoringDataDTO mapScoringData(FinishRegistrationRequestDTO request, Application application);
}
