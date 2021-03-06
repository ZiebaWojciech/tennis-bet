package pl.coderslab.tennis_bet.betting_site.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.tennis_bet.betting_site.entity.transient_model.CurrentUser;
import pl.coderslab.tennis_bet.betting_site.service.WalletService;

import java.math.BigDecimal;

@Controller
@RequestMapping(path = "/user/wallet")
public class WalletController {
    private  final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @ModelAttribute
    public void setModelAttributes(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        model.addAttribute("user", currentUser.getUser());
    }
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String  userWallet() {
        return "user/wallet";
    }

    @RequestMapping(path = "/recharge",method = RequestMethod.GET)
    public String  rechargeInit() {
        return "user/recharge-form";
    }

    @RequestMapping(path = "/recharge", method = RequestMethod.POST)
    public String  recharge(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam("rechargeAmount") BigDecimal amount) {
        walletService.rechargeFunds(currentUser.getUser(), amount);
        return "redirect:/user/wallet";
    }

    @RequestMapping(path = "/withdraw",method = RequestMethod.GET)
    public String  withdrawInit() {
        return "user/withdraw-form";
    }

    @RequestMapping(path = "/withdraw", method = RequestMethod.POST)
    public String  withdraw(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam("deductionAmount") BigDecimal amount, Model model) {
        if(!walletService.withdraw(currentUser.getUser(), amount)){
            model.addAttribute("deductionSuccess", "Problem occurred. You probably don't have enough funds.");
            return "user/withdraw-form";
        }
        return "redirect:/user/wallet";
    }
}

