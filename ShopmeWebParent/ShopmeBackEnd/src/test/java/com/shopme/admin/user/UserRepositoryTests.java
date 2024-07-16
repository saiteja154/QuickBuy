package com.shopme.admin.user;


import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole(){
      Role roleAdmin = entityManager.find(Role.class,1);
        User userSaiTeja=new User("saiteja@gmail.com","12345","Sai Teja","Rapelli");
        //userSaiTeja.addRole(null);
        userSaiTeja.addRole(roleAdmin);
        User savedUser= userRepository.save(userSaiTeja);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateNewUserWithTwoRoles(){
        User userAbhi=new User("abhilash@gmail.com","abhilash","Abhilash","Rapelli");
        Role roleEditor=new Role(3);
        Role roleAssistant=new Role(5);

        userAbhi.addRole(roleEditor);
        userAbhi.addRole(roleAssistant);
        User savedUser= userRepository.save(userAbhi);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers=userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));

    }
    @Test
    public void testGetUserById(){
        User user= userRepository.findById(2).get();
        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public  void testUpdateUserDetails(){
        User user=userRepository.findById(1).get();
        user.setEnabled(true);
        user.setEmail("saiteja154@gmail.com");
        userRepository.save(user);
        System.out.println(user);

    }

    @Test
    public void testUpdateUserRoles(){
        User user=userRepository.findById(2).get();
        Role roleEditor=new Role(3);
        Role roleSalesperson=new Role(2);
        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesperson);
        userRepository.save(user);

    }

    @Test
    public void testDeleteUser(){
        Integer userId=2;
        //User user=userRepository.findById(2).get();
        userRepository.deleteById(userId);

    }

    @Test
    public void testGetUserByEmail(){
        String email="abcdef@gmail.com";
        User user= userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();

    }

    @Test
    public void testCountById(){
        Integer id=1;
        Long count=userRepository.countById(id);
        assertThat(count).isNotNull().isGreaterThan(0);
        return;
    }

    @Test
    public void testDisableUser(){
        Integer id=17;
        boolean enabled=false;
        userRepository.updateEnabledStatus(id,enabled);

    }

    @Test
    public void testEnableUser(){
        Integer id=17;
        boolean enabled=true;
        userRepository.updateEnabledStatus(id,enabled);

    }

    @Test
    public void testListFirstPage(){
        int pageNumber=0;
        int pageSize=4;
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User> page=userRepository.findAll(pageable);
        List<User> listUsers=page.getContent();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers(){
        String keyword="Bruce";
        int pageNumber=0;
        int pageSize=4;
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<User> page=userRepository.findAll(keyword,pageable);
        List<User> listUsers=page.getContent();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isGreaterThan(0);

    }
}














