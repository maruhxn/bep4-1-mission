package com.back.bounded_context.payout.in;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * 하루에 여러 번 실행하는 이유: 한 번 실패해도 다음 실행에서 남은 것이 처리된다. 모으기와 집행 둘 다 "아직 처리되지 않은 것만" 가져오기 때문에 여러 번 실행해도 중복 처리되지 않는다.
 */
@Profile("prod")
@Component
@RequiredArgsConstructor
public class PayoutScheduler {
    private final JobOperator jobOperator;
    private final Job payoutCollectItemsAndCompletePayoutsJob;

    // 매일 01:00, 04:00, 22:00 (KST, JVM 기본 타임존)
    @Scheduled(cron = "0 0 1,4,22 * * *")
    public void runPayoutJob() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException,
            JobExecutionAlreadyRunningException, JobRestartException {
        runCollectItemsAndCompletePayoutsBatchJob();
    }

    private void runCollectItemsAndCompletePayoutsBatchJob() throws JobInstanceAlreadyCompleteException,
            InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("runDateTime", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME))
                .toJobParameters();

        jobOperator.start(payoutCollectItemsAndCompletePayoutsJob, jobParameters);
    }
}