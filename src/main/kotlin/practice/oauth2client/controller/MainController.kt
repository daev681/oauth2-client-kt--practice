package practice.oauth2client.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    @GetMapping("/")
    fun home(): String = "index"

    @GetMapping("/secure")
    fun secure(model: Model, @AuthenticationPrincipal oauth2User: OAuth2User): String {
        model.addAttribute("name", oauth2User.getAttribute<String>("sub"))
        return "secure"
    }
}
