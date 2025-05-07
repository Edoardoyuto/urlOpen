package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

@Controller

@RequiredArgsConstructor
public class LoginController {
	public final LoginService  service;
	private final HttpSession session; 
	
	@GetMapping("/login")
	public String view(Model model,LoginForm form) {
		return "login";
	}
	
	/**PasswordEncoder*/
	private final PasswordEncoder passwordEncoder; 
	
	/*message sorce*/
	private final MessageSource messageSource;
	

	
	
	/**
	 * 初期表示
	 * @param model
	 * @param form
	 * @return 表示画面
	 */
	@PostMapping("/login")
	public String login(Model model,LoginForm form) {
		System.out.println(form.toString());
		var encordedPassword = passwordEncoder.encode(form.getPassword());
		System.out.println(encordedPassword);
		var userInfo = service.searchUserId(form.getLoginId());
		boolean isCorrectUserAuth = userInfo.isPresent()
				&&passwordEncoder.matches(form.getPassword(), userInfo.get().getPassword());
		if(isCorrectUserAuth&&userInfo.get().isHost()) {
			session.setAttribute("loginId", form.getLoginId()); 
			return "redirect:/hostMenu";
		}else if(isCorrectUserAuth&&!userInfo.get().isHost()) {
			session.setAttribute("loginId", form.getLoginId());
			return "redirect:/guestMenu";			
		}else {
			var errorMsg = AppUtil.getMessage(messageSource,MessageConst.LOGIN_WRONG_INPUT);
			model.addAttribute("errorMsg",errorMsg);
			return "login";
		}
	}
}									