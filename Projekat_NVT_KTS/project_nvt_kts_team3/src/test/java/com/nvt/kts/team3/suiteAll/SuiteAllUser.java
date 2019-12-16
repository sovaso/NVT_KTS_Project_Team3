package com.nvt.kts.team3.suiteAll;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.nvt.kts.team3.controllerTests.AuthenticationControllerIntegrationTest;
import com.nvt.kts.team3.controllerTests.EventControllerIntegrationTest;
import com.nvt.kts.team3.controllerTests.EventControllerUnitTest;
import com.nvt.kts.team3.controllerTests.UserControllerIntegrationTest;
import com.nvt.kts.team3.repositoryTests.EventRepositoryIntegrationTest;
import com.nvt.kts.team3.repositoryTests.UserRepositoryIntegrationTest;
import com.nvt.kts.team3.serviceTests.CustomUserDetailsServiceIntegrationTest;
import com.nvt.kts.team3.serviceTests.CustomUserDetailsServiceUnitTest;
import com.nvt.kts.team3.serviceTests.EventServiceIntegrationTest;
import com.nvt.kts.team3.serviceTests.EventServiceUnitTest;
import com.nvt.kts.team3.serviceTests.UserServiceIntegrationTest;
import com.nvt.kts.team3.serviceTests.UserServiceUnitTest;

@RunWith(Suite.class)
@SuiteClasses({AuthenticationControllerIntegrationTest.class,
	UserControllerIntegrationTest.class, UserRepositoryIntegrationTest.class,
	CustomUserDetailsServiceIntegrationTest.class, CustomUserDetailsServiceUnitTest.class,
	UserServiceIntegrationTest.class, UserServiceUnitTest.class
	
})
public class SuiteAllUser {

}
