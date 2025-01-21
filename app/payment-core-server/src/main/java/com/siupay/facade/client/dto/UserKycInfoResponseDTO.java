package com.siupay.facade.client.dto;

import lombok.Data;

/**
 * @author Sucre
 * @date 2022年01月26日
 */
@Data
public class UserKycInfoResponseDTO {

    /**
     * first name
     */
    private String firstName;

    /**
     * last name
     */
    private String lastName;
}
