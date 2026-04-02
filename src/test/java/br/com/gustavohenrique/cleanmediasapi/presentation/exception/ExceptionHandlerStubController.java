package br.com.gustavohenrique.cleanmediasapi.presentation.exception;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExceptionHandlerStubController {

    static class SomeResourceNotFoundException extends NotFoundException {
        SomeResourceNotFoundException() { super("Resource with id 1 not found"); }
    }

    @GetMapping("/test/not-found")
    void notFound() { throw new SomeResourceNotFoundException(); }

    @GetMapping("/test/business")
    void business() { throw new BusinessException("name already exists"); }

    @GetMapping("/test/access-denied")
    void accessDenied() { throw new AccessDeniedException("Access Denied"); }
}
