package ch.wisv.controller;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller for error handling.
 */
@Controller
public class GlobalErrorController extends AbstractErrorController {

    private static final String ERROR_PATH = "/error";
    private static final String ERROR_TEMPLATE = "customError";

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(ERROR_PATH)
    public String error(Model model, HttpServletRequest request) {

        // {error={timestamp=Mon Nov 02 12:40:50 EST 2015, status=404, error=Not Found, message=No message available, path=/foo}}
        Map<String, Object> error = getErrorAttributes(request, true);

        model.addAttribute("timestamp", error.get("timestamp"));
        model.addAttribute("status", error.get("status"));
        model.addAttribute("error", error.get("error"));
        model.addAttribute("message", error.get("message"));
        model.addAttribute("path", error.get("path"));

        return ERROR_TEMPLATE;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


}
