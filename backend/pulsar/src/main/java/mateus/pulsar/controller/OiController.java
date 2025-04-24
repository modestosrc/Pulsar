package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;

@RestController
class OiController {

    @GetMapping("/oi")
    public String oi() {
        return "Oi, tudo bem?";
    }

    @GetMapping("/oi/{nome}")
    public String oi(@PathVariable String nome) {
        return "Oi, " + nome + ", tudo bem?";
    }

}

