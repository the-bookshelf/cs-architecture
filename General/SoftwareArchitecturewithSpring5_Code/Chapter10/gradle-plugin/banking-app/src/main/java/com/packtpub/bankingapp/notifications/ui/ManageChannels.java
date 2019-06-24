package com.packtpub.bankingapp.notifications.ui;

import com.packtpub.bankingapp.balance.dao.CustomerRepository;
import com.packtpub.bankingapp.balance.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ManageChannels {

    private final CustomerRepository customerRepository;

    @Autowired
    public ManageChannels(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public String manageNotificationChannels(Model model, @AuthenticationPrincipal User loggedInUser) {
        Customer customer = customerRepository.findByUsername(loggedInUser.getUsername()).get();
        model.addAttribute("preferredChannels", customer.getPreferredNotificationChannels());
        return "customers/notification-channels";
    }

}
