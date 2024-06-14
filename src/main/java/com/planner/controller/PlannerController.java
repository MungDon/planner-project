package com.planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlannerController {

	@GetMapping("/planner/main")
	public String main() {
		return"main";
	}
}
