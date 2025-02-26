package com.poseidoncapitalsolutions.poseiden.controllers;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.RatingDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import com.poseidoncapitalsolutions.poseiden.services.RatingService;
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
@RequestMapping("/rating")
public class RatingController {
    private final Logger logger = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @RequestMapping("/list")
    public String home(Model model, Principal principal)
    {
        if (principal != null) {
            model.addAttribute("user", principal.getName());
        }
        model.addAttribute("ratings", ratingService.getAll());
        return "rating/list";
    }

    @GetMapping("/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new RatingDTO());
        return "rating/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDTO rating, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Rating Add form has errors {}", result.getAllErrors());
            return "rating/add";
        }
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.getById(id);
        RatingDTO ratingDTO = new RatingDTO().fromEntity(rating);

        model.addAttribute("rating", ratingDTO);
        return "rating/update";
    }

    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid @ModelAttribute("rating") RatingDTO ratingDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Rating Update form has errors {}", result.getAllErrors());
            return "rating/update";
        }
        ratingService.update(ratingDTO);
        return "redirect:/rating/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.delete(id);
        return "redirect:/rating/list";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        logger.error("Failed operation on rating: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "redirect:/rating/list";
    }
}
