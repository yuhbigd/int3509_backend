package com.project.nhatrotot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.nhatrotot.mapper.ProvinceDtoMapper;
import com.project.nhatrotot.mapper.DistrictDtoMapper;
import com.project.nhatrotot.mapper.WardDtoMapper;
import com.project.nhatrotot.repository.jpa.ProvinceRepository;
import com.project.nhatrotot.repository.jpa.DistrictRepository;
import com.project.nhatrotot.repository.jpa.WardRepository;
import com.project.nhatrotot.rest.dto.ProvinceDto;
import com.project.nhatrotot.rest.dto.DistrictDto;
import com.project.nhatrotot.rest.dto.WardDto;
import java.util.List;
import com.project.nhatrotot.rest.advice.CustomException.GeneralException;
import org.springframework.http.HttpStatus;

@Service
public class CountryService {
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    WardRepository wardRepository;

    @Autowired
    ProvinceDtoMapper provinceDtoMapper;
    @Autowired
    DistrictDtoMapper districtDtoMapper;
    @Autowired
    WardDtoMapper wardDtoMapper;

    public List<ProvinceDto> getAllProvince() {
        var provinces = provinceRepository.findAll();
        return provinceDtoMapper.convertFromListProvinces(provinces);
    }

    public List<DistrictDto> getDistrictsInProvince(String provinceCode) {
        var province = provinceRepository.findById(provinceCode);
        if (!province.isPresent()) {
            throw new GeneralException("province not found", HttpStatus.NOT_FOUND);
        }
        return districtDtoMapper.convertFromListDistrict(province.get().getDistricts());
    }

    public List<WardDto> getWardInDistrictsInProvince(String DistrictCode) {
        var districtOpt = districtRepository.findById(DistrictCode);
        if (!districtOpt.isPresent()) {
            throw new GeneralException("district not found", HttpStatus.NOT_FOUND);
        }
        return wardDtoMapper.convertFromListWard(districtOpt.get().getWards());
    }

    public ProvinceDto getDetailsProvince(String provinceCode) {
        var provinceOpt = provinceRepository.findById(provinceCode);
        if (provinceOpt.isPresent()) {
            return provinceDtoMapper.convertFromProvinces(provinceOpt.get());
        }
        throw new GeneralException("province not found", HttpStatus.NOT_FOUND);
    }

    public DistrictDto getDetailsDistrict(String districtCode) {
        var districtOpt = districtRepository.findById(districtCode);
        if (districtOpt.isPresent()) {
            return districtDtoMapper.convertFromDistrict(districtOpt.get());
        }
        throw new GeneralException("district not found", HttpStatus.NOT_FOUND);
    }

    public WardDto getDetailsWard(String wardCode) {
        var wardOpt = wardRepository.findById(wardCode);
        if (wardOpt.isPresent()) {
            return wardDtoMapper.convertFromWard(wardOpt.get());
        }
        throw new GeneralException("ward not found", HttpStatus.NOT_FOUND);
    }

}
