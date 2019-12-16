package com.nvt.kts.team3.suiteAll;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.nvt.kts.team3.controllerTests.EventControllerIntegrationTest;
import com.nvt.kts.team3.controllerTests.ReservationControllerIntegrationTest;
import com.nvt.kts.team3.repositoryTests.EventRepositoryIntegrationTest;
import com.nvt.kts.team3.repositoryTests.ReservationRepositoryIntegrationTest;
import com.nvt.kts.team3.serviceTests.EventServiceIntegrationTest;
import com.nvt.kts.team3.serviceTests.EventServiceUnitTest;
import com.nvt.kts.team3.serviceTests.ReservationServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({EventControllerIntegrationTest.class,EventRepositoryIntegrationTest.class,EventServiceIntegrationTest.class, EventServiceUnitTest.class})
public class SuiteAllEvent {

}
