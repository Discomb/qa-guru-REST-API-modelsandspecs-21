package qa.models.lombok;

import lombok.Data;

@Data
public class LoginBodyModel {
    private String email, password;
}
