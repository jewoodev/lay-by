package com.layby.web.service.implement;

import com.layby.domain.dto.request.auth.CheckCertificationRequestDto;
import com.layby.domain.dto.request.auth.EmailCertificationRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.auth.CheckCertificationResponseDto;
import com.layby.domain.dto.response.auth.EmailCertificationResponseDto;
import com.layby.domain.entity.CertificationEntity;
import com.layby.domain.repository.CertificationRepository;
import com.layby.domain.repository.UserRepository;
import com.layby.web.provider.EmailProvider;
import com.layby.web.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String username = dto.getUsername();
            String email = dto.getEmail();

            String certificationNumber = getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(username, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String username = dto.getUsername();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUsername(username);
            if (certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatched = certificationEntity.getEmail().equals(email) &&
                    certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return CheckCertificationResponseDto.certificationFail();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    private String getCertificationNumber() {

        StringBuilder certificationNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            certificationNumber.append((int) (Math.random() * 10));
        }

        return certificationNumber.toString();
    }
}
