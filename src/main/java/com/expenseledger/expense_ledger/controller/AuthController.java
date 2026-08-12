package com.expenseledger.expense_ledger.controller;
import com.expenseledger.expense_ledger.dto.RegisterRequest;
import com.expenseledger.expense_ledger.entity.User;
import com.expenseledger.expense_ledger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.expenseledger.expense_ledger.dto.LoginRequest;
import com.expenseledger.expense_ledger.dto.AuthResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
@PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request){
    User savedUser = userService.registerUser(request);
    return ResponseEntity.ok(savedUser);
}
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        String token = userService.loginUser(request);
        return ResponseEntity.ok(new AuthResponse(token));
    } catch (RuntimeException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

}
