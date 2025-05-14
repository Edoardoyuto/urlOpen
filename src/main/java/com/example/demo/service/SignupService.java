package com.example.demo.service;

import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor /*finalがついたフィールドに対応するコンストラクタを自動生成する（依存注入のため）*/
public class SignupService {
	/*ユーザ情報テーブルDAO*/
	private final UserInfoRepository repository;
	/*Dozer Mapper*/
	private final Mapper mapper;
	/**PasswordEncoder*/
	private final PasswordEncoder passwordEncoder;
	

	/*
	 * ユーザー情報テーブル　新規登録
	 * @Param 入力情報
	 * @return 登録情報（ユーザー情報エンティティ）	すでにユーザーIDに登録がある場合はempty */
	
	public Optional<UserInfo> resistUserInfo(SignupForm form){ 
		
		var UserInfoExistedOpt = repository.findById(form.getLoginId());
		if(UserInfoExistedOpt.isPresent()) {
			return Optional.empty();
		}
		
		System.out.println("Host:"+ form.isHost());
		
		var userInfo = mapper.map(form,UserInfo.class);
		var encordedPassword = passwordEncoder.encode(form.getPassword());
		userInfo.setPassword(encordedPassword);
		/*この方法で、データベースに保存するが、項目が多くなると大変なのでdozerフレームワークを使っている
		 * var userInfo = new UserInfo();
		 
		userInfo.setLoginId(form.getLoginId());
		userInfo.setPassword(form.getPassword());
		*/
		return Optional.of(repository.save(userInfo));
	}
}


