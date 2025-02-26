package com.poseidoncapitalsolutions.poseiden.controllers;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.CurvePointDTO;
import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import com.poseidoncapitalsolutions.poseiden.services.CurvePointService;
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
@RequestMapping("/curvePoint")
public class CurveController {
    private final Logger logger = LoggerFactory.getLogger(CurveController.class);

    private final CurvePointService curvePointService;

	public CurveController(CurvePointService curvePointService) {
		this.curvePointService = curvePointService;
	}

	@RequestMapping("/list")
    public String home(Model model, Principal principal)
    {
        if (principal != null) {
            model.addAttribute("user", principal.getName());
        }
        model.addAttribute("curvePoints", curvePointService.getAll());
        return "curvePoint/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointDTO());
        return "curvePoint/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDTO curvePoint, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("CurvePoint Add form has errors {}", result.getAllErrors());
            return "curvePoint/add";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.getById(id);
        CurvePointDTO curvePointDTO = new CurvePointDTO().fromEntity(curvePoint);

        model.addAttribute("curvePoint", curvePointDTO);
        return "curvePoint/update";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("CurvePoint Update form has errors {}", result.getAllErrors());
            return "curvePoint/update";
        }
        curvePointService.update(curvePointDTO);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.delete(id);
        return "redirect:/curvePoint/list";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        logger.error("Failed operation on curvePoint: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "redirect:/curvePoint/list";
    }
}
