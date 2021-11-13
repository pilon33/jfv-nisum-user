package com.nisum.test.user;


import com.nisum.test.user.model.Phone;
import com.nisum.test.user.model.User;
import com.nisum.test.user.repository.IUserRepository;
import com.nisum.test.user.service.JwtUserDetailsService;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class NisumUserApplicationTests {

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    JwtUserDetailsService userDetailsService;

    @Mock
    PasswordEncoder passwordEncoder;


    final String name = "Jose Valdez";
    final String email = "test@nisum.cl";
    final boolean active = true;



    @Test
    public void testSaveToDo() {

              Phone phone1 = Phone.builder()
                .cityCode("123")
                .countryCode("123")
                .number("123")
                .build();

        Phone phone2 = Phone.builder()
                .cityCode("55")
                .countryCode("55")
                .number("55")
                .build();


        List<Phone> phonesList = new ArrayList<>();
        phonesList.add(phone1);
        phonesList.add(phone2);

        String uuid = UUID.randomUUID().toString();

        User user = User.builder()
                .active(active)
                .id(uuid)
                .name(name)
                .email(email)
                .password(passwordEncoder.encode("Admin23"))
                .phones(phonesList)
                .build();

        when(userRepository.save(user)).thenReturn(user);
        User result = userRepository.save(user);
        assertEquals(uuid, result.getId());
        assertEquals(name, result.getName());
        assertEquals(active, result.isActive());
    }

    @Test
    public void testGetUserByUsername() {
        Phone phone = Phone.builder()
                .cityCode("88")
                .countryCode("88")
                .number("88")
                .build();

        List <Phone> phonesList = new ArrayList <>();
        phonesList.add(phone);

        String uuid = UUID.randomUUID().toString();
        User user = User.builder()
                .active(active)
                .id(uuid)
                .name(name)
                .email(email)
                .password(passwordEncoder.encode("Admin23"))
                .phones(phonesList)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(user);
        User userResult = userRepository.findByEmail(email);
        assertEquals(passwordEncoder.encode("Admin23"), userResult.getPassword());
        assertEquals(name, userResult.getName());
        assertEquals(email, userResult.getEmail());
        assertEquals(uuid, userResult.getId());


    }
}
