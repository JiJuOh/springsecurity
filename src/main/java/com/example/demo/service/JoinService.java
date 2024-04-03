package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.JoinDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;

@Service
public class JoinService {
	
	@Autowired
	private UserRepository userRepository; // 나중에 생성자 주입 방식으로 바꿔
	
	@Autowired 
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public void joinProcess(JoinDTO joinDTO) {
		
		// db에 이미 동일한 username 을 가진 회원이 존재하는지? 검증!
		boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
		if(isUser) {
			return; // 존재하면 강제 리턴
		}
		
		// 존재하지 않을 경우 정상 진행
		UserEntity data = new UserEntity(); //빈객체 만들기
		
		data.setUsername(joinDTO.getUsername());
		data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); // 암호화해서 넣어야함!
		data.setRole("ROLE_USER"); // ROLE_ 이라는 접두사 붙여야함 ex ROLE_ADMIN
		
		userRepository.save(data);
	}
}
