package pl.edu.pg.eti.kask.boatcharter.boatType.model.converter;

import pl.edu.pg.eti.kask.boatcharter.boatType.entity.BoatType;
import pl.edu.pg.eti.kask.boatcharter.boatType.model.BoatTypeModel;
import pl.edu.pg.eti.kask.boatcharter.boatType.service.BoatTypeService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Faces converter for {@link BoatTypeModel}. The managed attribute in {@link @FacesConverter} allows the converter
 * to be the CDI bean. In previous version of JSF converters were always created inside JSF lifecycle and where not
 * managed by container that is injection was not possible. As this bean is not annotated with scope the beans.xml
 * descriptor must be present.
 */
@FacesConverter(forClass = BoatTypeModel.class, managed = true)
public class BoatTypeModelConverter implements Converter<BoatTypeModel> {

    /**
     * Service for boatTypes management.
     */
    private BoatTypeService service;

    @Inject
    public BoatTypeModelConverter(BoatTypeService service) {
        this.service = service;
    }

    @Override
    public BoatTypeModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Long val = Long.parseLong(value);
        Optional<BoatType> boatType = service.find(val);
        return boatType.isEmpty() ? null : BoatTypeModel.entityToModelMapper().apply(boatType.get());
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BoatTypeModel value) {
        return value == null ? "" : value.getName();
    }

}
