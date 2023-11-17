package com.nt.rookies.assets.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Builder
@Getter
@Setter
public class ReportResourceDto {
    private Resource resource;
    private MediaType mediaType;
}
