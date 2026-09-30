package com.videoplatform.call.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calls")
public class CallController {
    private final CallService callService;
    public CallController(CallService callService) { this.callService = callService; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CallDtos.CallResponse create(@Valid @RequestBody CallDtos.CreateCallRequest request, Authentication authentication) {
        return callService.create(userId(authentication), request);
    }

    @PostMapping("/{id}/join")
    public CallDtos.CallResponse join(@PathVariable("id") UUID id, Authentication authentication) {
        return callService.join(id, userId(authentication));
    }

    @PatchMapping("/{id}/end")
    public CallDtos.CallResponse end(@PathVariable("id") UUID id, Authentication authentication) {
        return callService.end(id, userId(authentication));
    }

    @PatchMapping("/{id}/reject")
    public CallDtos.CallResponse reject(@PathVariable("id") UUID id, Authentication authentication) {
        return callService.reject(id, userId(authentication));
    }

    @GetMapping
    public List<CallDtos.CallResponse> history(Authentication authentication) {
        return callService.history(userId(authentication));
    }

    @GetMapping("/{id}")
    public CallDtos.CallResponse get(@PathVariable("id") UUID id, Authentication authentication) {
        return callService.get(id, userId(authentication));
    }

    private UUID userId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
