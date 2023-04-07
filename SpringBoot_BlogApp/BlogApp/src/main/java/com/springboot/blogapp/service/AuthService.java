package com.springboot.blogapp.service;

import com.springboot.blogapp.payload.LoginDto;
import com.springboot.blogapp.payload.RegisterDto;

public interface AuthService {

	String login(LoginDto loginDto);

	String register(RegisterDto registerDto);

}
