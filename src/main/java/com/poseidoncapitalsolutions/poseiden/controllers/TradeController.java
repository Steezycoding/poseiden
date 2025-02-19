package com.poseidoncapitalsolutions.poseiden.controllers;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.TradeDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import com.poseidoncapitalsolutions.poseiden.services.TradeService;
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
public class TradeController {
    private final Logger logger = LoggerFactory.getLogger(TradeController.class);

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @RequestMapping("/trade/list")
    public String home(Model model, Principal principal)
    {
        if (principal != null) {
            model.addAttribute("user", principal.getName());
        }
        model.addAttribute("trades", tradeService.getAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Model model) {
        model.addAttribute("trade", new TradeDTO());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDTO tradeDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Trade Add form has errors {}", result.getAllErrors());
            return "trade/add";
        }
        tradeService.save(tradeDTO);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.getById(id);
        TradeDTO tradeDTO = new TradeDTO().fromEntity(trade);

        model.addAttribute("trade", tradeDTO);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid @ModelAttribute("trade") TradeDTO tradeDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Trade Update form has errors {}", result.getAllErrors());
            return "trade/update";
        }
        tradeService.update(tradeDTO);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Trade by Id and delete the Trade, return to Trade list
        return "redirect:/trade/list";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        logger.error("Failed operation on trade: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "redirect:/trade/list";
    }
}
