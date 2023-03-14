package com.project.nhatrotot.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nhatrotot.mapper.HouseCreatingDetailsDtoMapper;
import com.project.nhatrotot.mapper.HouseDtoMapper;
import com.project.nhatrotot.mapper.HouseSearchDetailDtoMapper;
import com.project.nhatrotot.mapper.HouseUpdatableFieldsDtoMapper;
import com.project.nhatrotot.model.House;
import com.project.nhatrotot.model.HouseES;
import com.project.nhatrotot.model.HouseImages;
import com.project.nhatrotot.model.UserEntity;
import com.project.nhatrotot.model.Wards;
import com.project.nhatrotot.repository.jpa.HouseCategoryRepository;
import com.project.nhatrotot.repository.jpa.HouseRepository;
import com.project.nhatrotot.repository.jpa.HouseTypeRepository;
import com.project.nhatrotot.repository.jpa.UserEntityRepository;
import com.project.nhatrotot.repository.jpa.WardRepository;
import com.project.nhatrotot.rest.advice.CustomException.CustomException;
import com.project.nhatrotot.rest.advice.CustomException.GeneralException;
import com.project.nhatrotot.rest.advice.CustomException.InsufficientAmount;
import com.project.nhatrotot.rest.dto.GetHouseHandle200ResponseDto;
import com.project.nhatrotot.rest.dto.HouseCreatingDetailsDto;
import com.project.nhatrotot.rest.dto.HouseDto;
import com.project.nhatrotot.rest.dto.HouseMapSearchDto;
import com.project.nhatrotot.rest.dto.HouseNormalSearchDto;
import com.project.nhatrotot.rest.dto.HouseUpdatableFieldsDto;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoShapeRelation;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.json.JsonData;

@Service
public class HouseService {
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private HouseTypeRepository houseTypeRepository;
    @Autowired
    private HouseCategoryRepository houseCategoryRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    HouseCreatingDetailsDtoMapper houseCreatingDetailsDtoMapper;
    @Autowired
    private HouseDtoMapper houseDtoMapper;
    @Autowired
    private HouseUpdatableFieldsDtoMapper houseUpdatableFieldsDtoMapper;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private HouseSearchDetailDtoMapper houseSearchDetailsDtoMapper;

    @Transactional(rollbackFor = { Exception.class, Throwable.class,
            CustomException.class }, isolation = Isolation.REPEATABLE_READ)
    public void createHousePost(HouseCreatingDetailsDto houseCreatingDetailsDto, String userId) {
        UserEntity user = userEntityRepository.findById(userId).get();
        if (houseCreatingDetailsDto.getHouseType().equals(5) && houseCreatingDetailsDto.getHouseCategory().equals(1)) {
            throw new GeneralException("sell a room for lease ???", HttpStatus.BAD_REQUEST);
        }
        BigDecimal afterSubtract = user.getBalance().subtract(BigDecimal.valueOf(40000 * 100L));
        if (afterSubtract.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientAmount("not enough money", HttpStatus.PAYMENT_REQUIRED);
        }
        userEntityRepository.updateBalance(userId, BigDecimal.valueOf(-40000 *
                100L));
        House house = houseCreatingDetailsDtoMapper.convertFromHouseCreatingDetailDto(houseCreatingDetailsDto);
        house.setOwner(user);
        house.setCreatedDate(LocalDateTime.now());
        var houseImages = houseCreatingDetailsDto.getImages().stream().map((image) -> {
            HouseImages houseImage = new HouseImages();
            houseImage.setUrl(image);
            return houseImage;
        }).collect(Collectors.toSet());
        house.setHouseImages(houseImages);
        var category = houseCategoryRepository.findById(houseCreatingDetailsDto.getHouseCategory()).get();
        var type = houseTypeRepository.findById(houseCreatingDetailsDto.getHouseType()).get();
        house.setHouseType(type);
        house.setHouseCategory(category);
        Wards ward = wardRepository.findById(houseCreatingDetailsDto.getWard()).get();
        var district = ward.getDistrict();
        var province = district.getProvince();
        house.setWard(ward);
        house.setDistrict(district);
        house.setProvince(province);
        houseRepository.save(house);
    }

    public HouseDto getHouseById(Integer HouseId) {
        var houseOpt = houseRepository.findById(HouseId);
        if (!houseOpt.isPresent()) {
            throw new GeneralException("not found", HttpStatus.NOT_FOUND);
        }
        return houseDtoMapper.convertFromHouse(houseOpt.get());
    }

