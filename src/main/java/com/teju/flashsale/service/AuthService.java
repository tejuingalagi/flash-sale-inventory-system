package com.teju.flashsale.service;

import com.teju.flashsale.dto.AuthResponse;
import com.teju.flashsale.dto.LoginRequest;
import com.teju.flashsale.dto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
}