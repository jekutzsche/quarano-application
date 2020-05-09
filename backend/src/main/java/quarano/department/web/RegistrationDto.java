package quarano.department.web;

import io.jsonwebtoken.lang.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.core.EmailAddress;
import quarano.core.validation.Strings;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.validation.Errors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

	private @Pattern(regexp = Strings.USERNAME) @NotBlank String username;
	private @NotBlank String password, passwordConfirm;  // password rules are tested in entity
	private @NotNull @Past LocalDate dateOfBirth;
	private @NotNull UUID clientCode;
	private @NotBlank @Pattern(regexp = EmailAddress.PATTERN) String email;
	private UUID clientId;
	private String departmentId;

	Errors validate(Errors errors) {

		if (!Objects.nullSafeEquals(password, passwordConfirm)) {
			errors.rejectValue("passwordConfirm", "NonMatching.password");
		}

		return errors;
	}
}
