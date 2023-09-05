package com.sangeng.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private Long id;
    private String name;
    private String remark;

}
