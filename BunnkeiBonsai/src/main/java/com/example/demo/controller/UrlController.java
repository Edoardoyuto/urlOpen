package com.example.demo.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Url;
import com.example.demo.form.UrlForm;
import com.example.demo.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UrlController {

    private final UrlRepository urlRepository;
    private final HttpSession session;

    /** URL登録画面表示 */
    @GetMapping("/url/register")
    public String showUrlRegisterForm(Model model) {
        model.addAttribute("urlForm", new UrlForm());
        return "urlRegister";
    }

    /** URL登録処理 */
    @PostMapping("/url/register")
    public String registerUrl(@ModelAttribute UrlForm form, Model model) {
        String teacherId = (String) session.getAttribute("loginId");

        Url url = new Url();
        url.setTeacherId(teacherId);
        url.setUrl(form.getUrl());
        url.setLabel(form.getLabel());
        url.setIsActive(true);
        url.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        urlRepository.save(url);

        return "redirect:/hostMenu"; // ★リダイレクト先を変更
    }

    
    @GetMapping("/url/list")
    public String listUrls(Model model) {
        String teacherId = (String) session.getAttribute("loginId");

        // この先生が登録したURLを取得
        var urls = urlRepository.findByTeacherId(teacherId);

        model.addAttribute("urls", urls);
        return "urlList"; // ★表示用HTML（あとで作る）
    }
    
    @PostMapping("/url/delete")
    public String deleteUrl(@RequestParam Long urlId, HttpSession session) {
        String teacherId = (String) session.getAttribute("loginId");

        var urlOpt = urlRepository.findById(urlId);
        if (urlOpt.isPresent()) {
            var url = urlOpt.get();
            if (url.getTeacherId().equals(teacherId)) { // 自分のURLだけ削除可
                url.setIsActive(false);
                urlRepository.save(url);
            }
        }

        return "redirect:/hostMenu";
    }
}
