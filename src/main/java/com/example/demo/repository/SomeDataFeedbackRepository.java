package com.example.demo.repository;

import com.example.demo.entity.SomeDataFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SomeDataFeedbackRepository extends JpaRepository<SomeDataFeedback, Long> {
}
