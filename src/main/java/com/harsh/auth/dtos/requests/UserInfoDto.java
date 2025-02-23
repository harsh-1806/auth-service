package com.harsh.auth.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.harsh.auth.entities.User;
import lombok.*;
import org.springframework.stereotype.Service;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto extends User {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    private String phoneNumber;
    private String email;
}
