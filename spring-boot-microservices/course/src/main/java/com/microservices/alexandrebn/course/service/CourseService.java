package com.microservices.alexandrebn.course.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservices.alexandrebn.core.model.Course;
import com.microservices.alexandrebn.core.repository.CourseRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepository;
	
	private Logger log = LoggerFactory.getLogger(CourseService.class);
	
	public Iterable<Course> list(Pageable pageable) {
		log.info("listing the courses");
		return courseRepository.findAll();
	}
}
