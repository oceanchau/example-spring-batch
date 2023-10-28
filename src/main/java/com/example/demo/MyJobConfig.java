package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class MyJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SomeDataRepository someDataRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job myJob() {
        return jobBuilderFactory.get("myJob")
                .start(myStep())
                .build();
    }

    @Bean
    public Step myStep() {
        log.info("Starting myStep");

        return stepBuilderFactory.get("myStep")
                .<SomeData, SomeData>chunk(100)
                .reader(new MyItemReader(someDataRepository)) // Use MyItemReader as the reader
                .processor(getItemProcessor(someDataRepository))
                .writer(getItemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    private static ItemProcessor<SomeData, SomeData> getItemProcessor(SomeDataRepository someDataRepository) {
        return someData -> {
            log.info("Processor item: " + someData);
            someData.setStatus(true);
            someDataRepository.save(someData);
            return someData;
        };
    }

    private ItemReader<SomeData> getItemReader() {
        var dataIterator = someDataRepository.findAll(Example.of(SomeData.builder().status(false).build())).iterator();
        return () -> {
            if (dataIterator.hasNext()) {
                return dataIterator.next();
            } else {
                log.info("hahahah");
                return null;
            }
        };
    }

    private static ItemWriter<SomeData> getItemWriter() {
        return list -> {
            for (SomeData someData : list) {
                log.info("Write {}", someData);
            }
        };
    }

    @Scheduled(fixedRate = 20000)
    public void runBatchJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(myJob(), jobParameters);
    }
}
