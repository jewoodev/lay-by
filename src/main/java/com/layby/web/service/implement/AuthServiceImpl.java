package com.layby.web.service.implement;

import com.layby.domain.dto.request.auth.EmailCertificationRequestDto;
import com.layby.domain.dto.response.ResponseDto;
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

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            Long certificationId = Long.parseLong(dto.getId());
            String email = dto.getEmail();

            String certificationNumber = getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(certificationId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    private String getCertificationNumber() {

        StringBuilder certificationNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            certificationNumber.append((int) (Math.random() * 10));
        }

        return certificationNumber.toString();
    }
}
