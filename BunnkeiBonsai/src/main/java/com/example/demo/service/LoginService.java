package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor /*finalがついたフィールドに対応するコンストラクタを自動生成する（依存注入のため）*/
public class LoginService {
	/*ユーザ情報テーブルDAO*/
	private final UserInfoRepository repository;
	/*
	 * ユーザー情報テーブル　主キー検索
	 * @Param loginId
	 * @return ユーザー情報テーブルを首位ー検索した結果(1件)
	 */
	
	public Optional<UserInfo> searchUserId(String loginId){
		return repository.findById(loginId);
	}
}


