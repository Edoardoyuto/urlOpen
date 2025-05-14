package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TeacherStudentLink;
import com.example.demo.entity.Url;
import com.example.demo.repository.TeacherStudentLinkRepository;
import com.example.demo.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class urlApi {

    private final HttpSession session;
    private final TeacherStudentLinkRepository teacherStudentLinkRepository;
    private final UrlRepository urlRepository;

    @GetMapping("/api/url/list")
    public String getUrlsForStudent() {
        String studentId = (String) session.getAttribute("loginId");
        if (studentId == null) return "";

        // 自分（この生徒）が有効なリンクになっている先生IDだけを抽出
        List<String> teacherIds = teacherStudentLinkRepository.findByStudentId(studentId).stream()
                .filter(link -> link.isEnabled()) // この生徒自身に対してenabledなもの
                .map(TeacherStudentLink::getTeacherId)
                .toList();

        // その先生が登録したアクティブなURLを取得
        List<Url> urls = teacherIds.stream()
                .flatMap(tid -> urlRepository.findByTeacherId(tid).stream())
                .filter(Url::getIsActive)
                .toList();

        return urls.stream()
                .map(Url::getUrl)
                .collect(Collectors.joining(","));
    }
}
