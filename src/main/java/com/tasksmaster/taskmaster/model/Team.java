package com.tasksmaster.taskmaster.model;

import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "teams",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "D_E_L_E_T_"}),
    }
)
@SQLDelete(sql = "UPDATE teams SET D_E_L_E_T_ = id WHERE id = ?")
@SQLRestriction("D_E_L_E_T_ = ''")
@Getter @Setter @NoArgsConstructor
public class Team extends BaseEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinator_id", nullable = false)
    private User coordinator;

    @ManyToMany
    @JoinTable(name = "team_members")
    private Set<User> members;

}
