package com.dnd.safety.data.repository

import com.dnd.safety.data.remote.api.LawDistrictService
import com.dnd.safety.domain.mapper.toLawDistricts
import com.dnd.safety.domain.model.LawDistrict
import com.dnd.safety.domain.model.Point
import com.dnd.safety.domain.model.PointDto
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.dnd.safety.utils.Logger
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate
import javax.inject.Inject

class LawDistrictRepositoryImpl @Inject constructor(
    private val lawDistrictService: LawDistrictService
) : LawDistrictRepository {

    override suspend fun getLawDistricts(keyword: String): ApiResponse<List<LawDistrict>> {
        return lawDistrictService.getLawDistricts(keyword)
            .mapSuccess {
                toLawDistricts()
            }.onFailure {
                Logger.e(message())
            }.onError {
                Logger.e(message())
            }
    }

    override suspend fun getPoint(pointDto: PointDto): ApiResponse<Point> {
        return lawDistrictService.getPoint(
            pointDto.admCd,
            pointDto.rnMgtSn,
            pointDto.udrtYn,
            pointDto.buldMnnm,
            pointDto.buldSlno
        ).mapSuccess {
            val juso = results.juso.first()
            convertUTM_KToWGS84(juso.entX, juso.entY)
        }.onFailure {
            Logger.e(message())
        }.onError {
            Logger.e(message())
        }
    }
}

private fun convertUTM_KToWGS84(x: Double, y: Double): Point {
    val crsFactory = CRSFactory()
    val transformFactory = CoordinateTransformFactory()

    // EPSG:5179 (UTM-K) → EPSG:4326 (WGS84)
    val sourceCRS = crsFactory.createFromName("epsg:5179")
    val targetCRS = crsFactory.createFromName("epsg:4326")
    val transform = transformFactory.createTransform(sourceCRS, targetCRS)

    val srcCoord = ProjCoordinate(x, y)
    val destCoord = ProjCoordinate()
    transform.transform(srcCoord, destCoord)

    return Point(destCoord.x, destCoord.y)  // (위도, 경도)
}
