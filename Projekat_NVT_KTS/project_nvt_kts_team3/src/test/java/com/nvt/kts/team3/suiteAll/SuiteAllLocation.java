package com.nvt.kts.team3.suiteAll;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.nvt.kts.team3.controller.LocationController;
import com.nvt.kts.team3.controllerTests.EventControllerIntegrationTest;
import com.nvt.kts.team3.controllerTests.EventControllerUnitTest;
import com.nvt.kts.team3.controllerTests.LocationControllerTest;
import com.nvt.kts.team3.repositoryTests.EventRepositoryIntegrationTest;
import com.nvt.kts.team3.repositoryTests.LocationRepositoryIntegrationTest;
import com.nvt.kts.team3.serviceTests.EventServiceIntegrationTest;
import com.nvt.kts.team3.serviceTests.EventServiceUnitTest;
import com.nvt.kts.team3.serviceTests.LocationServiceIntegrationTest;
import com.nvt.kts.team3.serviceTests.LocationServiceUnitTest;

@RunWith(Suite.class)
@SuiteClasses({LocationControllerTest.class, LocationRepositoryIntegrationTest.class, LocationServiceIntegrationTest.class, LocationServiceUnitTest.class})
public class SuiteAllLocation {

}
