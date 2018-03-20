package ClaimCalculator;


/**
 * Functional class for all calculations needed by the claim calculator.
 */
public class CrossoverCalculator {

    /**
     * Calculates the Medicare paid per diem from stored input element values
     * and returns as a double.
     * @return Returns the calculation of Medicare paid per diem as a double.
     */
    public static double getMedicarePaidPerDiem(
            Integer coinsuranceDays, Integer coveredDays,
            Double medicarePaidAmount,
            Double medicareRate,
            Double medicareSequestrationAmount)
    {
        return (medicarePaidAmount +
                medicareSequestrationAmount +
                (coinsuranceDays * medicareRate)) /
                coveredDays;
    }

    /**
     * Calculates the total secondary liability owed from stored input element values
     * and returns as a double.
     * @return the calculation of secondary liability as a double.
     */
    public static double getNetOwed(
            Integer coinsuranceDays, Integer coveredDays,
            Double medicaidRate, Double medicarePaidAmount,
            Double medicareRate, Double medicareSequestrationAmount
    ) {
        return (medicaidRate -
                getMedicarePaidPerDiem(
                        coinsuranceDays, coveredDays,
                        medicarePaidAmount, medicareSequestrationAmount,
                        medicareRate)
                + medicareRate) * coinsuranceDays;
    }


}
