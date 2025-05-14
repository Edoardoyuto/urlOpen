package com.example.demo.controller;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.service.SignupService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録画面コントローラー
 */
@Controller
@RequiredArgsConstructor
public class SignupController {
	
	/*ユーザー登録画面サービス*/
	public final SignupService  service;
	
	/*message sorce*/
	private final MessageSource messageSource;
	
	@GetMapping("/signup")
	public String view(Model model,SignupForm form) {
		model.addAttribute("signupForm", form); // ←これが必要
		return "signup";
	}
	
	
	/**
	 * ユーザー登録
	 * @param model
	 * @param form
	 *  @param bdResult 入力チェック結果
	 * @return 表示画面
	 */
	@PostMapping("/signup")
	public void signup(Model model,@Validated SignupForm form,BindingResult bdResult) {
		
		if(bdResult.hasErrors()) {
			return;
		}
		var userInfoOpt=service.resistUserInfo(form);
		
		var errorMsg = AppUtil.getMessage(messageSource,judgeMessageKey(userInfoOpt));
		model.addAttribute("message",errorMsg);
		
	}
	/**
	 * ユーザー情報の結果メッセージキーを判断する。
	 * @param userInfoOpt　ユーザー登録結果（登録済みだった場合はEmpty）
	 * @return　メッセージキー
	 */
	private String judgeMessageKey(Optional<UserInfo> userInfoOpt) {
		if(userInfoOpt.isEmpty()) {
			System.out.println("登録被り");
			return MessageConst.SIGNUP_EXISTED_LOGINID;
		}else {
			return MessageConst.SIGNUP_RESIST_SUCCEED;
		}
	}


}									