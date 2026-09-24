package io.github.obrenoxs.simuladev.ai.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeminiRequest(

        @JsonProperty("system_instruction")
        SystemInstruction systemInstruction,

        List<Content> contents
) {}
