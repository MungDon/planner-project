package com.planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

	/*전역 예외 핸들러에서 리다이렉트로 보내는 매핑*/
	@GetMapping("/error")
	public String customErrors(@RequestParam(value =  "errorMessage")String errorMessage,Model model) {
		System.out.println(errorMessage);
		model.addAttribute("errorMessage",errorMessage); // 매개변수로 받은 예외객체를 뷰로 errorMessage라는 이름으로 보내준다.
		return "error";
	}
}
