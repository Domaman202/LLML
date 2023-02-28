package ru.DmN.llml.precompiler;

public class CalculationOptions {
    /**
     * Two Argument Math Type Calc
     */
    public final boolean tamtc;
    /**
     * Types Calculated
     */
    public final boolean tc;

    /**
     * @param oamtc Two Argument Math Type Calc
     * @param tc Types Calculated
     */
    public CalculationOptions(boolean oamtc, boolean tc) {
        this.tamtc = oamtc;
        this.tc = tc;
    }
}
