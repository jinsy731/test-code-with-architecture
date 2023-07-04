package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.util.UUID;

@Builder
@Getter
public class User {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    public static User from(UserCreate userCreate) {
        return User.builder()
                .email(userCreate.getEmail())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .nickname(userCreate.getNickname())
                .certificationCode(UUID.randomUUID().toString())
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(id)
                .email(email)
                .address(userUpdate.getAddress())
                .nickname(userUpdate.getNickname())
                .status(status)
                .certificationCode(certificationCode)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User login() {
        return User.builder()
                .id(id)
                .email(email)
                .address(address)
                .nickname(nickname)
                .status(status)
                .certificationCode(certificationCode)
                .lastLoginAt(Clock.systemUTC().millis())
                .build();
    }

    public User certificate(String certificationCode) {
        if(!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }
        return User.builder()
                .id(id)
                .email(email)
                .address(address)
                .nickname(nickname)
                .status(UserStatus.ACTIVE)
                .certificationCode(certificationCode)
                .lastLoginAt(lastLoginAt)
                .build();
    }
}
