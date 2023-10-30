package com.example.demo.handler;

import com.example.demo.entity.SomeDataFeedback;
import com.example.demo.repository.SomeDataFeedbackRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Example;

import java.util.concurrent.atomic.AtomicInteger;

public class SomeDataFeedbackReader implements ItemReader<SomeDataFeedback> {

    // Implement the logic to read items here
    // and return the next item in each call to read() method
    private final SomeDataFeedbackRepository SomeDataFeedbackRepository;

    private final AtomicInteger batchRunCounter = new AtomicInteger(0);

    public SomeDataFeedbackReader(SomeDataFeedbackRepository SomeDataFeedbackRepository) {
        this.SomeDataFeedbackRepository = SomeDataFeedbackRepository;
    }

    @Override
    public SomeDataFeedback read() throws Exception {
        var dataList = SomeDataFeedbackRepository.findAll(Example.of(SomeDataFeedback.builder().status(false).build()));

        if (batchRunCounter.get() < dataList.size()) {
            return dataList.get(batchRunCounter.getAndIncrement());
        } else {
            batchRunCounter.set(0);
            return null;
        }
    }
}