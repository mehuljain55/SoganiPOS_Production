package com.Soganis.ScheduledService;

import com.Soganis.Service.TransactionDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransactionDailyScheduler {

    @Autowired
    private TransactionDailyService transactionDailyService;

    // Schedule the task to run every day at 10 PM IST
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Kolkata")
    public void scheduleDailyTransactionTask() {
        transactionDailyService.addTransactionDailyRecord();
    }
}
