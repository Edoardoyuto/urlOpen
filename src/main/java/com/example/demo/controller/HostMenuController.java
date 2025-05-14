package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.TeacherStudentLink;
import com.example.demo.entity.Url;
import com.example.demo.repository.TeacherStudentLinkRepository;
import com.example.demo.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HostMenuController {

    private final TeacherStudentLinkRepository teacherStudentLinkRepository;
    private final UrlRepository urlRepository; // ★追加

    @GetMapping("/hostMenu")
    public String hostMenu(Model model, HttpSession session) {
        String teacherId = (String) session.getAttribute("loginId");

        List<TeacherStudentLink> students = teacherStudentLinkRepository.findByTeacherId(teacherId);
        List<Url> urls = urlRepository.findByTeacherId(teacherId).stream()
        	    .filter(Url::getIsActive)
        	    .toList();
        model.addAttribute("students", students);
        model.addAttribute("urls", urls); // ★追加
        model.addAttribute("user", teacherId);

        return "hostMenu";
    }

    // 生徒の送信対象更新処理（既存のままでOK）
    @PostMapping("/student/update-enabled")
    public String updateStudentEnabled(@RequestParam("id") String id, @RequestParam(value = "isEnabled", required = false) String isEnabled) {
        var linkOpt = teacherStudentLinkRepository.findById(id);
        if (linkOpt.isPresent()) {
            var link = linkOpt.get();
            link.setEnabled(isEnabled != null);
            teacherStudentLinkRepository.save(link);
        }
        return "redirect:/hostMenu";
    }
    
    @Transactional
    @PostMapping("/student/update-enabled-api")
    public ResponseEntity<String> updateStudentEnabledApi(@RequestParam("id") String id,
                                                          @RequestParam(value = "isEnabled", required = false) String isEnabled) {
        System.out.println("✅ リクエスト受信: id=" + id + ", isEnabled=" + isEnabled);

        var linkOpt = teacherStudentLinkRepository.findById(id);
        if (linkOpt.isPresent()) {
            var link = linkOpt.get();
            boolean newStatus = "on".equals(isEnabled); // ← ここ修正！
            System.out.println("▶ 旧ステータス: " + link.isEnabled() + " → 新: " + newStatus);
            link.setEnabled(newStatus);
            teacherStudentLinkRepository.save(link);
            System.out.println("✅ 保存完了 → id: " + id + ", enabled: " + link.isEnabled());
            return ResponseEntity.ok("OK");
        } else {
            System.out.println("❌ 該当リンクなし → id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }
    
}
