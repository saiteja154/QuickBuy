package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE=4;
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> listALLUsers(){
        return (List<User>) userRepository.findAll();
    }

    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }
    public  List<Role> listRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public void save(User user) {
        boolean isUpdatingUser = (user.getId()!=null);


        if(isUpdatingUser){
            User existingUser=userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }
            else{
                encodePassword(user);
            }
        }
        else{
            encodePassword(user);
        }
        userRepository.save(user);

    }


    public User updateAccount(User userInForm) {
        User userInDB = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()) {
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }
        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepository.save(userInDB);
    }

    private void encodePassword(User user){
        String encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return ;
    }

    public boolean isEmailUnique(Integer id,String email){
        User userByEmail= userRepository.getUserByEmail(email);
        if(userByEmail==null){
            return true;
        }
        boolean isCreatingNewUser=(id==null);
        if(isCreatingNewUser){
            if(userByEmail!=null){
                return false;
            }
        }
        else{
            if(userByEmail.getId()!=id){
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }
        catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could Not find any User with ID: "+id);

        }
    }

    public void deleteById(Integer id) throws UserNotFoundException {
        Long countById=userRepository.countById(id);
        if(countById==null || countById==0){
            throw new UserNotFoundException("Could Not find any User with ID: "+id);
        }
        userRepository.deleteById(id);
    }

    public void updateEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id,enabled);
    }


    public Page<User> listByPage(int pageNumber,String sortField, String sortDir,String keyword){
        int pageSize=4;
        Sort sort=Sort.by(sortField);
        sort= sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable= PageRequest.of(pageNumber-1,USERS_PER_PAGE,sort);
        if(keyword!=null){
         Page<User> page=userRepository.findAll(keyword,pageable);
            return page;
        }
        else {
            Page<User> page = userRepository.findAll(pageable);
            return page;
        }

    }


}
