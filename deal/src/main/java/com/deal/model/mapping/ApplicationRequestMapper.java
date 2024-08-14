package com.deal.model.mapping;

import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.entities.Client;
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

}
