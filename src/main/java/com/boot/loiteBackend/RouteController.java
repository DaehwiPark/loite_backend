package com.boot.loiteBackend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {

    @RequestMapping({
            "/",
            "/cherry-blossom",
            "/christmas",
            "/family-month",
            "/{path:^(?!api|static|.*\\..*).*$}"  // 정적 리소스나 API 제외
    })
    public String forward() {
        return "forward:/index.html";
    }
}