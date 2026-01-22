package com.tasksmaster.taskmaster.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@SQLDelete(sql = "UPDATE #{#entityName} SET D_E_L_E_T_ = '*' WHERE id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = String.class))
@Filter(name = "deletedFilter", condition = "D_E_L_E_T_ = :isDeleted")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public abstract class BaseEntity {

    @Column(name = "D_E_L_E_T_", nullable = false)
    private String deleted = "";

    @CreatedDate
    @Column(name = "C_R_E_A_T_E_", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    

}
