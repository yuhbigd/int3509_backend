package com.project.nhatrotot.rest.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import com.project.nhatrotot.service.CountryService;
import com.project.nhatrotot.rest.api.CountryApi;
import com.project.nhatrotot.rest.dto.DistrictDto;
import com.project.nhatrotot.rest.dto.GetAllDistrictInProvince200ResponseDto;
import com.project.nhatrotot.rest.dto.GetAllProvince200ResponseDto;
import com.project.nhatrotot.rest.dto.GetAllWardsInDistrict200ResponseDto;
import com.project.nhatrotot.rest.dto.ProvinceDto;
import com.project.nhatrotot.rest.dto.WardDto;

@RestController
@RequestMapping("/api")
public class CountryController implements CountryApi {

    @Autowired
    private CountryService countryService;

    @Override
    public ResponseEntity<GetAllProvince200ResponseDto> getAllProvince() {
        var listProvinces = countryService.getAllProvince();
        var result = new GetAllProvince200ResponseDto();
        result.setProvinces(listProvinces);
        return new ResponseEntity<GetAllProvince200ResponseDto>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetAllDistrictInProvince200ResponseDto> getAllDistrictInProvince(String provinceCode) {
        var listDistrict = countryService.getDistrictsInProvince(provinceCode);
        var result = new GetAllDistrictInProvince200ResponseDto();
        result.setProvinces(listDistrict);
        return new ResponseEntity<GetAllDistrictInProvince200ResponseDto>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetAllWardsInDistrict200ResponseDto> getAllWardsInDistrict(String districtCode) {
        var listWard = countryService.getWardInDistrictsInProvince(districtCode);
        var result = new GetAllWardsInDistrict200ResponseDto();
        result.setWard(listWard);
        return new ResponseEntity<GetAllWardsInDistrict200ResponseDto>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DistrictDto> getDetailDistrict(String districtCode) {
        // TODO Auto-generated method stub
        return new ResponseEntity<DistrictDto>(countryService.getDetailsDistrict(districtCode), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProvinceDto> getDetailProvince(String provinceCode) {
        // TODO Auto-generated method stub
        return new ResponseEntity<ProvinceDto>(countryService.getDetailsProvince(provinceCode), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WardDto> getDetailWard(String wardCode) {
        // TODO Auto-generated method stub
        return new ResponseEntity<WardDto>(countryService.getDetailsWard(wardCode), HttpStatus.OK);
    }

}
