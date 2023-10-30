package com.example.demo;

import com.example.demo.entity.SomeDataFeedback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private static Collection<SomeDataFeedback> prepareTestData(final int amount) {
        final int actualYear = new GregorianCalendar().get(Calendar.YEAR);
        final Collection<SomeDataFeedback> customers = new LinkedList<>();
        for (int i = 1; i <= amount; i++) {
            final Calendar birthday = new GregorianCalendar();
            birthday.set(Calendar.YEAR, random(actualYear - 100, actualYear));
            birthday.set(Calendar.DAY_OF_YEAR, random(1, birthday.getActualMaximum(Calendar.DAY_OF_YEAR)));
            final SomeDataFeedback customer = SomeDataFeedback.builder().build();
            customer.setId((long) i);
            customer.setName(UUID.randomUUID().toString().replaceAll("[^a-z]", ""));
            customers.add(customer);
        }

        return customers;
    }

    private static int random(final int start, final int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}
