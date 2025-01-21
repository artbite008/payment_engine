package com.siupay.core.dto;

import com.siupay.common.api.dto.request.BasePaginationRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminQueryHistoryRequest extends BasePaginationRequest {

    private AdminPayinFilterDto filter;
}