    public void updateHouseById(String userId, boolean isAdmin, Integer houseId,
            HouseUpdatableFieldsDto houseUpdatableFieldsDto) {
        var houseNeedToUpdateOpt = houseRepository.findById(houseId);
        if (!houseNeedToUpdateOpt.isPresent()) {
            throw new GeneralException("house not found", HttpStatus.NOT_FOUND);
        }
        var houseNeedToUpdate = houseNeedToUpdateOpt.get();
        if (!houseNeedToUpdate.getOwner().getUserId().equals(userId) && !isAdmin) {
            throw new GeneralException("you don't have enough privilege to update this house", HttpStatus.FORBIDDEN);
        }
        House houseWithUpdatedFields = houseUpdatableFieldsDtoMapper
                .convertFromHouseUpdatableFields(houseUpdatableFieldsDto);
        houseWithUpdatedFields.merger(houseNeedToUpdateOpt.get());
        houseRepository.save(houseWithUpdatedFields);
    }

    public void deleteHouseById(String userId, boolean isAdmin, Integer houseId) {
        var houseNeedToDeleteOpt = houseRepository.findById(houseId);
        if (!houseNeedToDeleteOpt.isPresent()) {
            throw new GeneralException("house not found", HttpStatus.NOT_FOUND);
        }
        var houseNeedToDelete = houseNeedToDeleteOpt.get();
        if (!houseNeedToDelete.getOwner().getUserId().equals(userId) && !isAdmin) {
            throw new GeneralException("you don't have enough privilege to update this house", HttpStatus.FORBIDDEN);
        }
        houseRepository.delete(houseNeedToDelete);
    }

