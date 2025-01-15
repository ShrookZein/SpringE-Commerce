package com.global.ecommerce.Entity;

//import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @NotBlank(message = "Name of Role is mandatory")
    private String name;
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JsonIgnore
//    @JoinTable(name = "sec_user_roles",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "national_id"))
////    private List<User> users=new ArrayList<>();
//    private List<User> users;
}
