package com.steelerose.crm.controller;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.model.Role;
import com.steelerose.crm.model.User;
import com.steelerose.crm.service.ClientService;
import com.steelerose.crm.service.UserService;
import com.steelerose.crm.web.dto.CallListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class MainController {


    @Autowired
    ClientService clientService;

    @Autowired
    private UserService userService;

    // display list of clients
   /* @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listClients", clientService.getAllClients());
        return "home";
    }*/

    // display list of client

    @GetMapping("/settingForm")
    public String settingForm(Model model) {

        List<User> userList = userService.findUsers();
        for (User user: userList) {
            Collection<Role> roles = user.getRoles();
            for (Role role: roles) {
                if (role.getName().equals("ADMIN")) {
                    user.setAdmin(true);
                    continue;
                } else user.setAdmin(false);
            }
        }
        model.addAttribute("users", userList);
        return "settings_page";
    }
    @GetMapping("/makeAdmin/{id}")
    public String makeAdmin(@PathVariable(value = "id") long id) {

        this.userService.updateRoles(id, "makeAdmin");
        return "redirect:/settingForm";
    }
    @GetMapping("/removeAdmin/{id}")
    public String removeAdmin(@PathVariable(value = "id") long id) {

        this.userService.updateRoles(id, "removeAdmin");
        return "redirect:/settingForm";
    }

    @GetMapping("/index")
    public String root(Model model) {

        List<Client> clientList = clientService.getAllClients();
        model.addAttribute("listAll", clientList);

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userHome() {
        return "user/home";
    }

}
