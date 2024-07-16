package com.shopme.admin.user.controller;


import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {


    @Autowired
   private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage(1,model,"firstName","asc",null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") Integer pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){
        System.out.println("Sort field :"+sortField);
        System.out.println("Sort Order : "+sortDir);
        System.out.println("KeyWord: "+keyword);
        Page<User> page=userService.listByPage(pageNum,sortField,sortDir,keyword);
        List<User> usersList=page.getContent();
        long startCount=(pageNum-1) * userService.USERS_PER_PAGE + 1;
        long endCount=startCount+userService.USERS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();

        }
        int currentPage=pageNum;
        String reverseSortOrder= sortDir.equals("asc")? "desc":"asc";
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listUsers",usersList);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortOrder",reverseSortOrder);
        model.addAttribute("keyword",keyword);

        return "users/users";

        /*System.out.println("Page number "+pageNum);
        System.out.println("Total Elements = "+page.getTotalElements());
        System.out.println("Total Pages = "+page.getTotalPages());

         */
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> listRoles=userService.listRoles();
        User user=new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","Create New User");
        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        System.out.println(user);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message","The User has been saved Successfully.");

        String firstPartofEmail=user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartofEmail;

    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,Model model,
                           RedirectAttributes redirectAttributes) throws UserNotFoundException {

        try {
            User user = userService.get(id);
            List<Role> listRoles;
            listRoles=userService.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("listRoles",listRoles);
            model.addAttribute("pageTitle","Edit User (ID: "+id+")");

            return "users/user_form";
        }
        catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/users";
        }

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,Model model,
                             RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("message","User ID "+id+" has Sucessfully Deleted");

        }
        catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());

        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
                                      RedirectAttributes redirectAttributes){
        userService.updateEnabledStatus(id,enabled);

        /*if(enabled==true){
            redirectAttributes.addFlashAttribute("message","User Status is Enabled");
        }
        else {
            redirectAttributes.addFlashAttribute("message","User Status is Disabled");
        }*/

        String status=enabled ? " Enabled ":" Disabled ";
        String message="User ID "+id+" Status is"+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }



}