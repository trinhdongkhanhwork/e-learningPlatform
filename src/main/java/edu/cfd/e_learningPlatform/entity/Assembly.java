package edu.cfd.e_learningPlatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assembly")
public class Assembly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameAssembly;
    private String imageAssembly;

    @ManyToOne
    @JoinColumn(name = "admin")
    private User admin;

    private boolean active;

    @OneToMany(mappedBy = "assembly")
    private List<Message> messages;

    @ManyToMany
    @JoinTable(name = "user_assembly",
                joinColumns = @JoinColumn(name = "assembly_id"),
                inverseJoinColumns = @JoinColumn(name = "users_id"))
    private List<User> userAssemblys;
}