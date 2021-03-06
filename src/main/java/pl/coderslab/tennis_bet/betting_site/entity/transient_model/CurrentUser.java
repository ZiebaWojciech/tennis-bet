package pl.coderslab.tennis_bet.betting_site.entity.transient_model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUser extends User {
    private final pl.coderslab.tennis_bet.betting_site.entity.User user;

    public CurrentUser(String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities,
                       pl.coderslab.tennis_bet.betting_site.entity.User user) {
        super(username, password, authorities);
        this.user = user;
    }

    public pl.coderslab.tennis_bet.betting_site.entity.User getUser(){
        return user;
    }
}
