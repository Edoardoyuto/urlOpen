package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.TeacherStudentLink;
import com.example.demo.form.InviteCodeForm;
import com.example.demo.repository.TeacherStudentLinkRepository; // ★ 忘れずimport！
import com.example.demo.service.InviteCodeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GuestMenuController {

    private final HttpSession session;
    private final InviteCodeService inviteCodeService;
    private final TeacherStudentLinkRepository teacherStudentLinkRepository; // ★これ！

    @GetMapping("/guestMenu")
    public String view(Model model) {
        String studentId = (String) session.getAttribute("loginId");
        
        // 生徒に紐づいてる先生一覧を取得
        List<TeacherStudentLink> links = teacherStudentLinkRepository.findByStudentId(studentId);
        
        model.addAttribute("teacherLinks", links);
        model.addAttribute("inviteCodeForm", new InviteCodeForm()); // ★これも追加しとくといい
        return "guestMenu";
    }

    @PostMapping("/invite-code/join")
    public String joinWithInviteCode(@ModelAttribute InviteCodeForm form, Model model) {
        String studentId = getCurrentLoginUserId();
        boolean success = inviteCodeService.bindStudentToTeacher(form.getCode(), studentId);

        if (success) {
            model.addAttribute("message", "先生との紐づけに成功しました！");
        } else {
            model.addAttribute("message", "招待コードが無効か期限切れです。");
        }
        return "redirect:/guestMenu"; // ★ここリダイレクトにするとより安全
    }

    private String getCurrentLoginUserId() {
        return (String) session.getAttribute("loginId");
    }
}
