package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Example;

import java.util.concurrent.atomic.AtomicInteger;

public class MyItemReader implements ItemReader<SomeData> {

    // Implement the logic to read items here
    // and return the next item in each call to read() method
    private final SomeDataRepository someDataRepository;

    public MyItemReader(SomeDataRepository someDataRepository) {
        this.someDataRepository = someDataRepository;
    }
    private AtomicInteger batchRunCounter = new AtomicInteger(0);
    @Override
    public SomeData read() throws Exception {
        var dataList = someDataRepository.findAll(Example.of(SomeData.builder().status(false).build()));

        if (batchRunCounter.get() < dataList.size()) {
            return dataList.get(batchRunCounter.getAndIncrement());
        } else {
            batchRunCounter.set(0);
            return null;
        }
    }
}