package org.westernacher.solutions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.westernacher.solutions.domain.entities.Log;
import org.westernacher.solutions.domain.entities.Operation;
import org.westernacher.solutions.domain.entities.User;
import org.westernacher.solutions.domain.models.service.UserServiceModel;
import org.westernacher.solutions.repository.LogsRepository;
import org.westernacher.solutions.repository.UserRepository;
import org.westernacher.solutions.service.LogServiceImpl;
import org.westernacher.solutions.service.UserServiceImpl;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
//@DataJpaTEst
@ActiveProfiles("test")
public class LogServiceTestsMockito
{
    private static final String PASSWORD_HASH = "myCustomHash";

    @Mock
    private UserRepository userRepository;
    @Mock
    private LogsRepository logsRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private User userEntity;

    @InjectMocks
    private LogServiceImpl logService;
//
//    @Before
//    public void setUp() {
//        when(this.bCryptPasswordEncoder.encode(anyString()))
//                .thenReturn(PASSWORD_HASH);
//    }

    @Test
    public void testInsertLog_withLog_shouldBeInsertedAndReturnCheck() {

        when(this.logsRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Log log = new Log();
        log.setUser("dimitaravramov");
        log.setTableName("Organization");
        log.setOperation(Operation.Promote);
        log.setId("myuuid");
        log.setDate("04/01/2020");

        boolean result = this.logService.insertLog(log);

        assertEquals("Log is not inserted.", "dimitaravramov", log.getUser());
    }

    @Test
    public void testInsertLog_withLog_shouldBeInsertedAndGetUserCheck() {

            when(this.logsRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Log log = new Log();
            log.setUser("dimitaravramov");
            log.setTableName("Organization");
            log.setOperation(Operation.Promote);
            log.setId("myuuid");
            log.setDate("04/01/2020");

            boolean result = this.logService.insertLog(log);

            assertEquals("Log is not inserted.", true, result);
    }

//    @Test
//    public void testRegister_withUsernameAndPassword_passwordShouldBeEncoded() {
//
//        when(this.userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
//
//        final String USERNAME = "Peter";
//        final String PASSWORD = "123123";
//        final String FIRST_NAME = "Peter";
//        final String LAST_NAME = "Brysch";
//        final String EMAIL = "Peter";
//        final Date BIRTH_DATE = new Date();
//
//        UserServiceModel userServiceModel = new UserServiceModel();
//        userServiceModel.setUsername(USERNAME);
//        userServiceModel.setPassword(PASSWORD);
//        userServiceModel.setConfirmPassword(PASSWORD);
//        userServiceModel.setFirstName(FIRST_NAME);
//        userServiceModel.setLastName(LAST_NAME);
//        userServiceModel.setEmail(EMAIL);
//        userServiceModel.setBirthDate(BIRTH_DATE);
//
//        boolean result = this.userService.createUser(userServiceModel);
//
//        assertEquals("Account is not registered.", true, result);
//        //assertEquals("Password was not or wrongly encoded.", PASSWORD_HASH, result.getPassword() );
//    }



}
