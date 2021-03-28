package com.helinfengxs.Excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelEntity {
    //设置对应列属性
    @ExcelProperty(index = 0)
    private String caseSutiesName;
    @ExcelProperty(index = 1)
    private Integer caseNum;
    @ExcelProperty(index = 2)
    private String caseName;
    @ExcelProperty(index = 3)
    private String requestAddr;
    @ExcelProperty(index = 4)
    private String requestType;
    @ExcelProperty(index = 5)
    private String headers;
    @ExcelProperty(index = 6)
    private String paramsType;
    @ExcelProperty(index = 7)
    private String params;
    @ExcelProperty(index = 8)
    private Integer statusCode;
    @ExcelProperty(index = 9)
    private String sql;
    @ExcelProperty(index = 10)
    private String expectedResults;
    @ExcelProperty(index = 11)
    private String retrunResult;
    @ExcelProperty(index = 12)
    private String runResult;
    @ExcelProperty(index = 13)
    private String testTime;

    @Override
    public String toString() {
        return "{" +
                "caseSutiesName:'" + caseSutiesName + '\'' +
                ", caseNum:'" + caseNum + '\'' +
                ", caseName:'" + caseName +'\'' +
                ", requestAddr:'" + requestAddr + '\'' +
                ", requestType:'" + requestType + '\'' +
                ", headers:'" + headers + '\'' +
                ", paramsType:'" + paramsType + '\'' +
                ", params:'" + params + '\'' +
                ", statusCode:" + statusCode +
                ", sql:'" + sql + '\'' +
                ", expectedResults:'" + expectedResults + '\'' +
                ", retrunResult:'" + retrunResult + '\'' +
                ", runResult:'" + runResult + '\'' +
                ", testTime:'" + testTime + '\'' +
                '}';
    }
}
