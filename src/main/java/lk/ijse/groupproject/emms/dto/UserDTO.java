package lk.ijse.groupproject.emms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
}
