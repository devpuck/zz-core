package com.zz.core.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * Api文档
 * </p>
 */
@Controller
@RequestMapping("/docs")
@ApiIgnore
public class ApiDocController extends BaseController
{

    /**
     * swaggerUI
     */
    @GetMapping("")
    public String swaggerUI()
    {
        return "redirect:/swagger-ui.html";
    }

}
