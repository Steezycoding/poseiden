package com.poseidoncapitalsolutions.poseiden.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@RequestMapping("/")
	public String home(Model model)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "redirect:/admin/home";
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			return "redirect:/bidList/list";
		} else {
			return "redirect:/app/login";
		}
	}

	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "home";
	}
}
