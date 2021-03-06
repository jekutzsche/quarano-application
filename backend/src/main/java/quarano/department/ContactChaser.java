package quarano.department;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.diary.DiaryManagement;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Felix Schultze
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class ContactChaser {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull DiaryManagement diaries;

	public Stream<Contact> findIndexContactsFor(TrackedCase contactCase) {

		if (!contactCase.isContactCase() && !contactCase.isMedicalContactCase()) {
			return Stream.empty();
		}

		return contactCase.getOriginContacts().stream().distinct() //
				.flatMap(it -> cases.findByTrackedPerson(it.getOwnerId()) //
						.map(indexCase -> fromIndexCase(indexCase, it)) //
						.stream());
	}

	// having that index case, we need to find the contact date per encounters of index person
	private Contact fromIndexCase(TrackedCase indexCase, ContactPerson contactPerson) {

		var trackedPerson = indexCase.getTrackedPerson();
		var encounters = trackedPerson.getEncounters();
		var encounterDate = encounters.getDateOfFirstEncounterWith(contactPerson) //
				.or(() -> diaries.findDiaryFor(trackedPerson).getDateOfFirstEncounterWith(contactPerson)) //
				.orElse(null);

		return new Contact(indexCase, contactPerson, encounterDate);
	}

	@Getter
	public static class Contact {

		private final TrackedCaseIdentifier caseId;
		private final TrackedPerson person;
		private final LocalDate contactAt;
		private final ContactPerson contactPerson;

		private Contact(TrackedCase trackedCase, ContactPerson contactPerson, @Nullable LocalDate contactAt) {
			this.caseId = trackedCase.getId();
			this.person = trackedCase.getTrackedPerson();
			this.contactAt = contactAt;
			this.contactPerson = contactPerson;
		}
	}
}
