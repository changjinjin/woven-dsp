package com.merce.woven.security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.merce.woven.cas.client.reactive.config.EnableCasClientWebflux;
import com.merce.woven.cas.client.reactive.vo.LoginParamDto;

@SpringBootApplication
@Controller
@EnableCasClientWebflux
public class CasClientStarter {
	public static void main(String[] args) {
		SpringApplication.run(CasClientStarter.class, args);
	}

	@RequestMapping(value = { "/", "/index" })
	public String index(Model model) {
		return "index";
	}

	@PostMapping(value = { "/test/ok", "/test/ok1", "/test/ok2", "/test/ok3" })
	@ResponseBody
	public ResponseEntity<LoginParamDto> test(LoginParamDto dto) {
		return ResponseEntity.ok(dto);
	}
}
