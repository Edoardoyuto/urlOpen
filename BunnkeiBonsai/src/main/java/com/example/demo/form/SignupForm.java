package com.example.demo.form;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * ユーザー登録画面フォーム
 */

@Data
public class SignupForm {
	
	@Length(min=4,max=20)
	private String loginId;
	
	@Length(min=5,max=20)
	private String password;
	
	private boolean isHost;
}
