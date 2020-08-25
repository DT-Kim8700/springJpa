package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	/** 스프링 부트 스타터를 통해 설정
	 * - 필요한 라이브러리(스프링 web, jpa, 타임리프, 데이터베이스, Lombok)
	 * - 실행 후 포트 번호 확인
	 * - compiler - annotation processor - Enable annotation processing 체크
	 * */

	@Bean
	Hibernate5Module hibernate5Module(){
		return new Hibernate5Module();
	}
}
