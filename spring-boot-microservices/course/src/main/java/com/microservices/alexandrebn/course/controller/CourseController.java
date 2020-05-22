package com.microservices.alexandrebn.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.alexandrebn.core.model.Course;
import com.microservices.alexandrebn.course.service.CourseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/admin/courses")
@Api(value= "Endpoints to manage courses")
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "List all available courses", response = Course[].class)
	public ResponseEntity<Iterable<Course>> list(Pageable pageable) {
		final Iterable<Course> courses = courseService.list(pageable);
		return ResponseEntity.ok(courses);
	}
}
