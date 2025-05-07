package com.example.demo.service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID; // ← これインポート必要！

import org.springframework.stereotype.Service;

import com.example.demo.entity.InviteCode;
import com.example.demo.entity.TeacherStudentLink;
import com.example.demo.repository.InviteCodeRepository;
import com.example.demo.repository.TeacherStudentLinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InviteCodeService {

    private final InviteCodeRepository inviteCodeRepository;
    private final TeacherStudentLinkRepository teacherStudentLinkRepository;

    /**
     * 招待コードを生成して保存する
     * @param teacherId ログイン中の先生のID
     * @return 発行されたコード文字列
     */
    public String createInviteCode(String teacherId) {
        String code = generateCode();

        InviteCode inviteCode = new InviteCode();
        inviteCode.setCode(code);
        inviteCode.setTeacherId(teacherId);
        inviteCode.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusYears(1)));
        inviteCode.setUsageCount(0);

        inviteCodeRepository.save(inviteCode);

        return code;
    }
    
    public String createOrGetInviteCode(String teacherId) {
        var existingCodes = inviteCodeRepository.findByTeacherId(teacherId);

        if (!existingCodes.isEmpty()) {
            return existingCodes.get(0).getCode(); // すでにあるコードを返す
        }

        // なければ普通に作る
        return createInviteCode(teacherId);
    }
    
    public static String generateCode() {
    	
    	final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int CODE_LENGTH = 6;
        final SecureRandom random = new SecureRandom();

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }
    public boolean bindStudentToTeacher(String code, String studentId) {
        // 1. 招待コードを探す
        var inviteCodeOpt = inviteCodeRepository.findById(code);
        if (inviteCodeOpt.isEmpty()) {
            return false; // コードが存在しない
        }
        var inviteCode = inviteCodeOpt.get();

        // 2. 有効期限チェック
        if (inviteCode.getExpiresAt() != null && inviteCode.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
            return false; // 期限切れ
        }

        // 3. すでに同じ生徒-先生のリンクが存在するか確認
        boolean exists = teacherStudentLinkRepository
                .existsByStudentIdAndTeacherId(studentId, inviteCode.getTeacherId());
        if (exists) {
            return true; // すでに登録済みなら何もしないで成功扱い
        }

        // 4. なければ新規作成
        TeacherStudentLink link = new TeacherStudentLink();
        link.setId(UUID.randomUUID().toString());
        link.setStudentId(studentId);
        link.setTeacherId(inviteCode.getTeacherId());
        link.setJoinedAt(Timestamp.valueOf(LocalDateTime.now()));
        link.setInviteCode(code);

        teacherStudentLinkRepository.save(link);

        // 5. 招待コードの使用回数カウントを増やす（任意）
        inviteCode.setUsageCount(inviteCode.getUsageCount() + 1);
        inviteCodeRepository.save(inviteCode);

        return true;
    }
}