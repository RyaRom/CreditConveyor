package com.deal.model.mapping;

import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.entities.Client;
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
            @Mapping(target = "client_id", ignore = true),
            @Mapping(target = "gender", ignore = true),
            @Mapping(target = "marital_status", ignore = true),
            @Mapping(target = "dependent_amount", ignore = true),
            @Mapping(target = "employment_id", ignore = true),
            @Mapping(target = "account", ignore = true)
    })
    Client toClient(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
