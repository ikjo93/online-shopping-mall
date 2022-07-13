package musinsa.onlineshoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnlineShoppingMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShoppingMallApplication.class, args);
	}

}