    public GetHouseHandle200ResponseDto getHouses(String queryFor,
            String queryType, BigDecimal distance,
            List<BigDecimal> polygonPoints,
            List<BigDecimal> mapPoint, Integer pageSize,
            Integer pageNumber, Integer houseType, String ownerId,
            Integer houseCategory, Boolean hasAc, Boolean hasParking,
            Boolean hasElevator, Boolean hasFurnished, Boolean allowPet, String province,
            String district, String ward, BigDecimal squareLte,
            BigDecimal squareGte, Integer roomLte,
            Integer roomGte, Integer bedRoomLte, Integer bedRoomGte,
            Integer bathRoomLte, Integer bathRoomGte, LocalDate fromDate,
            LocalDate toDate, BigDecimal priceFrom,
            BigDecimal priceTo, String sortBy, String sortOrder) {
        List<Query> filterQueries = new ArrayList<>();
        List<Query> shouldQueries = new ArrayList<>();
        List<SortOptions> sortOptions = new ArrayList<>();
        filterQueries.add(createTermQueryWithIntegerValue("visible", 1));
        if (houseType != null) {
            filterQueries.add(createTermQueryWithIntegerValue("house_type", houseType));
        }
        if (houseCategory != null) {
            filterQueries.add(createTermQueryWithIntegerValue("trade_category", houseCategory));
        }
        if (hasAc != null) {
            filterQueries.add(createTermQueryWithBooleanValue("ac", hasAc));
        }
        if (hasParking != null) {
            filterQueries.add(createTermQueryWithBooleanValue("parking", hasParking));
        }
        if (hasElevator != null) {
            filterQueries.add(createTermQueryWithBooleanValue("elevator", hasElevator));
        }
        if (hasFurnished != null) {
            filterQueries.add(createTermQueryWithBooleanValue("furnished", hasFurnished));
        }
        if (allowPet != null) {
            filterQueries.add(createTermQueryWithBooleanValue("pet", allowPet));
        }
        if (ownerId != null) {
            filterQueries.add(createTermQueryWithStringValue("owner_id", ownerId));

        }
        if (district != null) {
            filterQueries.add(createTermQueryWithStringValue("district_code", district));

        }
        if (ward != null) {
            filterQueries.add(createTermQueryWithStringValue("wards_code", ward));

        }
        if (province != null) {
            filterQueries.add(createTermQueryWithStringValue("province_code", province));

        }
        if (squareLte != null || squareGte != null) {
            filterQueries.add(createRangeQueryFromNumberValue("square", squareGte, squareLte));
        }
        if (roomLte != null || roomGte != null) {
            filterQueries.add(createRangeQueryFromNumberValue("rooms", roomGte, roomLte));
        }
        if (bedRoomLte != null || bedRoomLte != null) {
            filterQueries.add(createRangeQueryFromNumberValue("bedrooms", bedRoomGte, bedRoomLte));
        }
        if (bathRoomLte != null || bathRoomGte != null) {
            filterQueries.add(createRangeQueryFromNumberValue("bath_rooms", bathRoomGte, bathRoomLte));
        }
        if (priceFrom != null || priceTo != null) {
            filterQueries.add(createRangeQueryFromNumberValue("price", priceFrom, priceTo));
        }
        if (fromDate != null || toDate != null) {
            filterQueries.add(createRangeQueryFromLocalDate("created_date", fromDate, toDate));
        }

        if (queryType.equals("distance")) {
            if (mapPoint == null) {
                throw new GeneralException("mapPoint is missing", HttpStatus.BAD_REQUEST);
            }
            if (distance == null) {
                throw new GeneralException("distance is missing", HttpStatus.BAD_REQUEST);
            }
            List<Double> point = mapPoint.stream().map(m -> m.doubleValue()).toList();
            Query distanceQuery = createDistanceQuery("location", point, distance.doubleValue());
            filterQueries.add(distanceQuery);
        } else if (queryType.equals("polygon")) {
            if (polygonPoints.size() % 2 != 0) {
                throw new GeneralException(
                        "wrong polygonPoints format. Polygon point need to be an array with size can be divided by 2",
                        HttpStatus.BAD_REQUEST);
            }

            try {
                String jsonPolyGon = "[";
                for (int i = 0; i < polygonPoints.size(); i++) {
                    String pointValue = polygonPoints.get(i).toString();
                    if (i % 2 == 0) {
                        jsonPolyGon += "[" + pointValue + ",";
                    } else {
                        jsonPolyGon += pointValue + "],";
                    }
                }
                if (jsonPolyGon.length() > 1) {
                    jsonPolyGon = jsonPolyGon.substring(0, jsonPolyGon.length() - 1);
                }
                jsonPolyGon += "]";
                var node = new ObjectMapper().readTree("{\"type\": \"polygon\",\"coordinates\":[" + jsonPolyGon + "]}");
                var polygonJsonData = JsonData.of(node);
                Query polygonQuery = new Query.Builder()
                        .geoShape(g -> g
                                .field("location")
                                .shape(s -> s
                                        .relation(GeoShapeRelation.Within)
                                        .shape(polygonJsonData)))
                        .build();
                filterQueries.add(polygonQuery);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException(
                        "error: convert polygon to json",
                        HttpStatus.BAD_REQUEST);
            }
        }
        if (queryFor.equals("map")) {
            try {
                SourceConfig sourceConfig = SourceConfig.of(s -> s.filter(v -> v.includes(List.of("house_id", "price",
                        "thumbnail", "latitude", "longitude", "address", "house_type", "trade_category", "title"))));
                SearchResponse<HouseES> searchResponse = elasticsearchClient.search(
                        s -> s
                                .index("houses")
                                .source(sourceConfig)
                                .query(q -> q
                                        .bool(b -> b
                                                .filter(filterQueries)
                                                .should(shouldQueries)))
                                .size(300)
                                .trackTotalHits(t -> t.enabled(true)),
                        HouseES.class);
                var totalHouses = searchResponse.hits().total().value();
                var size = searchResponse.hits().hits().size();
                var housesList = searchResponse.hits().hits().stream().map(hit -> hit.source()).toList();
                var houseListDto = houseSearchDetailsDtoMapper.convertFromListHouseES(housesList);
                var houseMapSearch = new HouseMapSearchDto();
                houseMapSearch.setHouses(houseListDto);
                houseMapSearch.setSize(size);
                houseMapSearch.setTotalSize((int) totalHouses);
                return houseMapSearch;
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException("Error when querying map", HttpStatus.BAD_REQUEST);
            }
        } else {
            if (sortBy.equals("distanceToPoint")) {
                if (mapPoint == null) {
                    throw new GeneralException("mapPoint is missing. Can not sort by distance",
                            HttpStatus.BAD_REQUEST);
                }
                if (distance == null) {
                    throw new GeneralException("distance is missing. Can not sort by distance", HttpStatus.BAD_REQUEST);
                }
                try {
                    var originJson = new ObjectMapper().readTree(
                            "{ \"lon\":" + mapPoint.get(0).toString() + ", \"lat\":" + mapPoint.get(1).toString()
                                    + "}");
                    var pivotJson = new ObjectMapper().readTree("\"" + distance.toString() + "km\"");
                    Query sortByDistanceQuery = new Query.Builder()
                            .distanceFeature(
                                    dis -> dis
                                            .field("location")
                                            .origin(JsonData.of(originJson))
                                            .pivot(JsonData.of(pivotJson)))
                            .build();
                    shouldQueries.add(sortByDistanceQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new GeneralException(
                            "error: convert map point to json",
                            HttpStatus.BAD_REQUEST);
                }
            } else if (sortBy.equals("created_date")) {
                var sortByDate = createSortOptionByField("created_date", sortOrder);
                sortOptions.add(sortByDate);
            } else if (sortBy.equals("price")) {
                var sortByDate = createSortOptionByField("price", sortOrder);
                sortOptions.add(sortByDate);
            }

            try {
                SourceConfig sourceConfig = SourceConfig.of(s -> s.filter(v -> v.includes(List.of("house_id", "price",
                        "thumbnail", "latitude", "longitude", "address", "house_type", "trade_category", "title"))));
                var skippedHouse = pageSize.intValue() * pageNumber.intValue();
                SearchResponse<HouseES> searchResponse = elasticsearchClient.search(
                        s -> s
                                .index("houses")
                                .source(sourceConfig)
                                .query(q -> q
                                        .bool(b -> b
                                                .filter(filterQueries)
                                                .should(shouldQueries)))
                                .size(pageSize.intValue())
                                .from(skippedHouse)
                                .sort(sortOptions)
                                .trackTotalHits(t -> t.enabled(true)),
                        HouseES.class);
                var totalHousesSearched = searchResponse.hits().total().value();
                var totalPage = Math.ceil((double) totalHousesSearched / pageSize.intValue());
                var housesList = searchResponse.hits().hits().stream().map(hit -> hit.source()).toList();
                var houseListDto = houseSearchDetailsDtoMapper.convertFromListHouseES(housesList);
                var houseNormalPage = new HouseNormalSearchDto();
                houseNormalPage.setCurrentPage(pageNumber);
                houseNormalPage.setTotalPage((int) totalPage);
                houseNormalPage.houses(houseListDto);
                return houseNormalPage;
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException("Error when querying page", HttpStatus.BAD_REQUEST);
            }
        }
    }

    private Query createTermQueryWithIntegerValue(String fieldName, Integer value) {
        return Query.of(q -> q.term(t -> t.field(fieldName).value(value)));
    }

    private Query createTermQueryWithStringValue(String fieldName, String value) {
        return Query.of(q -> q.term(t -> t.field(fieldName).value(value)));
    }

    private Query createTermQueryWithBooleanValue(String fieldName, Boolean value) {
        Integer searchValue = value ? 1 : 0;// because mysql dont store boolean and debezium convert to short type so in
                                            // elastic search it is stored in int type
        return createTermQueryWithIntegerValue(fieldName, searchValue);
    }

    private <T extends Number> Query createRangeQueryFromNumberValue(String fieldName, T from, T to) {
        var rangeQuery = new RangeQuery.Builder();
        rangeQuery.field(fieldName);
        if (from != null) {
            rangeQuery.from(from.toString());
        }
        if (to != null) {
            rangeQuery.to(to.toString());
        }
        return rangeQuery.build()._toQuery();
    }

    private Query createRangeQueryFromLocalDate(String fieldName, LocalDate from, LocalDate to) {
        var rangeQuery = new RangeQuery.Builder();
        rangeQuery.field(fieldName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (from != null) {
            rangeQuery.from(from.format(formatter));
        }
        if (to != null) {
            rangeQuery.to(to.format(formatter));
        }
        return rangeQuery.build()._toQuery();
    }

    private Query createDistanceQuery(String fieldName, List<Double> point, Double distance) {
        return Query
                .of(q -> q.geoDistance(g -> g
                        .field(fieldName)
                        .distance(distance.toString() + "km")
                        .location(l -> l.coords(point))));
    }

    private SortOptions createSortOptionByField(String fieldName, String sortOrder) {
        SortOrder sOrder;
        if (sortOrder.equals("asc")) {
            sOrder = SortOrder.Asc;
        } else {
            sOrder = SortOrder.Desc;
        }
        return SortOptions.of(s -> s.field(f -> f.field(fieldName).order(sOrder)));
    }
}
