package com.nk.carrental.dto.rental;

import jakarta.validation.constraints.Size;

public record AdminDecisionRequest(
        @Size(max = 2000) String note
) {}
