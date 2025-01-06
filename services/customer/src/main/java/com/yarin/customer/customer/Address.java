package com.yarin.customer.customer;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
@Validated
public class Address {
    private String cityName;
    private String streetName;
    private String houseNum;
}
