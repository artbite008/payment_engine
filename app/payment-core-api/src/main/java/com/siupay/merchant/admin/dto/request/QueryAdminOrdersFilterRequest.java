package com.siupay.merchant.admin.dto.request;

import com.siupay.common.api.dto.request.BasePaginationRequest;
import com.siupay.merchant.admin.dto.AdminOrderFilterDto;
import lombok.Data;

/**
 * 
 * @Date: 2022/8/23 21:03
 */
@Data
public class QueryAdminOrdersFilterRequest extends BasePaginationRequest {

	private AdminOrderFilterDto filter;
}
