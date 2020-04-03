package com.vmware.tanzu.demo.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class AccountsApplicationTests {

	@Test
	void contextLoads() {
	}

}
