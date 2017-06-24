package nextbike.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public class ValidationUtils {

	public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static void reject(Errors errors, String fieldName,
			String errorMessage) {
		errors.rejectValue(fieldName, errorMessage, errorMessage);
	}

	public static void rejectIfTooLong(Errors errors, String fieldName,
			String fieldValue, int maxLength, String errorMessage) {
		if (fieldValue != null && fieldValue.length() > maxLength) {
			errors.rejectValue(fieldName, errorMessage, errorMessage);
		}
	}

	public static void rejectIfEmpty(Errors errors, String fieldName,
			String errorMessage) {
		org.springframework.validation.ValidationUtils.rejectIfEmpty(errors,
				fieldName, errorMessage, errorMessage);
	}

	public static void rejectIfNoMatch(Errors errors, String fieldName,
			String fieldValue, String regexp, String errorMessage) {
		if (fieldValue == null) {
			errors.rejectValue(fieldName, errorMessage, errorMessage);
			return;
		}
		Matcher matcher = Pattern.compile(regexp).matcher(fieldValue);
		if (!matcher.matches()) {
			errors.rejectValue(fieldName, errorMessage, errorMessage);
		}
	}

	public static void rejectIfNoMatchAcceptNulls(Errors errors,
			String fieldName, String fieldValue, String regexp,
			String errorMessage) {
		if (fieldValue == null) {
			return;
		}

		rejectIfNoMatch(errors, fieldName, fieldValue, regexp, errorMessage);
	}

	public static void rejectIfEmptyList(Errors errors, String fieldName,
			@SuppressWarnings("rawtypes") List fieldValue, String errorMessage) {
		if (fieldValue == null || fieldValue.size() == 0) {
			errors.rejectValue(fieldName, errorMessage, errorMessage);
		}
	}

	public static void rejectIfNoValidNRB(Errors errors, String fieldName,
			String fieldValue, String errorMessage) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			return;
		}
		if (!isValidNRBAccount(fieldValue)) {
			errors.rejectValue(fieldName, errorMessage, errorMessage);
		}

	}

	public static boolean isValidNRBAccount(String bankAccountNumber) {
		if (bankAccountNumber == null || bankAccountNumber.length() != 26)
			return false;

		Matcher matcher = Pattern.compile("\\d{26}").matcher(bankAccountNumber);
		if (!matcher.matches())
			return false;

		// NRB validation
		StringBuilder tmpAccount = new StringBuilder(bankAccountNumber);
		tmpAccount.append("2521");
		String tmp = tmpAccount.toString();
		String nrb = tmp.substring(2) + tmp.substring(0, 2);
		int[] wagi = new int[] { 1, 10, 3, 30, 9, 90, 27, 76, 81, 34, 49, 5,
				50, 15, 53, 45, 62, 38, 89, 17, 73, 51, 25, 56, 75, 71, 31, 19,
				93, 57 };

		int sum = 0;
		try {
			for (int index = 0; index < wagi.length; index++) {
				sum += Integer.parseInt(new String(new char[] { nrb.charAt(nrb
						.length() - index - 1) }))
						* wagi[index];

			}
		} catch (NumberFormatException e1) {
			return false;
		}

		if (sum % 97 != 1)
			return false;

		return true;
	}

	public static void validateEmailList(Errors errors, String fieldName,
			String fieldValue, String errorMessage, String emailListSeparator) {
		String[] split = fieldValue.split(emailListSeparator);
		for (String email : split) {
			Matcher matcher = Pattern.compile(EMAIL_REGEX)
					.matcher(email.trim());
			if (!matcher.matches()) {
				errors.rejectValue(fieldName, errorMessage, errorMessage);
				return;
			}
		}
	}
}
