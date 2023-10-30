package com.example.demo.handler;

import com.example.demo.entity.SomeDataFeedback;
import com.example.demo.repository.SomeDataFeedbackRepository;
import com.example.demo.repository.SomeDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SomeDataFeedbackHandler {

    private final JobBuilderFactory jobBuilderFactory;


    private final StepBuilderFactory stepBuilderFactory;


    private final SomeDataFeedbackRepository someDataFeedbackRepository;

    private final SomeDataRepository someDataRepository;


    private final JobLauncher jobLauncher;

    public Job feedbackJob() {
        return jobBuilderFactory.get("feedbackJob")
                .start(myStep())
                .build();
    }

    public Step myStep() {
        return stepBuilderFactory.get("myStep")
                .<SomeDataFeedback, SomeDataFeedback>chunk(100)
                .reader(new SomeDataFeedbackReader(someDataFeedbackRepository)) // Use MyItemReader as the reader
                .processor(getItemProcessor(someDataFeedbackRepository))
                .writer(getItemWriter())
                .build();
    }

    private static ItemProcessor<SomeDataFeedback, SomeDataFeedback> getItemProcessor(SomeDataFeedbackRepository someDataFeedbackRepository) {
        return SomeDataFeedback -> {
            log.info("Processor item: " + SomeDataFeedback);
            SomeDataFeedback.setStatus(true);
            someDataFeedbackRepository.save(SomeDataFeedback);
            return SomeDataFeedback;
        };
    }

    private static ItemWriter<SomeDataFeedback> getItemWriter() {
        return list -> {
            for (SomeDataFeedback SomeDataFeedback : list) {
                log.info("Write {}", SomeDataFeedback);
            }
        };
    }

    @Scheduled(fixedRate = 20000)
    public void runBatchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(feedbackJob(), jobParameters);
    }
}
