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

        content = content.replace("{amount}", credit.amount().toString())
                .replace("{term}", credit.term().toString())
                .replace("{monthlyPayment}", credit.monthlyPayment().toString())
                .replace("{psk}", credit.psk().toString())
                .replace("{insuranceEnable}", credit.isInsuranceEnabled().toString())
                .replace("{salaryClient}", credit.isSalaryClient().toString())

                .replace("{clientId}", client.clientId().toString())
                .replace("{lastName}", client.lastName())
                .replace("{firstName}", client.firstName())
                .replace("{middleName}", client.middleName())
                .replace("{birthdate}", client.birthdate().toString())
                .replace("{email}", client.email())
                .replace("{gender}", client.gender().name())
                .replace("{maritalStatus}", client.maritalStatus().name())
                .replace("{dependentAmount}", client.dependentAmount().toString())

                .replace("{passportSeries}", client.passport().series())
                .replace("{passportNumber}", client.passport().number())
                .replace("{passportIssueBranch}", client.passport().issueBranch())
                .replace("{passportIssueDate}", client.passport().issueDate().toString())

                .replace("{employmentStatus}", client.employment().status().name())
                .replace("{employmentInn}", client.employment().employmentInn())
                .replace("{employmentSalary}", client.employment().salary().toString())
                .replace("{employmentPosition}", client.employment().position().name())
                .replace("{employmentWorkExperienceTotal}", client.employment().workExperienceTotal().toString())
                .replace("{employmentWorkExperienceCurrent}", client.employment().workExperienceCurrent().toString())

                .replace("{paymentSchedule}", formatPaymentSchedule(credit.paymentSchedule()));

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
                        e.number(),
                        e.date(),
                        e.totalPayment(),
                        e.interestPayment(),
                        e.date(),
                        e.remainingDebt()))
                .collect(Collectors.joining("\n"));
    }
}
