package com.dossier.service;

import com.dossier.model.dto.ClientDTO;
import com.dossier.model.dto.CreditDTO;
import com.dossier.model.dto.PaymentScheduleElementDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    public File createDocument(String template, CreditDTO credit, ClientDTO client) {
        String content = template;

        content = content.replace("{amount}", credit.getAmount().toString())
                .replace("{term}", credit.getTerm().toString())
                .replace("{monthlyPayment}", credit.getMonthlyPayment().toString())
                .replace("{psk}", credit.getPsk().toString())
                .replace("{insuranceEnable}", credit.getIsInsuranceEnabled().toString())
                .replace("{salaryClient}", credit.getIsSalaryClient().toString())

                .replace("{clientId}", client.getClientId().toString())
                .replace("{lastName}", client.getLastName())
                .replace("{firstName}", client.getFirstName())
                .replace("{middleName}", client.getMiddleName())
                .replace("{birthdate}", client.getBirthdate().toString())
                .replace("{email}", client.getEmail())
                .replace("{gender}", client.getGender().name())
                .replace("{maritalStatus}", client.getMaritalStatus().name())
                .replace("{dependentAmount}", client.getDependentAmount().toString())

                .replace("{passportSeries}", client.getPassport().series())
                .replace("{passportNumber}", client.getPassport().number())
                .replace("{passportIssueBranch}", client.getPassport().issueBranch())
                .replace("{passportIssueDate}", client.getPassport().issueDate().toString())

                .replace("{employmentStatus}", client.getEmployment().status().name())
                .replace("{employmentInn}", client.getEmployment().employmentInn())
                .replace("{employmentSalary}", client.getEmployment().salary().toString())
                .replace("{employmentPosition}", client.getEmployment().position().name())
                .replace("{employmentWorkExperienceTotal}", client.getEmployment().workExperienceTotal().toString())
                .replace("{employmentWorkExperienceCurrent}", client.getEmployment().workExperienceCurrent().toString())

                .replace("{paymentSchedule}", formatPaymentSchedule(credit.getPaymentSchedule()));

        log.info("document created {}", content);
        File outputFile = File.createTempFile("CreditContract-", "");
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(content);
        }
        outputFile.renameTo(new File(outputFile.getParentFile(), "CreditContract.txt"));

        return outputFile;
    }

    private String formatPaymentSchedule(List<PaymentScheduleElementDTO> schedule) {
        return schedule.stream()
                .map(e -> String.format("Payment %d: Date: %s, Total Payment: %s, Interest Payment: %s, Debt Payment: %s, Remaining Debt: %s",
                        e.getNumber(), e.getDate(), e.getTotalPayment(), e.getInterestPayment(), e.getDebtPayment(), e.getRemainingDebt()))
                .collect(Collectors.joining("\n"));
    }
}
