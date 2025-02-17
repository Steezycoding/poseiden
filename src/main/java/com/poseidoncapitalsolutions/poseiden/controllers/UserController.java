package com.poseidoncapitalsolutions.poseiden.controllers;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.UserDTO;
import com.poseidoncapitalsolutions.poseiden.domain.User;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserAlreadyExistsException;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserIdNotFoundException;
import com.poseidoncapitalsolutions.poseiden.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.getAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("User Registration form has errors {}", result.getAllErrors());
            return "user/add";
        } else {
            userService.save(userDTO);
        }
        return "redirect:/user/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getById(id);
        UserDTO userDTO = new UserDTO().fromEntity(user);

        model.addAttribute("user", userDTO);
        return "user/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        userService.update(id, userDTO);

        return "redirect:/user/list";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.delete(id);
        model.addAttribute("users", userService.getAll());
        return "redirect:/user/list";
    }

    // Handle exceptions
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleInvalidUserFormException(UserAlreadyExistsException e, Model model) {
        logger.error("Failed operation on user: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "user/add";
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public String handleUserNotFoundException(UserIdNotFoundException e, Model model) {
        logger.error("Failed operation on user: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "redirect:/user/list";
    }
}
