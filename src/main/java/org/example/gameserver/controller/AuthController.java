package org.example.gameserver.controller;
import org.example.gameserver.entity.PlayerEntity;
import org.example.gameserver.service.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final PlayerService playerService;
    public AuthController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public PlayerEntity register(@RequestParam String username, @RequestParam String password) {
        return playerService.register(username, password);
    }

    @PostMapping("/login")
    public PlayerEntity login(@RequestParam String username, @RequestParam String password) {
        return playerService.login(username, password);
    }

    @GetMapping("/checkUsername")
    public String checkUsername(@RequestParam String username) {
        boolean exists = playerService.exists(username);
        return exists ? "EXISTS" : "OK";
    }
}
