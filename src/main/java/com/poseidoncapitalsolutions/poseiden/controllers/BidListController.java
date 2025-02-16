package com.poseidoncapitalsolutions.poseiden.controllers;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.BidListDTO;
import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.services.BidListService;
import com.poseidoncapitalsolutions.poseiden.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/bidList")
public class BidListController {
    private final Logger logger = LoggerFactory.getLogger(BidListController.class);

    private final BidListService bidListService;
    private final UserService userService;

    public BidListController(BidListService bidListService, UserService userService) {
		this.bidListService = bidListService;
        this.userService = userService;
    }

	@RequestMapping("/list")
    public String home(Model model, Principal principal)
    {
        if (principal != null) {
            model.addAttribute("user", principal.getName());
        }
        model.addAttribute("bidLists", bidListService.getAll());
        return "bidList/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("bid", new BidListDTO());
        return "bidList/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bid") BidListDTO bid, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("BidList Add form has errors {}", result.getAllErrors());
            return "bidList/add";
        }
        bidListService.save(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListService.getById(id);
        BidListDTO bidListDTO = new BidListDTO().fromEntity(bidList);

        model.addAttribute("bid", bidListDTO);
        return "bidList/update";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidListDTO bid, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.update(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        return "redirect:/bidList/list";
    }

    // Handle the exceptions
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleBidNotFoundException(EntityNotFoundException e, Model model) {
        logger.error("Failed operation on bid: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "redirect:/bidList/list";
    }
}
