package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repository.TeacherStudentLinkRepository;
import com.example.demo.service.InviteCodeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InviteCodeController {

    private final InviteCodeService inviteCodeService;
    private final HttpSession session; // コンストラクタインジェクション
    private final TeacherStudentLinkRepository teacherStudentLinkRepository;

    @PostMapping("/invite-code/generate")
    public String generateInviteCode(Model model) {
        String teacherId = getCurrentLoginUserId();
        String code = inviteCodeService.createOrGetInviteCode(teacherId);

        model.addAttribute("inviteCode", code);

        // ★ここを追加！！ 生徒一覧も一緒に取得して渡す
        var students = teacherStudentLinkRepository.findByTeacherId(teacherId);
        model.addAttribute("students", students);

        return "hostMenu";
    }
    
    public String getCurrentLoginUserId() {
        return (String) session.getAttribute("loginId");
    }
}
