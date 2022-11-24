package com.triple.destination_management.domain.user.dto;

import javax.validation.constraints.NotBlank;

import com.triple.destination_management.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

	@NotBlank(message = "아이디를 입력해주세요.")
	private String username;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@NotBlank(message = "이름을 입력해주세요.")
	private String name;

	public static User dtoToEntity(UserRequest userRequest) {
		return User.builder()
			.name(userRequest.getName())
			.username(userRequest.getUsername())
			.build();
	}
}
