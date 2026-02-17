package com.example.spring2.controller;

//GitHub username: Venkatesh-123456789
//password: Venkatesh@123456789




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring2.entity.Customer;
import com.example.spring2.service.GeneralService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GeneralController {

	@Autowired
	GeneralService generalService;

	@GetMapping("/")
	public String loadMain(ModelMap map) {
		return generalService.loadMainPage(map);
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "login.html";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return generalService.login(email, password, session);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return generalService.logout(session);
	}
	
	@GetMapping("/forgot-password")
	public String forgetPassword() {
		return "forget-password.html";
	}
	
	@PostMapping("/verify-details")
	public String verifyDetails(@RequestParam String email,HttpSession session) {
		//System.out.println("dddddddddddddddddddddddddddddddddddddddddd");
		return generalService.verifyDetails(email,session);
	}
	
	@GetMapping("/forget-password-otp")
	public String loadOtpPage() {
	    return "forget-password-otp.html";
	}
	
	@PostMapping("/check-otp")
	public String checkOtp(@RequestParam int otp,HttpSession session) {
		return generalService.checkOtp(otp,session);
	}
	
	@GetMapping("/reset-password")
	public String loadResetPasswordPage() {
	    return "reset-password.html";
	}

	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String email,@RequestParam String password, @RequestParam String repassword,HttpSession session) {
	    return generalService.resetPassword(email, password, repassword, session);
	}

	

}