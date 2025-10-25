package ru.arkhipov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank
    @Length(max = 32)
    private String uid;

    @NotBlank
    @Length(max = 32)
    private String operationUid;

    private Systems systemName;

    @NotBlank
    private String systemTime;

    private String source;

    private Positions position;

    private double salary;

    private double bonus;

    @Range(min = 0, max = 366)
    private int workDays;

    @Range(min = 1, max = 100_000)
    private int communicationId;

    private int templateId;

    private int productCode;

    private int smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position=" + position.toString() +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", workDays=" + workDays +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}