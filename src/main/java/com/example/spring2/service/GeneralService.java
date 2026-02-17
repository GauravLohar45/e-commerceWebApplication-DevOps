package com.example.spring2.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.spring2.config.AES;
import com.example.spring2.entity.Customer;
import com.example.spring2.entity.Product;
import com.example.spring2.repository.CustomerRepository;
import com.example.spring2.repository.ProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class GeneralService {

//	private final GeneralController generalController;

	@Value("${admin.email}")
	private String adminEmail;
	@Value("${admin.password}")
	private String adminPassword;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	JavaMailSender sender;

//	GeneralService(GeneralController generalController) {
//		this.generalController = generalController;
//	}

	public String login(String email, String password, HttpSession session) {
		if (email.equals(adminEmail) && password.equals(adminPassword)) {
			session.setAttribute("admin", "admin");
			session.setAttribute("pass", "Login Success, Welcome Admin");
			return "redirect:/admin/home";
		} else {
			Customer customer = customerRepository.findByEmail(email);
			if (customer == null) {
				session.setAttribute("fail", "Invalid Email");
				return "redirect:/login";
			} else {
				if (AES.decrypt(customer.getPassword()).equals(password)) {
					session.setAttribute("pass", "Login Success, Welcome " + customer.getName());
					session.setAttribute("customer", customer);
					return "redirect:/customer/home";
				} else {
					session.setAttribute("fail", "Invalid Password");
					return "redirect:/login";
				}
			}
		}
	}

	public void removeMessage() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession(true);
		session.removeAttribute("pass");
		session.removeAttribute("fail");
	}

	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		session.setAttribute("fail", "Logout Success");
		return "redirect:/";
	}

	public String loadMainPage(ModelMap map) {
		List<Product> products = productRepository.findAll();
		map.put("products", products);
		return "main";
	}

	public String resetPassword(String email, String newPassword, String reEnteredPassword, HttpSession session) {
		Customer customer = customerRepository.findByEmail(email);
		String oldPassword = AES.decrypt(customer.getPassword());
		if (oldPassword.equals(newPassword)) {
			session.setAttribute("fail", "Password already existed");
			return "redirect:/reset-password";
		} else {
			if (newPassword.equals(reEnteredPassword)) {
				session.setAttribute("pass", "password updated successfully !!!");
				// oldPassword=newPassword;
				customer.setPassword(AES.encrypt(newPassword));
				customerRepository.save(customer);
				return "redirect:/login";
			} else {
				session.setAttribute("fail", "Enter password correctly");
				return "redirect:/reset-password";
			}
		}
	}

	public String verifyDetails(String email, HttpSession session) {
		Customer customer = customerRepository.findByEmail(email);
		if (customer.getEmail().equals(email)) {
			int otp = new Random().nextInt(100000, 1000000);
			session.setAttribute("otp", otp);
			session.setAttribute("update-password", customer);
			sendOtp(otp, customer.getEmail());
			session.setAttribute("pass", "Otp sent successfully");
			return "redirect:/forget-password-otp";
		} else {
			session.setAttribute("fail", "Please enter valid email");
			return "redirect:/forget-password";
		}

	}

	public void sendOtp(int otp, String email) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("OTP Verification");
		message.setText("Your OTP is "+otp+" ,Enter OTP to update password");
		try {
			sender.send(message);
		}catch(MailAuthenticationException e) {
			System.err.println("Sending Mail Failed but OTP is: "+otp);
		}
	}

	public String checkOtp(int otp, HttpSession session) {
		return null;
	}

}