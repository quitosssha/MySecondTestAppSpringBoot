package ru.arkhipov.MySecondTestAppSpringBoot.model;

import lombok.Getter;

@Getter
public enum Positions {

    DEV(2.2),
    HR(1.2),
    TL(2.6),
    PO(5.2),
    TPM(2.8),
    CTO(3.5);

    private final double positionCoefficient;
    private final boolean isManager;

    Positions(double positionCoefficient){
        this.positionCoefficient = positionCoefficient;
        isManager = positionCoefficient == 2.6 || positionCoefficient == 2.8;
    }

}
