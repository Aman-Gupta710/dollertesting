package com.example.blogapp.entity;

import com.example.blogapp.config.AppConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Date expirationTime;


    @OneToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_RESET_PASSWORD_TOKEN")
    )
    private User user;

    public PasswordResetToken(User user, String token){
        this.user = user;
        this.token = token;
        this.expirationTime = calculateExpirationDate(AppConstants.EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, AppConstants.EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());

    }
}
