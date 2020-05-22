package com.microservices.alexandrebn.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.microservices.alexandrebn.core.model.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

}
