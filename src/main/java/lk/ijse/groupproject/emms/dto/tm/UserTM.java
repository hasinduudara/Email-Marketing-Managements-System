package lk.ijse.groupproject.emms.dto.tm;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserTM {

    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
}
