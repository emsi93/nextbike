package nextbike.validator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import nextbike.model.RoadStation;
import nextbike.modules.ValidationUtils;

@Component("roadFormValidator")
@Scope("singleton")
public class RoadFormValidator implements Validator {

	private static final String OBLIGATORY_FIELD_ERROR_MSG = "Pole jest wymagane.";
	private static final String DIFFERENT_POINTS_ERROR_MSG = "Punkty musz¹ siê od siebie ró¿niæ";

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return RoadStation.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

		RoadStation roadStation = (RoadStation) target;

		if (roadStation.getPointA().equals(roadStation.getPointB()) && !(roadStation.getPointA().equals(""))) {
			ValidationUtils.reject(errors, "pointA", DIFFERENT_POINTS_ERROR_MSG);
			ValidationUtils.reject(errors, "pointB", DIFFERENT_POINTS_ERROR_MSG);
		}
		if (roadStation.getPointA().equals("") || roadStation.getPointA() == null)
			ValidationUtils.rejectIfEmpty(errors, "pointA", OBLIGATORY_FIELD_ERROR_MSG);
		if (roadStation.getPointB().equals("") || roadStation.getPointB() == null)
			ValidationUtils.rejectIfEmpty(errors, "pointB", OBLIGATORY_FIELD_ERROR_MSG);
	}

}
