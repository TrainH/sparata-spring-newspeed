package spartaspringnewspeed.spartafacespeed;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.friend.service.FriendService;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;
import spartaspringnewspeed.spartafacespeed.user.service.UserService;

import java.util.Optional;

//@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpartaFacespeedApplicationTests {

//    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void setUp() {
        // 각 테스트 전에 데이터베이스 초기화
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void contextLoads() {
        // Given: 테스트용 사용자 데이터 추가
        User user = new User("망곰이", "MG22@gmail.com", "123456");
        userRepository.save(user);


        // When: 메서드 호출
        Optional<User> foundUser = userRepository.findByUserId(1L);



        // Then: 결과 검증
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(1L, foundUser.get().getUserId());
        Assertions.assertEquals("망곰이", foundUser.get().getUserName());

    }



}
