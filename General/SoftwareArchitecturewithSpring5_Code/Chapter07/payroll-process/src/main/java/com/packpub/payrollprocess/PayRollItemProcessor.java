package com.packpub.payrollprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PayRollItemProcessor implements ItemProcessor<PayrollTo, PayrollTo> {

    private static final Logger log = LoggerFactory.getLogger(PayRollItemProcessor.class);

    @Override
    public PayrollTo process(PayrollTo payrollTo) throws Exception {

        final PayrollTo resultTransformation = new PayrollTo();
        resultTransformation.setFirstLastName(payrollTo.getFirstLastName().toUpperCase());
        resultTransformation.setDescription(payrollTo.getDescription().toUpperCase());
        resultTransformation.setAccountNumber(payrollTo.getAccountNumber());
        resultTransformation.setAccountType(payrollTo.getAccountType());
        resultTransformation.setCurrency(payrollTo.getCurrency());
        resultTransformation.setIdentification(payrollTo.getIdentification());

        // Data Type Transform
        final double ammountAsNumber = payrollTo.getAmmount().doubleValue();
        resultTransformation.setAmmount(ammountAsNumber);

        log.info("Transforming (" + payrollTo + ") into (" + resultTransformation + ")");
        return resultTransformation;
    }
}
