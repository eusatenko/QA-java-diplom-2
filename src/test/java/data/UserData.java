package data;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String email = "usatenko" + (10000 + (int)(Math.random() *99999)) + "@pochta.ru";
    private String password = "Qwerty" + (100000 + (int)(Math.random() *999999));
    private String name = "Test" + (100000 + (int)(Math.random() *999999));
}
