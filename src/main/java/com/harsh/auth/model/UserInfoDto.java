package com.harsh.auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.harsh.auth.entities.UserInfo;
import lombok.*;
import org.springframework.stereotype.Service;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto extends UserInfo {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    private Long phoneNumber;
    private String email;
}
