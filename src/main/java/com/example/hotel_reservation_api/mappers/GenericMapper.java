package com.example.hotel_reservation_api.mappers;

import com.example.hotel_reservation_api.dtos.CityDto;
import com.example.hotel_reservation_api.models.City;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {
    private final ModelMapper modelMapper;

    public GenericMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configureMappings();
    }

    public <D, E> D mapToDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <E, D> E mapToEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    private void configureMappings() {
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        // City -> CityDto
        modelMapper.addMappings(new PropertyMap<City, CityDto>() {
            @Override
            protected void configure() {
                map().setCountryId(source.getCountry().getId());
                map().setCountryName(source.getCountry().getName());
            }
        });
    }
}
