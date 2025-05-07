package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UrlForm {

    @NotBlank(message = "URLを入力してください")
    private String url;

    private String label; // 任意でタイトル付ける用
}
