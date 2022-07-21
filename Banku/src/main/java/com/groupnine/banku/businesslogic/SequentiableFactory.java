package com.groupnine.banku.businesslogic;

public abstract class SequentiableFactory {
    private int counter = 0;

    private void increaseCounter()
    /* Aumenta o valor do counter. */
    {
        counter += 1;
    }

    protected String getEncodedSequentialId()
    /* Retorna um valor normalizado para ser usado como um
     * id sequencial.
     * */
    {
        increaseCounter();

        String num = String.valueOf(counter);
        double decimalPlaces = Math.pow(10d, 12d);

        for (int x = counter ; x < decimalPlaces ; decimalPlaces /= 10) {
            num = "0".concat(num);
        }

        return num;
    }
}