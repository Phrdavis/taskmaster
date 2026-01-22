package com.tasksmaster.taskmaster.model;

import java.util.Collection;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tasksmaster.taskmaster.enums.Roles;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "D_E_L_E_T_"}),
    }
)
@SQLDelete(sql = "UPDATE users SET D_E_L_E_T_ = id WHERE id = ?")
@SQLRestriction("D_E_L_E_T_ = ''")
@Getter @Setter @NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private Roles role;

    @Override
    public String getUsername() { return email; }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
