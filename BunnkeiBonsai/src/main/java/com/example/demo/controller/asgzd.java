package com.example.demo.controller;

import com.example.demo.entity.TeacherStudentLink;
import com.example.demo.entity.Url;
import com.example.demo.repository.TeacherStudentLinkRepository;
import com.example.demo.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class asgzd {

    private final HttpSession session;
    private final TeacherStudentLinkRepository teacherStudentLinkRepository;
    private final UrlRepository urlRepository;

    @GetMapping("/api/url/list")
    public String getUrlsForStudent() {
        String studentId = (String) session.getAttribute("loginId");
        if (studentId == null) return "";

        // 紐づけされている先生の一覧
        List<String> teacherIds = teacherStudentLinkRepository.findByStudentId(studentId).stream()
                .filter(TeacherStudentLink::isEnabled)
                .map(TeacherStudentLink::getTeacherId)
                .toList();

        // 各先生が登録した有効なURLを取得
        List<Url> urls = teacherIds.stream()
                .flatMap(tid -> urlRepository.findByTeacherId(tid).stream())
                .filter(Url::getIsActive)
                .toList();

        return urls.stream()
                .map(Url::getUrl)
                .collect(Collectors.joining(","));
    }
}
