package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private Collection<Long> events;
    private Boolean pinned = false;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
