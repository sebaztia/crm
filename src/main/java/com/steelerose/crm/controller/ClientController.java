package com.steelerose.crm.controller;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.model.User;
import com.steelerose.crm.service.CallListService;
import com.steelerose.crm.service.ClientService;
import com.steelerose.crm.service.EmailService;
import com.steelerose.crm.service.UserService;
import com.steelerose.crm.web.dto.CallListDto;
import com.steelerose.crm.web.dto.ClientDto;
import com.steelerose.crm.web.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    // display list of clients

    @GetMapping("/showClientList")
    public String showClientList(Model model) {
        return findPaginatedClients(1, "fullName", "asc", model);
    }

   /* @GetMapping("/")
    public String viewClientList(Model model) {
        return findPaginated(1, "firstName", "asc", model);
    }*/

    @GetMapping("/showNewClientForm")
    public String showNewClientForm(Model model) {
        // create model attribute to bind form data
        ClientDto dto = new ClientDto();
        dto.setRecordDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        model.addAttribute("client", dto);
        return "new_client";
    }

    @PostMapping("/saveClient")
    public String saveClient(@ModelAttribute("client") @Valid ClientDto dto, BindingResult result) {
        // save client to database
        if (result.hasErrors()) {
            return "new_client";
        }

       // String email = dto.getUserId();

      //  User user = userService.findByEmail(email);
      //  log.info("ndfjgdfgs =" + user);

      //  dto.setUserId(String.valueOf(user.getId()));


        clientService.saveClient(dto);
        return "redirect:/index";
    }
    @PostMapping("/updateClient")
    public String updateClient(@ModelAttribute("client") @Valid ClientDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "update_client";
        }
        clientService.saveClient(dto);
        return "redirect:/index";
    }
    @GetMapping("/showReference/{id}")
    public String showReference(@PathVariable(value = "id") long id, Model model) {

        ClientDto showRefDto = clientService.getClientById(id);
        List<ClientDto> linkRef = clientService.getAllByRefNumber(showRefDto.getRefNumber(), id);

        model.addAttribute("show_ref", showRefDto);
        model.addAttribute("linkedReference", linkRef);
        return "show_reference";
    }


    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model) {

        // get Client from the service
        ClientDto dto = clientService.getClientById(id);

        // set Client as a model attribute to pre-populate the form
        model.addAttribute("client", dto);
        return "update_client";
    }


    @GetMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable (value = "id") long id) {

        // call delete Client method
        this.clientService.deleteClientById(id);
        return "redirect:/index";
    }

    @GetMapping("/client_list/page/{pageNo}")
    public String findPaginatedClients(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 10;

        Page< Client > page = clientService.findPaginatedClients(pageNo, pageSize, sortField, sortDir);
        List< Client > listClients = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listClients", listClients);
        return "client_list";
    }

    @GetMapping("/showClientEmailForm/{id}")
    public String showClientEmailForm(@PathVariable( value = "id") long id, Model model) {

        ClientDto dto = clientService.getClientById(id);

        EmailDto emailDto = new EmailDto();
     //   emailDto.setCallListDto(d);
        emailDto.setSubject("CRM Client Information");
        emailDto.setText("Hi, \nPlease find the below client details." +
                "\n\nFull Name: " + dto.getFullName() +
                "\nReference Number: " + dto.getRefNumber() +
                "\nDate: " + dto.getRecordDate() +
                "\nCall Sheet: " + dto.getCallSheet());

        model.addAttribute("email_client", emailDto);
        return "email_client_show";
    }

    @PostMapping("/sendEmailClient")
    public String sendEmailClient(@ModelAttribute("email_client") @Valid EmailDto emailDto, BindingResult result) {

        if (result.hasErrors()) {
            return "email_client_show";
        }

        String subject = emailDto.getSubject();
        String text = emailDto.getText();
        if (null == subject || subject.equals("")) {
            subject = "CRM Call List Information";
        }
        if (null == text || text.equals("")) {
            text = "Please find the below call sheet details.";
        }

        log.info("---------- email service:" + emailService);
        emailService.sendSimpleMessage(emailDto.getTo(), subject, text);

        return "redirect:/index";
    }

}
