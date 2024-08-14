package com.deal.model.mapping;

import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.entities.Client;
import com.deal.model.json.Passport;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
@Component
public class ApplicationRequestMapper {
//    @Mappings({
//            @Mapping(source = "firstName", target = "first_name"),
//            @Mapping(source = "lastName", target = "last_name"),
//            @Mapping(source = "middleName", target = "middle_name"),
//            @Mapping(source = "birthdate", target = "birth_date"),
//            @Mapping(source = "passportSeries", target = "passport_id.series"),
//            @Mapping(source = "passportNumber", target = "passport_id.number"),
//    })
    public Client toClient(LoanApplicationRequestDTO loanApplicationRequestDTO){
        Client client = new Client();
        client.setFirst_name(loanApplicationRequestDTO.getFirstName());
        client.setMiddle_name(loanApplicationRequestDTO.getMiddleName());
        client.setLast_name(loanApplicationRequestDTO.getLastName());
        client.setBirth_date(loanApplicationRequestDTO.getBirthdate());
        Passport passport = new Passport();
        passport.setNumber(loanApplicationRequestDTO.getPassportNumber());
        passport.setSeries(loanApplicationRequestDTO.getPassportSeries());
        client.setPassport_id(passport);
        return client;
    }
}
