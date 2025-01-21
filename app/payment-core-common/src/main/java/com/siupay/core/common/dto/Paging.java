package com.siupay.core.common.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siupay.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel("分页结果对象")
public class Paging<T> implements Serializable {
    private static final long serialVersionUID = 4784961132604516495L;

    @ApiModelProperty("总页码数")
    @JSONField(name = CommonConstant.PAGE_TOTAL_NAME)
    @JsonProperty(CommonConstant.PAGE_TOTAL_NAME)
    private long totalPage = 0;

    @ApiModelProperty("总数")
    @JSONField(name = CommonConstant.TOTAL_NUM_NAME)
    @JsonProperty(CommonConstant.TOTAL_NUM_NAME)
    private long totalNum = 0;

    @ApiModelProperty("数据列表")
    @JSONField(name = CommonConstant.PAGE_RECORDS_NAME)
    @JsonProperty(CommonConstant.PAGE_RECORDS_NAME)
    private List<T> records = Collections.emptyList();

    @ApiModelProperty(value = "页码")
    @JSONField(name = CommonConstant.PAGE_INDEX_NAME)
    @JsonProperty(CommonConstant.PAGE_INDEX_NAME)
    private Long pageIndex;

    @ApiModelProperty(value = "页大小")
    @JSONField(name = CommonConstant.PAGE_SIZE_NAME)
    @JsonProperty(CommonConstant.PAGE_SIZE_NAME)
    private Long pageSize;

    public Paging() {

    }
}
