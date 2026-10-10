package com.FindAJob.demo.companies.responses;

import com.FindAJob.demo.companies.domain.entities.Companies;

public record CompResponseDTO(String comp_name,
                              String location,
                              String comp_email) {

    public static CompResponseDTO from(Companies company){
        return new CompResponseDTO(
                company.getComp_name(),
                company.getLocation(),
                company.getCompEmail()
        );

    }

}
