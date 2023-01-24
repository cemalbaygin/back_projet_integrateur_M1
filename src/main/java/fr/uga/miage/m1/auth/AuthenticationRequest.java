package fr.uga.miage.m1.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
  @NonNull
  private String email;
  @NonNull
  String password;
}
