package co.edu.univalle.validadores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("numeros")
public class NumerosValidator extends Validador implements Validator {

    public static final String PATTERN = "(-?\\d+)";
    public static final String MENSAJE = ": Debe contener solo numeros";

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if (o instanceof Integer) {
            o = "" + ((Integer) o);
        }
        validar(PATTERN, MENSAJE, uic, o);
    }
}
